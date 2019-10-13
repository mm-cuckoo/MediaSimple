package com.cfox.camera.model.module;


import android.hardware.camera2.CameraAccessException;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface IModule {

    Observable<FxResult> onStartPreview(FxRequest request) throws CameraAccessException;
    Observable<FxResult> openCamera(FxRequest request);

}
