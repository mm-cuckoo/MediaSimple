package com.cfox.camera.mode;



import com.cfox.camera.EsParams;

import io.reactivex.Observable;

public interface PhotoMode extends IMode {
    Observable<EsParams> requestCapture(EsParams esParams);
}
