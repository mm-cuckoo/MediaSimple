package com.cfox.camera.camera;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface IFxCameraSession {

    Observable<FxResult> createPreviewSession(FxRequest request);
    Observable<FxResult> sendRepeatingRequest(FxRequest request);
    void closeSession();
}
