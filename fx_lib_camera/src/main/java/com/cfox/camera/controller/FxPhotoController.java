package com.cfox.camera.controller;


import android.util.Log;

import com.cfox.camera.utils.FxRequest;

public class FxPhotoController implements IPhotoController {
    private static final String TAG = "FxPhotoController";

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
        Log.d(TAG, "onCapture: ");
        mCameraController.capture(request);
    }
}
