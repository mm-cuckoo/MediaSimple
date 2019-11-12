package com.cfox.camera.camera.session;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface ICameraSession {
    Observable<FxResult> onCreatePreviewSession(FxRequest request);
    Observable<FxResult> onPreviewRepeatingRequest(FxRequest request);
    Observable<FxResult> onOpenCamera(FxRequest request);
    CaptureRequest.Builder onCreateCaptureRequest(int templateType) throws CameraAccessException;
    Observable<FxResult> onClose();
    IBuilderPack getBuilderPack();
    int createPreviewTemplate();
    boolean isAutoFocusSupported();
    boolean isRawSupported();
    boolean isLegacyLocked();
}
