package com.cfox.camera.controller;

import com.cfox.camera.utils.FxRequest;

public class FxVideoController implements IVideoController {
    private ICameraController mCameraController;

    public FxVideoController(ICameraController cameraController) {
        mCameraController = cameraController;
    }

    @Override
    public void onStartPreview(FxRequest request) {
        mCameraController.startPreview(request);
    }

    @Override
    public void onCameraConfig(FxRequest request) {
        mCameraController.cameraConfig(request);
    }

    @Override
    public void onStop() {
        mCameraController.stop();
    }
}
