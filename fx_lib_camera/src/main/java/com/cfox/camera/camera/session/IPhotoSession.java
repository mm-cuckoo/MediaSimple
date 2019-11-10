package com.cfox.camera.camera.session;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface IPhotoSession extends ICameraSession{
    Observable<FxResult> onSendRepeatingRequest(FxRequest request);
    Observable<FxResult> onCapture(FxRequest request);
    Observable<FxResult> onCaptureStillPicture(FxRequest request);
    int createStillCaptureTemplate();
}
