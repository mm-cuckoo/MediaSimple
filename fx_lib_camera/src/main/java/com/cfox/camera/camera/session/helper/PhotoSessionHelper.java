package com.cfox.camera.camera.session.helper;


import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface PhotoSessionHelper extends CameraSessionHelper {

    Observable<EsResult> capture(EsRequest request);

    Observable<EsResult> captureStillPicture(EsRequest request);

}
