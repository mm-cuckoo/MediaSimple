package com.cfox.camera.controller;

import android.content.Context;

import com.cfox.camera.model.ICameraModule;
import com.cfox.camera.model.CameraModule;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

public abstract class AbsController implements IController {

    private ICameraModule mCameraModule;
    private ISurfaceHelper mSurfaceHelper;

    AbsController(Context context, ISurfaceHelper surfaceHelper) {
        mCameraModule = CameraModule.getInstance(context);
        mSurfaceHelper = surfaceHelper;
    }

    @Override
    public Observable<FxResult> startPreview(FxRequest request) {
        return Observable.combineLatest(mSurfaceHelper.isAvailable(), openCamera(request), new BiFunction<FxRequest, FxResult, FxResult>() {
            @Override
            public FxResult apply(FxRequest request, FxResult fxResult) throws Exception {
                return null;
            }
        });
    }

//    @Override
    private Observable<FxResult> openCamera(FxRequest request) {
        return mCameraModule.openCamera(request);
    }
}
