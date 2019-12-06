package com.cfox.camera.controller;


import android.util.Log;
import android.util.Range;

import com.cfox.camera.log.EsLog;
import com.cfox.camera.model.ICameraModule;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

public class PhotoController extends CameraController {
    private static final String TAG = "PhotoController";

    private ICameraModule mCameraModule;

    public PhotoController(ICameraModule cameraModule) {
        mCameraModule = cameraModule;
    }

    @Override
    public void onStartPreview(EsRequest request) {
        mCameraModule.startPreview(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                EsLog.d("onNext: .requestPreview....");


            }
        });
    }

    @Override
    public void onCameraConfig(EsRequest request) {
        mCameraModule.sendCameraConfig(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                EsLog.d("onNext: .requestCameraConfig....");

            }
        });
    }

    @Override
    public void onStop(EsRequest request) {
        mCameraModule.stop(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                EsLog.d("onNext: .requestStop....");

            }
        });
    }

    @Override
    public Range<Integer> getEvRange(EsRequest request) {
        return mCameraModule.getEvRange(request);
    }

    @Override
    public void onCapture(EsRequest request) {
        mCameraModule.capture(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                EsLog.d("onNext: .requestCapture....");

            }
        });
    }
}
