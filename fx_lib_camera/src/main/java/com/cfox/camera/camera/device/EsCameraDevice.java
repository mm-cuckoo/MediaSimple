package com.cfox.camera.camera.device;

import com.cfox.camera.EsParams;

import io.reactivex.Observable;

public interface EsCameraDevice {

    Observable<EsParams> openCameraDevice(EsParams esParam);

    Observable<EsParams> closeCameraDevice(String cameraId);

}
