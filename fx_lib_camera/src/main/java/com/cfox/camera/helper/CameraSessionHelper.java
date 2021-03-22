package com.cfox.camera.helper;

import android.hardware.camera2.CameraAccessException;

import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface CameraSessionHelper {

    void init();

    Observable<EsResult> onOpenCamera(EsRequest request);

    Observable<EsResult> onCreatePreviewSession(EsRequest request) throws CameraAccessException;

    Observable<EsResult> onRepeatingRequest(EsRequest request);

    Observable<EsResult> onPreviewRepeatingRequest(EsRequest request) throws CameraAccessException;

    Observable<EsResult> close(EsRequest request);

}
