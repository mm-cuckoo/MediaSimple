package com.cfox.camera.capture.impl;

import android.util.Range;

import com.cfox.camera.capture.VideoCapture;
import com.cfox.camera.mode.VideoMode;
import com.cfox.camera.utils.EsRequest;

public class VideoCaptureImpl implements VideoCapture {
    private final VideoMode mVideoMode;

    public VideoCaptureImpl(VideoMode videoMode) {
        mVideoMode = videoMode;
    }

    @Override
    public void onStartPreview(EsRequest request) {
        mVideoMode.requestPreview(request);
    }

    @Override
    public void onCameraConfig(EsRequest request) {
        mVideoMode.requestCameraConfig(request);
    }

    @Override
    public void onStop(EsRequest request) {
        mVideoMode.requestStop(request);
    }

    @Override
    public Range<Integer> getEvRange(EsRequest request) {
        return null;
    }
}
