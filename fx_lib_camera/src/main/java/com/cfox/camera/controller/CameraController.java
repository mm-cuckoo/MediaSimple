package com.cfox.camera.controller;

import android.util.Log;

import com.cfox.camera.model.ICameraModule;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CameraController implements ICameraController {
    private static final String TAG = "AbsController";

    private ICameraModule mCameraModule;

    public CameraController(ICameraModule cameraModule) {
        mCameraModule = cameraModule;
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
    public void cameraConfig(FxRequest request) {
        mCameraModule.sendCameraConfig(request).subscribe(new Observer<FxResult>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ....");

            }

            @Override
            public void onNext(FxResult fxResult) {
                Log.d(TAG, "onNext: ........");

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ......." + e);

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: .......");

            }
        });
    }

    @Override
    public void capture(FxRequest request) {

    }

    @Override
    public void stop() {
        mCameraModule.stop().subscribe(new Observer<FxResult>() {
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
