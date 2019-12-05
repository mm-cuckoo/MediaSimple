package com.cfox.camera.controller;


import android.util.Log;
import android.util.Range;

import com.cfox.camera.model.ICameraModule;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

public class FxDulVideoController implements IDulVideoController {
    private static final String TAG = "FxPhotoController";

    private ICameraModule mCameraModule;

    public FxDulVideoController(ICameraModule cameraModule) {
        mCameraModule = cameraModule;
    }

    @Override
    public void onStartPreview(FxRequest request) {
        mCameraModule.startPreview(request).subscribe(new CameraObserver<FxResult>(){
            @Override
            public void onNext(FxResult fxResult) {
                Log.d(TAG, "onNext: .requestPreview....");

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
                Log.d(TAG, "onNext: .requestStop....");

            }
        });
    }

    @Override
    public Range<Integer> getEvRange() {
        return mCameraModule.getEvRange();
    }
}
