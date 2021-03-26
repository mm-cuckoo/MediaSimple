package com.cfox.camera.sessionmanager.callback;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;

import androidx.annotation.NonNull;

import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.EsParams;

import io.reactivex.subjects.PublishSubject;

public class PhotoCaptureCallback extends CameraCaptureSession.CaptureCallback {



    private final PublishSubject<EsParams> CAPTURE_STATUS = PublishSubject.create();

    private CaptureRequest.Builder mCaptureBuilder;
    private DeviceSession mDeviceSession;


    public PublishSubject<EsParams> getCaptureStateSubject() {
        return CAPTURE_STATUS;
    }

    public void prepareCapture(DeviceSession deviceSession, CaptureRequest.Builder captureBuilder) {
        this.mCaptureBuilder = captureBuilder;
        this.mDeviceSession = deviceSession;
    }

    public void capture() {
        if (mDeviceSession != null && mCaptureBuilder != null) {
            sendStillPictureRequest();
        }
    }


    @Override
    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull
            CaptureRequest request, @NonNull TotalCaptureResult result) {
        super.onCaptureCompleted(session, request, result);
        EsLog.d("onCaptureCompleted====>");
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.CAPTURE_STATE, EsParams.Value.CAPTURE_STATE.CAPTURE_COMPLETED);
        CAPTURE_STATUS.onNext(esParams);
    }

    @Override
    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
        super.onCaptureFailed(session, request, failure);
        EsLog.d("onCaptureFailed====>");
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.CAPTURE_STATE, EsParams.Value.CAPTURE_STATE.CAPTURE_FAIL);
        CAPTURE_STATUS.onNext(esParams);
    }

    private void sendStillPictureRequest() {
        EsLog.d("sendStillPictureRequest===>11");
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.REQUEST_BUILDER, mCaptureBuilder);
        esParams.put(EsParams.Key.CAPTURE_CALLBACK, this);
        try {
            mDeviceSession.stopRepeating();
            mDeviceSession.capture(esParams);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
