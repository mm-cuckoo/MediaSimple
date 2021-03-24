package com.cfox.camera.mode;


import com.cfox.camera.utils.EsParams;

import io.reactivex.Observable;

public interface IMode {

    void init();

    Observable<EsParams> requestPreview(EsParams esParams);

    Observable<EsParams> requestStop(EsParams esParams);

    Observable<EsParams> requestCameraConfig(EsParams esParams);

}
