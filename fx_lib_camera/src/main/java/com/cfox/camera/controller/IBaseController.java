package com.cfox.camera.controller;

import com.cfox.camera.utils.FxRequest;

public interface IBaseController {

    void onStartPreview(FxRequest request);
    void onCameraConfig(FxRequest request);
    void onStop();

}
