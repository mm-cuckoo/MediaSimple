package com.cfox.camera.capture;

import android.util.Range;

import com.cfox.camera.request.PreviewRequest;
import com.cfox.camera.request.RepeatRequest;
import com.cfox.camera.surface.SurfaceProvider;
import com.cfox.camera.utils.EsParams;

public interface Capture {

    void onStartPreview(EsParams esParams, SurfaceProvider surfaceProvider);

    void onStartPreview(PreviewRequest request, PreviewStateListener listener);

    void onCameraRepeating(EsParams esParams);

    void onCameraRepeating(RepeatRequest request);

    void onStop();

    Range<Integer> getEvRange();

    Range<Float> getFocusRange();
}
