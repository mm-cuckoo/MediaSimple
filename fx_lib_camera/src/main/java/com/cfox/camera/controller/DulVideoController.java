package com.cfox.camera.controller;


import android.util.Log;
import android.util.Range;

import com.cfox.camera.model.ICameraModule;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

public class DulVideoController extends CameraController {
    private static final String TAG = "DulVideoController";

    private ICameraModule mCameraModule;

    public DulVideoController(ICameraModule cameraModule) {
        mCameraModule = cameraModule;
    }

    @Override
    public void onStartPreview(EsRequest request) {
        mCameraModule.startPreview(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                Log.d(TAG, "onNext: .requestPreview....");

            }
        });
    }

    @Override
    public void onCameraConfig(EsRequest request) {
        mCameraModule.sendCameraConfig(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                Log.d(TAG, "onNext: .requestCameraConfig....");

            }
        });
    }

    @Override
    public void onStop(EsRequest request) {
        mCameraModule.stop(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                Log.d(TAG, "onNext: .requestStop....");

            }
        });
    }

    @Override
    public Range<Integer> getEvRange(EsRequest request) {
        return mCameraModule.getEvRange(request);
    }
}
