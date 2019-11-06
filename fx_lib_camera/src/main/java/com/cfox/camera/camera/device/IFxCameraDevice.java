package com.cfox.camera.camera.device;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface IFxCameraDevice {

    Observable<FxResult> openCameraDevice(FxRequest fxRequest);
    Observable<FxResult> closeCameraDevice(String cameraId);

}
