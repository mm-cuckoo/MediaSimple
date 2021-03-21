package com.cfox.camera.camera.session.helper;

import android.hardware.camera2.CameraAccessException;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface CameraSessionHelper {

    void init();

    Observable<EsResult> onOpenCamera(EsRequest request);

    Observable<EsResult> onCreatePreviewSession(EsRequest request) throws CameraAccessException;

    Observable<EsResult> onSendRepeatingRequest(EsRequest request);

    Observable<EsResult> onSendPreviewRepeatingRequest(EsRequest request) throws CameraAccessException;

    Observable<EsResult> close(EsRequest request);

}
