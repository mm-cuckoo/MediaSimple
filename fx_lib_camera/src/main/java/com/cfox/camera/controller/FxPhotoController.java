package com.cfox.camera.controller;


import android.util.Log;

import com.cfox.camera.model.ICameraModule;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

public class FxPhotoController implements IPhotoController {
    private static final String TAG = "FxPhotoController";

    private ICameraModule mCameraModule;

    public FxPhotoController(ICameraModule cameraModule) {
        mCameraModule = cameraModule;
    }

    @Override
    public void onStartPreview(FxRequest request) {
        mCameraModule.startPreview(request).subscribe(new CameraObserver<FxResult>(){
            @Override
            public void onNext(FxResult fxResult) {
                Log.d(TAG, "onNext: .onStartPreview....");


            }
        });
    }

    @Override
    public void onCameraConfig(FxRequest request) {
        mCameraModule.sendCameraConfig(request).subscribe(new CameraObserver<FxResult>(){
            @Override
            public void onNext(FxResult fxResult) {
                Log.d(TAG, "onNext: .onCameraConfig....");

            }
        });
    }

    @Override
    public void onStop() {
        mCameraModule.stop().subscribe(new CameraObserver<FxResult>(){
            @Override
            public void onNext(FxResult fxResult) {
                Log.d(TAG, "onNext: .onStop....");

            }
        });
    }

    @Override
    public void onCapture(FxRequest request) {
        mCameraModule.capture(request).subscribe(new CameraObserver<FxResult>(){
            @Override
            public void onNext(FxResult fxResult) {
                Log.d(TAG, "onNext: .onCapture....");

            }
        });
    }
}
