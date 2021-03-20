package com.cfox.camera.capture.impl;


import android.util.Log;
import android.util.Range;

import com.cfox.camera.capture.DulVideoCapture;
import com.cfox.camera.mode.DulVideoMode;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

public class DulVideoCaptureImpl implements DulVideoCapture {
    private static final String TAG = "DulVideoController";

    private DulVideoMode mDulVideoMode;

    public DulVideoCaptureImpl(DulVideoMode dulVideoMode) {
        mDulVideoMode = dulVideoMode;
    }

    @Override
    public void onStartPreview(EsRequest request) {
        mDulVideoMode.requestPreview(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                Log.d(TAG, "onNext: .requestPreview....");

            }
        });
    }

    @Override
    public void onCameraConfig(EsRequest request) {
        mDulVideoMode.requestCameraConfig(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                Log.d(TAG, "onNext: .requestCameraConfig....");

            }
        });
    }

    @Override
    public void onStop(EsRequest request) {
        mDulVideoMode.requestStop(request).subscribe(new CameraObserver<EsResult>(){
            @Override
            public void onNext(EsResult fxResult) {
                Log.d(TAG, "onNext: .requestStop....");

            }
        });
    }

    @Override
    public Range<Integer> getEvRange(EsRequest request) {
        return null;
    }
}
