package com.cfox.camera.sessionmanager.callback;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;

import androidx.annotation.NonNull;

import com.cfox.camera.camera.session.CameraSession;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.EsParams;

import io.reactivex.ObservableEmitter;

public class PreviewCaptureCallback extends CameraCaptureSession.CaptureCallback {

    private static final int STATE_PREVIEW                      = 1;
    private static final int STATE_CAPTURE                      = 2;
    private static final int STATE_WAITING_LOCK                 = 3;
    private static final int STATE_WAITING_PRE_CAPTURE          = 4;
    private static final int STATE_WAITING_NON_PRE_CAPTURE      = 5;

    private int mState = 0;
    private int mAFState = -1;
    private int mFlashState = -1;
    private boolean mFirstFrameCompleted = false;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraSession mCameraSession;
    private ObservableEmitter<EsParams> mEmitter;

    public void applyPreview(@NonNull CameraSession cameraSession,
                             @NonNull CaptureRequest.Builder previewBuilder,
                             @NonNull ObservableEmitter<EsParams> emitter) {
        this.mPreviewBuilder = previewBuilder;
        this.mCameraSession = cameraSession;
        this.mEmitter = emitter;
        mFirstFrameCompleted = false;
        stateChange(STATE_PREVIEW);
    }

    private void stateChange(int type) {
        EsLog.d("setType  mState:" + type);
        this.mState = type;

    }

    @Override
    public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                    @NonNull CaptureRequest request,
                                    @NonNull CaptureResult partialResult) {
        updateAFState(partialResult);
        updateFlashState(partialResult);
        processPreCapture(partialResult);
    }

    @Override
    public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                   @NonNull CaptureRequest request,
                                   @NonNull TotalCaptureResult result) {


        if (!mFirstFrameCompleted) {
            mFirstFrameCompleted = true;
            EsParams esParams = new EsParams();
            esParams.put(EsParams.Key.PREVIEW_FIRST_FRAME, EsParams.Value.OK);
            mEmitter.onNext(esParams);
            EsLog.d("preview first frame call back");
        }

        updateAFState(result);
        updateFlashState(result);
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
                    runCaptureAction();
                } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                        CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    EsLog.d("STATE_WAITING_LOCK===>aeState:" + aeState);
                    if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                        stateChange(STATE_CAPTURE);
                        runCaptureAction();
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
                    runCaptureAction();
                }
                break;
            }
        }
    }

    private void runCaptureAction() {
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.CAPTURE_STATE, EsParams.Value.CAPTURE_STATE.CAPTURE_START);
        mEmitter.onNext(esParams);
    }

    private void updateAFState(CaptureResult captureResult) {
        Integer afState = captureResult.get(CaptureResult.CONTROL_AF_STATE);
        if (afState != null && afState != mAFState) {
            mAFState = afState;
            EsParams esParams = new EsParams();
            esParams.put(EsParams.Key.AF_STATE, afState);
            mEmitter.onNext(esParams);
        }
    }

    private void updateFlashState(CaptureResult captureResult) {
        Integer flashState = captureResult.get(CaptureResult.FLASH_MODE);
        if (flashState != null && flashState != mFlashState) {
            mFlashState = flashState;
            EsParams esParams = new EsParams();
            esParams.put(EsParams.Key.FLASH_STATE, flashState);
            mEmitter.onNext(esParams);
        }
    }

    public void capture() {
        triggerAFCaptureSequence();
    }

    private void triggerAFCaptureSequence() {
        EsLog.d("triggerAFCaptureSequence===>");
        mPreviewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
        stateChange(STATE_WAITING_LOCK);
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.REQUEST_BUILDER, mPreviewBuilder);
        esParams.put(EsParams.Key.CAPTURE_CALLBACK, this);
        try {
            mCameraSession.capture(esParams);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void triggerAECaptureSequence() {
        EsLog.d("triggerAECaptureSequence===>");
        mPreviewBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
        stateChange(STATE_WAITING_PRE_CAPTURE);
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.REQUEST_BUILDER, mPreviewBuilder);
        esParams.put(EsParams.Key.CAPTURE_CALLBACK, this);
        try {
            mCameraSession.capture(esParams);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void resetPreviewState() {
        EsLog.d("resetTriggerState===>");
        stateChange(STATE_PREVIEW);
        mPreviewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
        mPreviewBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_IDLE);
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.REQUEST_BUILDER, mPreviewBuilder);
        esParams.put(EsParams.Key.CAPTURE_CALLBACK, this);
        try {
            mCameraSession.onRepeatingRequest(esParams).subscribe(new CameraObserver<EsParams>());
            mCameraSession.capture(esParams);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
