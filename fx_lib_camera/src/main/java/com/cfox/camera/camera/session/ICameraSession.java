package com.cfox.camera.camera.session;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface ICameraSession {
    Observable<EsResult> onCreatePreviewSession(EsRequest request);
//    Observable<FxResult> onPreviewRepeatingRequest(FxRequest request);
    Observable<EsResult> onOpenCamera(EsRequest request);
    CaptureRequest.Builder onCreateRequestBuilder(int templateType) throws CameraAccessException;
    Observable<EsResult> onRepeatingRequest(EsRequest request);
    Observable<EsResult> onClose();
    void capture(EsRequest request, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException;
    void stopRepeating() throws CameraAccessException;
//    IBuilderPack getBuilderPack();
//    int createPreviewTemplate();
//    boolean isAutoFocusSupported();
//    boolean isRawSupported();
//    boolean isLegacyLocked();
//    Range<Integer> getEvRange();
}
