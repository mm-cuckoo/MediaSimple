package com.cfox.camera.controller;

import android.util.Range;

import com.cfox.camera.model.ICameraModule;
import com.cfox.camera.utils.EsRequest;

public class VideoController extends CameraController {
    private ICameraModule mCameraModule;

    public VideoController(ICameraModule cameraModule) {
        mCameraModule = cameraModule;
    }

    @Override
    public void onStartPreview(EsRequest request) {
        mCameraModule.startPreview(request);
    }

    @Override
    public void onCameraConfig(EsRequest request) {
        mCameraModule.sendCameraConfig(request);
    }

    @Override
    public void onStop(EsRequest request) {
        mCameraModule.stop(request);
    }

    @Override
    public Range<Integer> getEvRange(EsRequest request) {
        return mCameraModule.getEvRange(request);
    }
}
