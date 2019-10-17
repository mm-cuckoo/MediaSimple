package com.cfox.camera.model.module;


import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface IModule {

    Observable<FxResult> onStartPreview(FxRequest request);
    Observable<FxResult> onOpenCamera(FxRequest request);
    Observable<FxResult> onStop();
    Observable<FxResult> onCameraConfig(FxRequest request);
    Observable<FxResult> onCapture(FxRequest request);

}
