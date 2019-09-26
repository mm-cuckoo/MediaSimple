package com.cfox.camera.model.module;


import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface IModule {

    Observable<FxResult> onStartPreview(FxRequest request);
    Observable<FxResult> openCamera(FxRequest request);

}
