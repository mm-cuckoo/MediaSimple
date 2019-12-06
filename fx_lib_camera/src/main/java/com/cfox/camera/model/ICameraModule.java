package com.cfox.camera.model;

import android.util.Range;

import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface ICameraModule {

    Observable<EsResult> startPreview(EsRequest request);

    void initModule(CameraModule.ModuleFlag moduleFlag);
    Observable<EsResult> sendCameraConfig(EsRequest request);
    Observable<EsResult> capture(EsRequest request);
    Observable<EsResult> stop(EsRequest request);
    Range<Integer> getEvRange(EsRequest request);
}
