package com.cfox.camera.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.utils.FxRequest;

public class VideoSessionHelper extends AbsBaseSessionHelper{

    public VideoSessionHelper() {

    }

    @Override
    public CaptureRequest.Builder createRequestBuilder(FxRequest fxRequest) throws CameraAccessException {
        return null;
    }
}
