package com.cfox.camera.controller;


import com.cfox.camera.utils.FxRequest;

public class FxPhotoController implements IPhotoController {

    private ICameraController mCameraController;

    public FxPhotoController(ICameraController cameraController) {
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

    @Override
    public void onCapture(FxRequest request) {
        mCameraController.capture(request);
    }
}
