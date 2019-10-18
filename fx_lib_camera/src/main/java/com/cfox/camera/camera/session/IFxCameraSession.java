package com.cfox.camera.camera.session;


import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface IFxCameraSession {

    Observable<FxResult> createPreviewSession(FxRequest request);
    Observable<FxResult> sendRepeatingRequest(FxRequest request);
    Observable<FxResult> capture(FxRequest request);
    Observable<FxResult> captureStillPicture(FxRequest request);
    void closeSession();
}
