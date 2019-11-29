package com.cfox.camera.controller;

import android.util.Range;

import com.cfox.camera.model.ICameraModule;
import com.cfox.camera.utils.FxRequest;

public class FxVideoController implements IVideoController {
    private ICameraModule mCameraModule;

    public FxVideoController(ICameraModule cameraModule) {
        mCameraModule = cameraModule;
    }

    @Override
    public void onStartPreview(FxRequest request) {
        mCameraModule.startPreview(request);
    }

    @Override
    public void onCameraConfig(FxRequest request) {
        mCameraModule.sendCameraConfig(request);
    }

    @Override
    public void onStop() {
        mCameraModule.stop();
    }

    @Override
    public Range<Integer> getEvRange() {
        return mCameraModule.getEvRange();
    }
}
