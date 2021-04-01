package com.cfox.camera.mode;


import com.cfox.camera.EsParams;

import io.reactivex.Observable;

public interface IMode {

    void init();

    Observable<EsParams> requestPreview(EsParams esParams);

    Observable<EsParams> requestStop(EsParams esParams);

    Observable<EsParams> requestCameraRepeating(EsParams esParams);

}
