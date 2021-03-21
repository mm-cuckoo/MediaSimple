package com.cfox.camera.capture;

import android.util.Range;

import com.cfox.camera.utils.EsRequest;

public interface Capture {

    void onStartPreview(EsRequest request);

    void onCameraConfig(EsRequest request);

    void onStop(EsRequest request);

    Range<Integer> getEvRange(EsRequest request);
}
