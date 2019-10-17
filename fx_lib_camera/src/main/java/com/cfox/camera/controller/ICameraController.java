package com.cfox.camera.controller;

import com.cfox.camera.utils.FxRequest;


public interface ICameraController {
    void startPreview(FxRequest request);
    void cameraConfig(FxRequest request);
    void capture(FxRequest request);
    void stop();

}
