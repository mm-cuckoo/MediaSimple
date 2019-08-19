package com.cfox.camera.camera;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface FxCameraDevice {

    Observable<FxResult> openCameraDevice(FxRequest fxRequest);
}
