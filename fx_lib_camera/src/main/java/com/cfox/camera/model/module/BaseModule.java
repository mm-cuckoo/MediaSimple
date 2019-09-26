package com.cfox.camera.model.module;


import com.cfox.camera.camera.FxCameraDevice;
import com.cfox.camera.camera.FxCameraSession;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public abstract class BaseModule implements IModule {

    private FxCameraDevice mCameraDevice;
    private FxCameraSession mCameraSession;

    public BaseModule(FxCameraDevice mCameraDevice, FxCameraSession mCameraSession) {
        this.mCameraDevice = mCameraDevice;
        this.mCameraSession = mCameraSession;
    }

    @Override
    public Observable<FxResult> onStartPreview(FxRequest request) {
        return null;
    }

    @Override
    public Observable<FxResult> openCamera(FxRequest request) {
        return null;
    }
}

