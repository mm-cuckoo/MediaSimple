package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.camera.info.CameraInfoManagerImpl;
import com.cfox.camera.camera.session.IPhotoRequestBuilderManager;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.ImageSession;
import com.cfox.camera.camera.session.helper.ImageSessionHelper;
import com.cfox.camera.camera.session.builder.ImageRequestBuilderManger;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 负责挣了并发送个camera session 动作
 */
public class ImageSessionHelperImpl extends AbsCameraSessionHelper implements ImageSessionHelper {

    private CaptureRequest.Builder mPreviewBuilder;

    private DeviceSession mImageSession;
    private final IPhotoRequestBuilderManager mRequestBuilderManager;
    private final CameraInfoManager mCameraInfoManager = CameraInfoManagerImpl.CAMERA_INFO_MANAGER;
    private final ISessionManager mCameraSessionManager;

    public ImageSessionHelperImpl(ISessionManager sessionManager) {
        mCameraSessionManager = sessionManager;
        mRequestBuilderManager = new ImageRequestBuilderManger(mCameraInfoManager);
    }

    @Override
    public void init() {

    }

    @Override
    Observable<EsResult> beforeOpenCamera(EsRequest request) {
        mImageSession = null;
        return mCameraSessionManager.closeSession();
    }

    @Override
    public void applyPreviewRepeatingBuilder(EsRequest request) throws CameraAccessException {
        mPreviewBuilder = createPreviewBuilder(request);
        applyBuilderSettings(request);
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        request.put(Es.Key.SESSION_CALLBACK, mCaptureCallback.setType(CaptureSessionCallback.TYPE_PREVIEW));
    }

    private CaptureRequest.Builder createPreviewBuilder(EsRequest request) throws CameraAccessException {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);
        CaptureRequest.Builder builder = mImageSession.onCreateRequestBuilder(CameraDevice.TEMPLATE_PREVIEW);
        // TODO: 2020-01-11 adjust add more target
        builder.addTarget(surfaceHelper.getSurface());
        return builder;
    }

    private void applyBuilderSettings(EsRequest request) {
        int flashValue = request.getInt(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.OFF);
        mRequestBuilderManager.getFlashRequest(getPreviewBuilder(), flashValue);
        mRequestBuilderManager.getPreviewRequest(getPreviewBuilder());
    }

    private CaptureRequest.Builder getPreviewBuilder() {
        return mPreviewBuilder;
    }

    private CaptureRequest.Builder getCaptureBuilder(EsRequest request) {
        mRequestBuilderManager.captureBuilder(mPreviewBuilder);
        return mPreviewBuilder;
    }


    @Override
    public Observable<EsResult> onSendRepeatingRequest(EsRequest request) {
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        return mImageSession.onRepeatingRequest(request);
    }


    @Override
    public Observable<EsResult> close(EsRequest request) {
        return mCameraSessionManager.closeSession();
    }

    @Override
    public Observable<EsResult> capture(final EsRequest request) {
        EsLog.d("capture: ......3333...");
        request.put(Es.Key.REQUEST_BUILDER, getCaptureBuilder(request));
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(final ObservableEmitter<EsResult> emitter) throws Exception {
                boolean previewCapture = request.getBoolean(Es.Key.PREVIEW_CAPTURE, false);
                EsLog.d("subscribe: capture: ......3333...previewCapture：" + previewCapture);

                if (previewCapture) {
                    mImageSession.capture(request);
                    emitter.onNext(new EsResult());
                    return;
                }

//                mCaptureTime = SystemClock.elapsedRealtime();
                mCaptureCallback.setType(CaptureSessionCallback.TYPE_CAPTURE);
                mCaptureCallback.setEmitter(emitter);
                mImageSession.capture(request);
            }
        });
    }

    @Override
    public Observable<EsResult> captureStillPicture(final EsRequest request) {
        EsLog.d("captureStillPicture: .......");
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(final ObservableEmitter<EsResult> emitter) throws Exception {
                mImageSession.stopRepeating();
                mImageSession.capture(request);
            }
        });
    }

    @Override
    public DeviceSession getCameraSession(EsRequest request) {
        if (mImageSession == null) {
            mImageSession = mCameraSessionManager.createSession();
        }
        return mImageSession;
    }

    private CaptureSessionCallback mCaptureCallback = new CaptureSessionCallback();

    public class CaptureSessionCallback extends CameraCaptureSession.CaptureCallback {

        public static final int TYPE_PREVIEW = 1;
        private static final int TYPE_REPEAT = 2;
        private static final int TYPE_CAPTURE = 3;

        private final Object mCaptureLock = new Object();

        private ObservableEmitter<EsResult> mEmitter;
        private int mType = 0;
        private int mAFState = -1;
        private boolean mFirstFrameCompleted = false;
        private boolean mCaptured = false;

        CaptureSessionCallback setType(int type) {
            this.mType = type;
            if (type == TYPE_PREVIEW) {
                mFirstFrameCompleted = false;
            } else if (type == TYPE_CAPTURE) {
                mCaptured = false;
            }
            return this;
        }


        public CaptureSessionCallback setEmitter(ObservableEmitter<EsResult> emitter) {
            this.mEmitter = emitter;
            return this;
        }


        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            updateAFState(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            if (!mFirstFrameCompleted) {
                mFirstFrameCompleted = true;
                EsResult esResult = new EsResult();
                esResult.put(Es.Key.FIRST_FRAME_CALLBACK, Es.Value.OK);
                mEmitter.onNext(esResult);
                EsLog.d("preview first frame call back");
            }

            updateAFState(result);
        }

        private void updateAFState(CaptureResult captureResult) {
//            Integer afState = captureResult.get(CaptureResult.CONTROL_AF_STATE);
//            if (afState != null && afState != mAFState) {
//                mAFState = afState;
//                EsResult result = new EsResult();
//                result.put(Es.Key.AF_CHANGE_STATE, afState);
//                mEmitter.onNext(result);
//            }

            synchronized (mCaptureLock) {
                if (mType != TYPE_CAPTURE) return;

                boolean readyCapture = true;
                if (mCameraInfoManager.isAutoFocusSupported()) {
                    Integer afState = captureResult.get(CaptureResult.CONTROL_AF_STATE);
                    EsLog.d("onCapture: af state   " + afState);

                    if (afState == null) return;

                    readyCapture = CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState
                            || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState;
                }
//
                if (!mCameraInfoManager.isLegacyLocked()) {
                    Integer aeState = captureResult.get(CaptureResult.CONTROL_AE_STATE);
                    Integer awbState = captureResult.get(CaptureResult.CONTROL_AWB_STATE);
                    EsLog.d("onCapture: ae  :" + aeState  + "   awb:" + awbState);
                    if (aeState == null || awbState == null) {
                        return;
                    }

                    readyCapture = readyCapture &&
                            aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED &&
                            awbState == CaptureResult.CONTROL_AWB_STATE_CONVERGED;
                }
//
                EsLog.d( "onCapture: readyCapture:" + readyCapture);

//                if (!readyCapture && hitTimeoutLocked()) {
//                    Log.w(TAG, "Timed out waiting for pre-capture sequence to complete.");
//                    readyCapture = true;
//                }
                EsLog.d("onCapture: mFlag:" + mType  + "   readyCapture:" + readyCapture  + "   mCaptured"  + mCaptured);
                if (readyCapture && !mCaptured) {
                    mCaptured = true;
                    mType = 0;
                    mEmitter.onNext(new EsResult());
                }
            }
        }
    }

}
