package com.cfox.camera.sessionmanager.impl;

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
import com.cfox.camera.camera.device.session.DeviceSessionManager;
import com.cfox.camera.sessionmanager.PhotoSessionManager;
import com.cfox.camera.sessionmanager.RequestBuilderManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.surface.SurfaceManager;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsParams;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.subjects.PublishSubject;

/**
 * 负责挣了并发送个camera session 动作
 */
public class PhotoSessionManagerImpl extends AbsSessionManager implements PhotoSessionManager {

    private CaptureRequest.Builder mPreviewBuilder;
    private CaptureRequest.Builder mCaptureBuilder;

    private DeviceSession mPhotoSession;
    private final RequestBuilderManager mRequestBuilderManager;
    private final CameraInfoManager mCameraInfoManager = CameraInfoManagerImpl.CAMERA_INFO_MANAGER;
    private final DeviceSessionManager mCameraSessionManager;
    private SurfaceManager mSurfaceManager;
    private final PublishSubject<EsParams> mCameraStatus = PublishSubject.create();

    public PhotoSessionManagerImpl(DeviceSessionManager sessionManager) {
        mCameraSessionManager = sessionManager;
        mRequestBuilderManager = new RequestBuilderManager(mCameraInfoManager);
    }

    @Override
    public void init() {

    }

    @Override
    public Observable<EsParams> cameraStatus() {
        return mCameraStatus;
    }

    @Override
    public DeviceSession getCameraSession(EsParams esParams) {
        if (mPhotoSession == null) {
            mPhotoSession = mCameraSessionManager.createSession();
        }
        return mPhotoSession;
    }


    @Override
    public void onBeforeOpenCamera(EsParams esParams) {
        mCameraSessionManager.closeSession().subscribe();
        mPhotoSession = null;
        mPreviewBuilder = null;
        mCaptureBuilder = null;
    }

