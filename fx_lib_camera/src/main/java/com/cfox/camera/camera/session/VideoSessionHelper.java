package com.cfox.camera.camera.session;

import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.utils.FxRequest;

public class VideoSessionHelper extends AbsBaseSessionHelper {

    public VideoSessionHelper(IFxCameraSession cameraSession) {
        super(cameraSession);

    }

    @Override
    public CaptureRequest.Builder createRequestBuilder(FxRequest fxRequest){
        return null;
    }
}
