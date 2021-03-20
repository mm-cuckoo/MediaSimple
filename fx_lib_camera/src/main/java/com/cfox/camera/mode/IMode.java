package com.cfox.camera.mode;


import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface IMode {

    void init();

    Observable<EsResult> requestPreview(EsRequest request);

    Observable<EsResult> requestStop(EsRequest request);

    Observable<EsResult> requestCameraConfig(EsRequest request);

}
