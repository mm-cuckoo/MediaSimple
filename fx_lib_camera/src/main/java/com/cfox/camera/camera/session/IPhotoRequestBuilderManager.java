package com.cfox.camera.camera.session;

import android.hardware.camera2.CaptureRequest;

public interface IPhotoRequestBuilderManager extends IRequestBuilderManager {
    public void captureBuilder(CaptureRequest.Builder builder);
}
