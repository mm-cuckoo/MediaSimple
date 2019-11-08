package com.cfox.camera.camera.session;

import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.utils.FxRequest;

public interface IBuilderPack {
    void clear();
    void configToBuilder(FxRequest request, CaptureRequest.Builder builder);
    void configToBuilder(FxRequest request, CaptureRequest.Builder builder, boolean isSave);
    void appendPreviewBuilder(CaptureRequest.Builder builder);

    void configBuilder(FxRequest request);
    void previewBuilder(CaptureRequest.Builder builder);
    void startCaptureBuilder(CaptureRequest.Builder builder);
    void captureBuilder(CaptureRequest.Builder builder);
    void previewCaptureBuilder(CaptureRequest.Builder builder);


}
