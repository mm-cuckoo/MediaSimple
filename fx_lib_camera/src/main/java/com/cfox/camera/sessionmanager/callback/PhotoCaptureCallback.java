package com.cfox.camera.sessionmanager.callback;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;

import androidx.annotation.NonNull;

import com.cfox.camera.camera.session.CameraSession;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.EsParams;

import io.reactivex.ObservableEmitter;

public class PhotoCaptureCallback extends CameraCaptureSession.CaptureCallback {

    private CaptureRequest.Builder mCaptureBuilder;
    private CameraSession mCameraSession;
    private ObservableEmitter<EsParams> mEmitter;

    public void prepareCapture(@NonNull CameraSession cameraSession,
                               @NonNull CaptureRequest.Builder captureBuilder,
                               @NonNull ObservableEmitter<EsParams> emitter) {
        this.mCaptureBuilder = captureBuilder;
        this.mCameraSession = cameraSession;
        this.mEmitter = emitter;
    }

    public void capture() {
        sendStillPictureRequest();
    }

    @Override
    public void onCaptureStarted(@NonNull CameraCaptureSession session,
                                 @NonNull CaptureRequest request, long timestamp, long frameNumber) {
        super.onCaptureStarted(session, request, timestamp, frameNumber);
        EsLog.d("onCaptureStarted====>");
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.CAPTURE_STATE, EsParams.Value.CAPTURE_STATE.CAPTURE_START);
        mEmitter.onNext(esParams);

    }

    @Override
    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull
            CaptureRequest request, @NonNull TotalCaptureResult result) {
        super.onCaptureCompleted(session, request, result);
        EsLog.d("onCaptureCompleted====>");
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.CAPTURE_STATE, EsParams.Value.CAPTURE_STATE.CAPTURE_COMPLETED);
        mEmitter.onNext(esParams);
    }

    @Override
    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
        super.onCaptureFailed(session, request, failure);
        EsLog.d("onCaptureFailed====>");
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.CAPTURE_STATE, EsParams.Value.CAPTURE_STATE.CAPTURE_FAIL);
        mEmitter.onNext(esParams);
    }

    private void sendStillPictureRequest() {
        EsLog.d("sendStillPictureRequest===>");
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.REQUEST_BUILDER, mCaptureBuilder);
        esParams.put(EsParams.Key.CAPTURE_CALLBACK, this);
        try {
            mCameraSession.stopRepeating();
            mCameraSession.capture(esParams);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
