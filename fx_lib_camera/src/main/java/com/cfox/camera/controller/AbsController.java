package com.cfox.camera.controller;

import android.content.Context;
import android.util.Log;

import com.cfox.camera.model.ICameraModule;
import com.cfox.camera.model.CameraModule;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class AbsController implements IController {
    private static final String TAG = "AbsController";

    private ICameraModule mCameraModule;

    AbsController(Context context, CameraModule.ModuleFlag moduleFlag) {
        mCameraModule = CameraModule.getInstance(context);
        mCameraModule.initModule(moduleFlag);
    }

    @Override
    public void startPreview(FxRequest request) {
        mCameraModule.startPreview(request).subscribe(new Observer<FxResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ......");
            }

            @Override
            public void onNext(FxResult fxResult) {
                String status = fxResult.getString(FxRe.Key.OPEN_CAMERA_STATUS);
                Log.d(TAG, "onNext: ,,,,,,,," + status);

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ........." + e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: .......");
            }
        });
    }

    @Override
    public void stop() {
        mCameraModule.onStop().subscribe(new Observer<FxResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: stop   ");

            }

            @Override
            public void onNext(FxResult fxResult) {
                Log.d(TAG, "onNext: stop");

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ,,,,, stop");

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ,,,,,, stop");

            }
        });
    }
}
