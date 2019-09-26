package com.cfox.camera.controller;

import android.content.Context;

import com.cfox.camera.model.CameraModule;
import com.cfox.camera.model.CameraModuleImpl;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public abstract class AbsController implements Controller {

    private CameraModule mCameraModule;

    public AbsController(Context context) {
        mCameraModule = CameraModuleImpl.getInstance(context);
    }

    @Override
    public Observable<FxResult> startPreview(FxRequest request) {
        return null;
    }

    @Override
    public Observable<FxResult> openCamera(FxRequest request) {
        return mCameraModule.openCamera(request);
    }
}