    @Override
    Observable<EsParams> applyPreviewPlan(final EsParams esParams) {
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsParams> emitter) {
                mSurfaceManager = (SurfaceManager) esParams.getObj(Es.Key.SURFACE_MANAGER);
                mPreviewBuilder = getPreviewBuilder();
                int flashValue = esParams.getInt(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.OFF);
                mRequestBuilderManager.getFlashRequest(getPreviewBuilder(), flashValue);
                mRequestBuilderManager.applyPreviewRequest(getPreviewBuilder());
                esParams.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
                mPreviewCaptureCallback.stateChange(CaptureSessionCallback.STATE_PREVIEW);
                esParams.put(Es.Key.SESSION_CALLBACK, mPreviewCaptureCallback);
                emitter.onNext(esParams);
            }
        });
    }

    private CaptureRequest.Builder getPreviewBuilder() {
        if (mPreviewBuilder == null) {
            mPreviewBuilder = createPreviewBuilder(mSurfaceManager.getPreviewSurface());
        }
        return mPreviewBuilder;
    }

    private CaptureRequest.Builder getCaptureBuilder() {
        if (mCaptureBuilder == null) {
            mCaptureBuilder = createCaptureBuilder(mSurfaceManager.getReaderSurface());
        }
        return mCaptureBuilder;
    }

    private CaptureRequest.Builder createCaptureBuilder(List<Surface> surfaceList) {
        return createBuilder(CameraDevice.TEMPLATE_STILL_CAPTURE, surfaceList);
    }

    private boolean isFlashOn() {
        return true;
    }

    @Override
    public Observable<EsParams> onRepeatingRequest(EsParams esParams) {
        EsLog.d("onSendRepeatingRequest: req:" + esParams);
        // 需要进行添加
        int flashValue = esParams.getInt(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.NONE);
        mRequestBuilderManager.getFlashRequest(getPreviewBuilder(), flashValue);
        esParams.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        esParams.put(Es.Key.SESSION_CALLBACK, mPreviewCaptureCallback);
        return mPhotoSession.onRepeatingRequest(esParams);
    }


    @Override
    public Observable<EsParams> close(EsParams esParams) {
        return mCameraSessionManager.closeSession();
    }

    private int picRotation = 0;
    @Override
    public Observable<EsParams> capture(final EsParams esParams) {
        picRotation = esParams.getInt(Es.Key.PIC_ORIENTATION, -1);
        EsLog.d("capture: ......3333..."  + picRotation);
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(final ObservableEmitter<EsParams> emitter) throws Exception {
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
        mPreviewCaptureCallback.stateChange(CaptureSessionCallback.STATE_WAITING_LOCK);
        EsParams esParams = new EsParams();
        esParams.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        esParams.put(Es.Key.CAPTURE_CALLBACK, mPreviewCaptureCallback);
        mPhotoSession.capture(esParams);
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
        EsParams esParams = new EsParams();
        esParams.put(Es.Key.REQUEST_BUILDER, builder);
        esParams.put(Es.Key.CAPTURE_CALLBACK, mPhotoCaptureCallback);
        try {
            mPhotoSession.stopRepeating();
            mPhotoSession.capture(esParams);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void triggerAECaptureSequence() {
        EsLog.d("triggerAECaptureSequence===>");
        CaptureRequest.Builder builder = getPreviewBuilder();
        builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
        mPreviewCaptureCallback.stateChange(CaptureSessionCallback.STATE_WAITING_PRE_CAPTURE);
        EsParams esParams = new EsParams();
        esParams.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        esParams.put(Es.Key.SESSION_CALLBACK, mPreviewCaptureCallback);
        try {
            mPhotoSession.capture(esParams);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void resetTriggerState() {
        EsLog.d("resetTriggerState===>");
        mPreviewCaptureCallback.stateChange(CaptureSessionCallback.STATE_PREVIEW);
        CaptureRequest.Builder builder = getPreviewBuilder();
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
        builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_IDLE);
        EsParams esParams = new EsParams();
        esParams.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        esParams.put(Es.Key.SESSION_CALLBACK, mPreviewCaptureCallback);
        try {
            mPhotoSession.onRepeatingRequest(esParams).subscribe();
            mPhotoSession.capture(esParams);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private final CameraCaptureSession.CaptureCallback mPhotoCaptureCallback = new CameraCaptureSession.CaptureCallback() {

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

    private final CaptureSessionCallback mPreviewCaptureCallback = new CaptureSessionCallback();

    public class CaptureSessionCallback extends CameraCaptureSession.CaptureCallback {

        private static final int STATE_PREVIEW                      = 1;
        private static final int STATE_CAPTURE                      = 2;
        private static final int STATE_WAITING_LOCK                 = 3;
        private static final int STATE_WAITING_PRE_CAPTURE          = 4;
        private static final int STATE_WAITING_NON_PRE_CAPTURE      = 5;

        private int mState = 0;
        private int mAFState = -1;
        private boolean mFirstFrameCompleted = false;

        public void stateChange(int type) {
            EsLog.d("setType  mState:" + type);
            this.mState = type;
            if (type == STATE_PREVIEW) {
                mFirstFrameCompleted = false;
            }
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
                EsParams esParams = new EsParams();
                esParams.put(Es.Key.FIRST_FRAME_CALLBACK, Es.Value.OK);
                mCameraStatus.onNext(esParams);
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
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            stateChange(STATE_CAPTURE);
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
                        stateChange(STATE_WAITING_NON_PRE_CAPTURE);
                    }
                    break;
                }
                case STATE_WAITING_NON_PRE_CAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    EsLog.d("STATE_WAITING_LOCK===>aeState:" + aeState);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        stateChange(STATE_CAPTURE);
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
                EsParams esParams = new EsParams();
                esParams.put(Es.Key.AF_CHANGE_STATE, afState);
                mCameraStatus.onNext(esParams);
            }

        }
    }

}
