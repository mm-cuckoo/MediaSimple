package com.cfox.camera.capture.impl;

import android.util.Range;

import com.cfox.camera.EsParams;
import com.cfox.camera.request.PreviewRequest;
import com.cfox.camera.capture.PreviewStateListener;
import com.cfox.camera.capture.VideoCapture;
import com.cfox.camera.mode.VideoMode;
import com.cfox.camera.request.RepeatRequest;

import io.reactivex.annotations.NonNull;

public class VideoCaptureImpl implements VideoCapture {
    private final VideoMode mVideoMode;

    public VideoCaptureImpl(VideoMode videoMode) {
        mVideoMode = videoMode;
    }

    @Override
    public void onStartPreview(@NonNull PreviewRequest request, PreviewStateListener listener) {

    }

    @Override
    public void onCameraRepeating(@NonNull RepeatRequest request) {

    }

    @Override
    public void onStop() {
        EsParams esParams = new EsParams();
        mVideoMode.requestStop(esParams);
    }

    @Override
    public Range<Integer> getEvRange() {
        return null;
    }

    @Override
    public Range<Float> getFocusRange() {
        return null;
    }
}
