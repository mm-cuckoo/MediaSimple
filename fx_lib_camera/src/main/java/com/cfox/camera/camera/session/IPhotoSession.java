package com.cfox.camera.camera.session;

import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface IPhotoSession extends ICameraSession {
    Observable<EsResult> onSendRepeatingRequest(EsRequest request);
    Observable<EsResult> onCapture(EsRequest request);
    Observable<EsResult> onCaptureStillPicture(EsRequest request);
    int createStillCaptureTemplate();
}
