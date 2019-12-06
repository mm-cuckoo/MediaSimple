package com.cfox.camera.controller;

import android.util.Range;

import com.cfox.camera.utils.EsRequest;

public interface ICameraController {

    void onStartPreview(EsRequest request);
    void onCameraConfig(EsRequest request);
    void onCapture(EsRequest request);
    void onStop(EsRequest request);

    Range<Integer> getEvRange(EsRequest request);
}
