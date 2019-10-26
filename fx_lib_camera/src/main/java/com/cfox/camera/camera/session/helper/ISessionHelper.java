package com.cfox.camera.camera.session.helper;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface ISessionHelper {
    Observable<FxResult> createPreviewSession(FxRequest request);
    CaptureRequest.Builder createPreviewRepeatingBuilder(FxRequest request) throws CameraAccessException;
    Observable<FxResult> sendRepeatingRequest(FxRequest request);
    Observable<FxResult> sendPreviewRepeatingRequest(FxRequest request);

    void closeSession();

}
