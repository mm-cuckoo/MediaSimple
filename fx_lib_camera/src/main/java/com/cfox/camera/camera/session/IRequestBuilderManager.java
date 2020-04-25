package com.cfox.camera.camera.session;

import android.hardware.camera2.CaptureRequest;

public interface IRequestBuilderManager {

    void getPreviewRequest(CaptureRequest.Builder builder);

    void applyFlashRequest(CaptureRequest.Builder builder, int value);

    void getFlashRequest(CaptureRequest.Builder builder, int value);

}
