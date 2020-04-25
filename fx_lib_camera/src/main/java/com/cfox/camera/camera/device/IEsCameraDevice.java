package com.cfox.camera.camera.device;

import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface IEsCameraDevice {

    Observable<EsResult> openCameraDevice(EsRequest fxRequest);

    Observable<EsResult> closeCameraDevice(String cameraId);

}
