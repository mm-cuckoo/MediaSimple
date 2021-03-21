package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.camera.info.CameraInfoManagerImpl;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.RequestBuilderManager;
import com.cfox.camera.camera.session.helper.PhotoSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 负责挣了并发送个camera session 动作
 */
public class PhotoSessionHelperImpl extends AbsCameraSessionHelper implements PhotoSessionHelper {

    private CaptureRequest.Builder mPreviewBuilder;

    private DeviceSession mImageSession;
    private final RequestBuilderManager mRequestBuilderManager;
    private final CameraInfoManager mCameraInfoManager = CameraInfoManagerImpl.CAMERA_INFO_MANAGER;
    private final ISessionManager mCameraSessionManager;
    private ISurfaceHelper mSurfaceHelper;

    public PhotoSessionHelperImpl(ISessionManager sessionManager) {
        mCameraSessionManager = sessionManager;
        mRequestBuilderManager = new RequestBuilderManager(mCameraInfoManager);
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
        mSurfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);
        mPreviewBuilder = createPreviewBuilder(mSurfaceHelper.getSurface());
        applyPreviewBuilder(request);
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        mPreviewCallback.setType(CaptureSessionCallback.STATE_PREVIEW);
        request.put(Es.Key.SESSION_CALLBACK, mPreviewCallback);
    }

    private void applyPreviewBuilder(EsRequest request) {
        int flashValue = request.getInt(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.OFF);
        mRequestBuilderManager.getFlashRequest(getPreviewBuilder(), flashValue);
        mRequestBuilderManager.applyPreviewRequest(getPreviewBuilder());
    }

    private CaptureRequest.Builder getPreviewBuilder() {
        return mPreviewBuilder;
    }

    private CaptureRequest.Builder createCaptureBuilder(List<Surface> surfaceList) {
        return createBuilder(CameraDevice.TEMPLATE_STILL_CAPTURE, surfaceList);
    }

    private CaptureRequest.Builder createPreviewBuilder(Surface surface) {
        List<Surface> previewSurfaceList = new ArrayList<>();
        previewSurfaceList.add(surface);
        return createBuilder(CameraDevice.TEMPLATE_PREVIEW, previewSurfaceList);
    }

    private CaptureRequest.Builder createBuilder(int templateType, List<Surface> surfaceList) {
        CaptureRequest.Builder captureBuilder = null;
        try {
             captureBuilder = mImageSession.onCreateRequestBuilder(templateType);
             for (Surface surface : surfaceList) {
                 captureBuilder.addTarget(surface);
             }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return captureBuilder;
    }

    private boolean isFlashOn() {
        return false;
    }

    @Override
    public Observable<EsResult> onSendRepeatingRequest(EsRequest request) {
        EsLog.d("onSendRepeatingRequest: req:" + request);
        // 需要进行添加
        int flashValue = request.getInt(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.NONE);
        if (flashValue != Es.FLASH_TYPE.NONE) {
            mRequestBuilderManager.getFlashRequest(getPreviewBuilder(), flashValue);
        }
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        request.put(Es.Key.SESSION_CALLBACK, mPreviewCallback);
        return mImageSession.onRepeatingRequest(request);
    }


    @Override
    public Observable<EsResult> close(EsRequest request) {
        return mCameraSessionManager.closeSession();
    }

    @Override
    public Observable<EsResult> capture(final EsRequest request) {
        EsLog.d("capture: ......3333...");
//        request.put(Es.Key.REQUEST_BUILDER, getCaptureBuilder(request));
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(final ObservableEmitter<EsResult> emitter) throws Exception {

                if (mCameraInfoManager.canTriggerAf() && isFlashOn()) {
                    triggerAFCaptureSequence();
                } else {
                    sendStillPictureRequest();
                }


//                mCaptureTime = SystemClock.elapsedRealtime();
                mPreviewCallback.setType(CaptureSessionCallback.STATE_CAPTURE);
                mPreviewCallback.setEmitter(emitter);
                mImageSession.capture(request);
            }
        });
    }

    private void triggerAFCaptureSequence() {
        CaptureRequest.Builder builder = getPreviewBuilder();
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
//        mState = STATE_WAITING_LOCK;
//        sendCaptureRequest(builder.build(), mPreviewCallback, mMainHandler);
    }

    private void sendStillPictureRequest() {
//        int jpegRotation = CameraUtil.getJpgRotation(characteristics, mDeviceRotation);
//        CaptureRequest.Builder builder = getCaptureBuilder(false, mImageReader.getSurface());
//        Integer aeFlash = getPreviewBuilder().get(CaptureRequest.CONTROL_AE_MODE);
//        Integer afMode = getPreviewBuilder().get(CaptureRequest.CONTROL_AF_MODE);
//        Integer flashMode = getPreviewBuilder().get(CaptureRequest.FLASH_MODE);
//        builder.set(CaptureRequest.CONTROL_AE_MODE, aeFlash);
//        builder.set(CaptureRequest.CONTROL_AF_MODE, afMode);
//        builder.set(CaptureRequest.FLASH_MODE, flashMode);
//        CaptureRequest request = mRequestMgr.getStillPictureRequest(
//                getCaptureBuilder(false, mImageReader.getSurface()), jpegRotation);
//        sendCaptureRequestWithStop(request, mCaptureCallback, mMainHandler);
    }

    private void triggerAECaptureSequence() {
        CaptureRequest.Builder builder = getPreviewBuilder();
        builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
//        mState = STATE_WAITING_PRE_CAPTURE;
//        sendCaptureRequest(builder.build(), mPreviewCallback, mMainHandler);
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

    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }
    };

    private CaptureSessionCallback mPreviewCallback = new CaptureSessionCallback();

    public class CaptureSessionCallback extends CameraCaptureSession.CaptureCallback {

        private static final int STATE_PREVIEW = 1;
        private static final int STATE_REPEAT = 2;
        private static final int STATE_CAPTURE = 3;
        private static final int STATE_WAITING_LOCK = 4;
        private static final int STATE_WAITING_PRE_CAPTURE = 5;

        private final Object mCaptureLock = new Object();

        private ObservableEmitter<EsResult> mEmitter;
        private int mState = 0;
        private int mAFState = -1;
        private boolean mFirstFrameCompleted = false;
        private boolean mCaptured = false;

        public void setType(int type) {
            this.mState = type;
            if (type == STATE_PREVIEW) {
                mFirstFrameCompleted = false;
            } else if (type == STATE_CAPTURE) {
                mCaptured = false;
            }
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
                if (mState != STATE_CAPTURE) return;

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
                EsLog.d("onCapture: mFlag:" + mState + "   readyCapture:" + readyCapture  + "   mCaptured"  + mCaptured);
                if (readyCapture && !mCaptured) {
                    mCaptured = true;
                    mState = 0;
                    mEmitter.onNext(new EsResult());
                }
            }
        }
    }

}
