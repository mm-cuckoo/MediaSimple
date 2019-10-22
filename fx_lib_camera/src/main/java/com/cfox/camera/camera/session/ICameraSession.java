package com.cfox.camera.camera.session;


import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface ICameraSession {
    Observable<FxResult> onCreatePreviewSession(FxRequest request);
    void closeSession();
}
