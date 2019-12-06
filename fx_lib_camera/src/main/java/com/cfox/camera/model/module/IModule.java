package com.cfox.camera.model.module;


import android.util.Range;

import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface IModule {

    Observable<EsResult> requestPreview(EsRequest request);
    Observable<EsResult> requestStop(EsRequest request);
    Observable<EsResult> requestCameraConfig(EsRequest request);
    Observable<EsResult> requestCapture(EsRequest request);
    Range<Integer> getEvRange(EsRequest request);

}
