package com.cfox.camera.model;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface ICameraModule {

//    Observable<FxResult> onOpenCamera(FxRequest request);
    Observable<FxResult> startPreview(FxRequest request);

    void initModule(CameraModule.ModuleFlag moduleFlag);
    Observable<FxResult> onStop();
}
