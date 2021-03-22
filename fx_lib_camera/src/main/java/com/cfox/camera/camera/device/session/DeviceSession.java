package com.cfox.camera.camera.device.session;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface DeviceSession {

    Observable<EsResult> onOpenCamera(EsRequest request);

    Observable<EsResult> onCreateCaptureSession(EsRequest request);

    CaptureRequest.Builder onCreateRequestBuilder(int templateType) throws CameraAccessException;

    Observable<EsResult> onRepeatingRequest(EsRequest request);

    Observable<EsResult> onClose();

    void capture(EsRequest request) throws CameraAccessException;

    void stopRepeating() throws CameraAccessException;

}
