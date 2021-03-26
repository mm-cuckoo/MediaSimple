package com.cfox.camera.sessionmanager;

import android.hardware.camera2.CameraAccessException;

import com.cfox.camera.utils.EsParams;

import io.reactivex.Observable;

public interface SessionManager {

    void init();

    Observable<EsParams> captureStatus();

    Observable<EsParams> previewStatus();

    Observable<EsParams> onOpenCamera(EsParams esParams);

    Observable<EsParams> onCreatePreviewSession(EsParams esParams) throws CameraAccessException;

    Observable<EsParams> onRepeatingRequest(EsParams esParams);

    Observable<EsParams> onPreviewRepeatingRequest(EsParams esParams) throws CameraAccessException;

    Observable<EsParams> close(EsParams esParams);

}
