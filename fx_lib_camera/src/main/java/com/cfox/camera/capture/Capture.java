package com.cfox.camera.capture;

import android.util.Range;

import androidx.annotation.NonNull;

import com.cfox.camera.request.PreviewRequest;
import com.cfox.camera.request.RepeatRequest;

public interface Capture {

    void onStartPreview(@NonNull PreviewRequest request, PreviewStateListener listener);

    void onCameraRepeating(@NonNull RepeatRequest request);

    void onStop();

    Range<Integer> getEvRange();

    Range<Float> getFocusRange();
}
