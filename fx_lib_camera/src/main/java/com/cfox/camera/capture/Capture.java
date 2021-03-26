package com.cfox.camera.capture;

import android.util.Range;

import com.cfox.camera.surface.SurfaceProvider;
import com.cfox.camera.utils.EsParams;

public interface Capture {

    void onStartPreview(EsParams esParams, SurfaceProvider surfaceProvider);

    void onCameraConfig(EsParams esParams);

    void onStop(EsParams esParams);

    Range<Integer> getEvRange();

    Range<Float> getFocusRange();
}
