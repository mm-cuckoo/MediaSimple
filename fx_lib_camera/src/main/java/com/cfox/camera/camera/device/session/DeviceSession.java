package com.cfox.camera.camera.device.session;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.utils.EsParams;

import io.reactivex.Observable;

public interface DeviceSession {

    Observable<EsParams> onOpenCamera(EsParams esParams);

    Observable<EsParams> onCreateCaptureSession(EsParams esParams);

    CaptureRequest.Builder onCreateRequestBuilder(int templateType) throws CameraAccessException;

    Observable<EsParams> onRepeatingRequest(EsParams esParams);

    Observable<EsParams> onClose();

    void capture(EsParams esParams) throws CameraAccessException;

    void stopRepeating() throws CameraAccessException;

}
