package com.cfox.camera.camera.session;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.EsParams;

import io.reactivex.Observable;

public interface CameraSession {

    Observable<EsParams> onOpenCamera(EsParams esParams);

    Observable<EsParams> onCreateCaptureSession(EsParams esParams);

    CaptureRequest.Builder onCreateRequestBuilder(int templateType) throws CameraAccessException;

    Observable<EsParams> onRepeatingRequest(EsParams esParams);

    Observable<EsParams> onClose();

    void capture(EsParams esParams) throws CameraAccessException;

    void stopRepeating() throws CameraAccessException;

}
