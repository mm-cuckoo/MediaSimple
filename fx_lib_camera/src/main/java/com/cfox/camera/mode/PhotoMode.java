package com.cfox.camera.mode;



import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface PhotoMode extends IMode {
    Observable<EsResult> requestCapture(EsRequest request);
}
