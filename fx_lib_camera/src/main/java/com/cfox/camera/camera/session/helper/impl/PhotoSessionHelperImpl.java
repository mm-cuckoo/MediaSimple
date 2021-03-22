package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
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
    private CaptureRequest.Builder mCaptureBuilder;

    private DeviceSession mPhotoSession;
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
        mPhotoSession = null;
        mPreviewBuilder = null;
        mCaptureBuilder = null;
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

    private CaptureRequest.Builder getCaptureBuilder() {
        if (mCaptureBuilder == null) {
            mCaptureBuilder = createCaptureBuilder(mSurfaceHelper.getCaptureSurfaces());
        }
        return mCaptureBuilder;
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
             captureBuilder = mPhotoSession.onCreateRequestBuilder(templateType);
             EsLog.d("surface size: ||||||||||||||---->" + surfaceList.size());
             for (Surface surface : surfaceList) {
                 captureBuilder.addTarget(surface);
             }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return captureBuilder;
    }

    private boolean isFlashOn() {
        return true;
    }

    @Override
    public Observable<EsResult> onRepeatingRequest(EsRequest request) {
        EsLog.d("onSendRepeatingRequest: req:" + request);
        // 需要进行添加
        int flashValue = request.getInt(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.NONE);
        if (flashValue != Es.FLASH_TYPE.NONE) {
            mRequestBuilderManager.getFlashRequest(getPreviewBuilder(), flashValue);
        }
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        request.put(Es.Key.SESSION_CALLBACK, mPreviewCallback);
        return mPhotoSession.onRepeatingRequest(request);
    }


    @Override
    public Observable<EsResult> close(EsRequest request) {
        return mCameraSessionManager.closeSession();
    }

    private int picRotation = 0;
    @Override
    public Observable<EsResult> capture(final EsRequest request) {
        picRotation = request.getInt(Es.Key.PIC_ORIENTATION, -1);
        EsLog.d("capture: ......3333..."  + picRotation);
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(final ObservableEmitter<EsResult> emitter) throws Exception {
                if (mCameraInfoManager.canTriggerAf() && isFlashOn()) {
                    triggerAFCaptureSequence();
                } else {
                    sendStillPictureRequest();
                }
            }
        });
    }

    private void triggerAFCaptureSequence() throws CameraAccessException {
        EsLog.d("triggerAFCaptureSequence===>");
        CaptureRequest.Builder builder = getPreviewBuilder();
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
        mPreviewCallback.setType(CaptureSessionCallback.STATE_WAITING_LOCK);
        EsRequest request = new EsRequest();
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        request.put(Es.Key.SESSION_CALLBACK, mPreviewCallback);
        mPhotoSession.capture(request);
    }

    private void sendStillPictureRequest() {
        EsLog.d("sendStillPictureRequest===>11");
        CaptureRequest.Builder builder = getCaptureBuilder();
        Integer aeFlash = getPreviewBuilder().get(CaptureRequest.CONTROL_AE_MODE);
        Integer afMode = getPreviewBuilder().get(CaptureRequest.CONTROL_AF_MODE);
        Integer flashMode = getPreviewBuilder().get(CaptureRequest.FLASH_MODE);
        builder.set(CaptureRequest.CONTROL_AE_MODE, aeFlash);
        builder.set(CaptureRequest.CONTROL_AF_MODE, afMode);
        builder.set(CaptureRequest.FLASH_MODE, flashMode);
        builder.set(CaptureRequest.JPEG_ORIENTATION, picRotation);
        EsRequest request = new EsRequest();
        request.put(Es.Key.REQUEST_BUILDER, builder);
        request.put(Es.Key.CAPTURE_CALLBACK, mCaptureCallback);
        try {
            mPhotoSession.stopRepeating();
            mPhotoSession.capture(request);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void triggerAECaptureSequence() {
        EsLog.d("triggerAECaptureSequence===>");
        CaptureRequest.Builder builder = getPreviewBuilder();
        builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
        mPreviewCallback.setType(CaptureSessionCallback.STATE_WAITING_PRE_CAPTURE);
        EsRequest request = new EsRequest();
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        request.put(Es.Key.SESSION_CALLBACK, mPreviewCallback);
        try {
            mPhotoSession.capture(request);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void resetTriggerState() {
        EsLog.d("resetTriggerState===>");
        mPreviewCallback.setType(CaptureSessionCallback.STATE_PREVIEW);
        CaptureRequest.Builder builder = getPreviewBuilder();
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
        builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_IDLE);
        EsRequest request = new EsRequest();
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        request.put(Es.Key.SESSION_CALLBACK, mPreviewCallback);
        try {
            mPhotoSession.onRepeatingRequest(request).subscribe();
            mPhotoSession.capture(request);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Observable<EsResult> captureStillPicture(final EsRequest request) {
        EsLog.d("captureStillPicture: .......");
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(final ObservableEmitter<EsResult> emitter) throws Exception {
                mPhotoSession.stopRepeating();
                mPhotoSession.capture(request);
            }
        });
    }

    @Override
    public DeviceSession getCameraSession(EsRequest request) {
        if (mPhotoSession == null) {
            mPhotoSession = mCameraSessionManager.createSession();
        }
        return mPhotoSession;
    }

    private final CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull
                CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            EsLog.d("onCaptureCompleted====>");
            resetTriggerState();
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            EsLog.d("onCaptureFailed====>");
            resetTriggerState();
        }
    };

    private final CaptureSessionCallback mPreviewCallback = new CaptureSessionCallback();

    public class CaptureSessionCallback extends CameraCaptureSession.CaptureCallback {

        private static final int STATE_PREVIEW = 1;
        private static final int STATE_REPEAT = 2;
        private static final int STATE_CAPTURE = 3;
        private static final int STATE_WAITING_LOCK = 4;
        private static final int STATE_WAITING_PRE_CAPTURE = 5;
        private static final int STATE_WAITING_NON_PRE_CAPTURE = 6;

        private final Object mCaptureLock = new Object();

        private ObservableEmitter<EsResult> mEmitter;
        private int mState = 0;
        private int mAFState = -1;
        private boolean mFirstFrameCompleted = false;

        public void setType(int type) {
            EsLog.d("setType  mState:" + type);
            this.mState = type;
            if (type == STATE_PREVIEW) {
                mFirstFrameCompleted = false;
            } else if (type == STATE_CAPTURE) {
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
            processPreCapture(partialResult);
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
            processPreCapture(result);
        }

        private void processPreCapture(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    EsLog.d("STATE_WAITING_LOCK===>afState:" + afState);

                    if (afState == null) {
                        sendStillPictureRequest();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        EsLog.d("STATE_WAITING_LOCK===>aeState:" + aeState);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            setType(STATE_CAPTURE);
                            sendStillPictureRequest();
                        } else {
                            triggerAECaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRE_CAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    EsLog.d("STATE_WAITING_LOCK===>aeState:" + aeState);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        setType(STATE_WAITING_NON_PRE_CAPTURE);
                    }
                    break;
                }
                case STATE_WAITING_NON_PRE_CAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    EsLog.d("STATE_WAITING_LOCK===>aeState:" + aeState);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        setType(STATE_CAPTURE);
                        sendStillPictureRequest();
                    }
                    break;
                }
            }
        }

        private void updateAFState(CaptureResult captureResult) {
            Integer afState = captureResult.get(CaptureResult.CONTROL_AF_STATE);
            if (afState != null && afState != mAFState) {
                mAFState = afState;
                EsResult result = new EsResult();
                result.put(Es.Key.AF_CHANGE_STATE, afState);
                mEmitter.onNext(result);
            }

        }
    }

}
