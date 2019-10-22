package com.cfox.camera.camera.session;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public class VideoSessionHelper extends AbsSessionHelper {


    public VideoSessionHelper(IVideoSession videoSession) {
        super(videoSession);

    }

    @Override
    public CaptureRequest.Builder createRequestBuilder(FxRequest request) throws CameraAccessException {
        return null;
    }

    @Override
    public Observable<FxResult> sendRepeatingRequest(FxRequest request) {
        return null;
    }
}
