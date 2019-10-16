package com.cfox.camera.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface ISessionHelper {

    CaptureRequest.Builder createRequestBuilder(FxRequest request) throws CameraAccessException;
    Observable<FxResult> createPreviewSession(FxRequest request);
    Observable<FxResult> sendRepeatingRequest(FxRequest request);
    void closeSession();

}
