package com.cfox.camera.camera;

import android.hardware.camera2.CameraDevice;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface IFxCameraSession {

    void setCameraDevice(CameraDevice cameraDevice);

    Observable<FxResult> createPreviewSession(FxRequest fxRequest);
    Observable<FxResult> sendRepeatingRequest(FxRequest fxRequest);
}
