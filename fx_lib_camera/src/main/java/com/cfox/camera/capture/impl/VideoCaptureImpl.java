package com.cfox.camera.capture.impl;

import android.util.Range;

import com.cfox.camera.capture.VideoCapture;
import com.cfox.camera.mode.VideoMode;
import com.cfox.camera.surface.SurfaceProvider;
import com.cfox.camera.utils.EsParams;

public class VideoCaptureImpl implements VideoCapture {
    private final VideoMode mVideoMode;

    public VideoCaptureImpl(VideoMode videoMode) {
        mVideoMode = videoMode;
    }

    @Override
    public void onStartPreview(EsParams esParams, SurfaceProvider surfaceProvider) {
        mVideoMode.requestPreview(esParams);
    }

    @Override
    public void onCameraConfig(EsParams esParams) {
        mVideoMode.requestCameraConfig(esParams);
    }

    @Override
    public void onStop(EsParams esParams) {
        mVideoMode.requestStop(esParams);
    }

    @Override
    public Range<Integer> getEvRange(EsParams esParams) {
        return null;
    }
}
