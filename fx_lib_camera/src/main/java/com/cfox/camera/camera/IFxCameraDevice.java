package com.cfox.camera.camera;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface IFxCameraDevice {

    Observable<FxResult> openCameraDevice(FxRequest fxRequest);
    void closeCameraDevice();

}
