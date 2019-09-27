package com.cfox.camera.model.module;


import com.cfox.camera.camera.IFxCameraDevice;
import com.cfox.camera.camera.IFxCameraSession;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public abstract class BaseModule implements IModule {

    private IFxCameraDevice mCameraDevice;
    private IFxCameraSession mCameraSession;

    public BaseModule(IFxCameraDevice mCameraDevice, IFxCameraSession mCameraSession) {
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

