package com.cfox.camera.capture.impl;


import android.util.Log;
import android.util.Range;

import com.cfox.camera.capture.DulVideoCapture;
import com.cfox.camera.mode.DulVideoMode;
import com.cfox.camera.surface.SurfaceProvider;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.utils.EsParams;

public class DulVideoCaptureImpl implements DulVideoCapture {
    private static final String TAG = "DulVideoController";

    private DulVideoMode mDulVideoMode;

    public DulVideoCaptureImpl(DulVideoMode dulVideoMode) {
        mDulVideoMode = dulVideoMode;
    }


    @Override
    public void onStartPreview(EsParams esParams, SurfaceProvider surfaceProvider) {

    }

    @Override
    public void onCameraConfig(EsParams esParams) {
        mDulVideoMode.requestCameraConfig(esParams).subscribe(new CameraObserver<EsParams>(){
            @Override
            public void onNext(EsParams fxParam) {
                Log.d(TAG, "onNext: .requestCameraConfig....");

            }
        });
    }

    @Override
    public void onStop(EsParams esParams) {
        mDulVideoMode.requestStop(esParams).subscribe(new CameraObserver<EsParams>(){
            @Override
            public void onNext(EsParams fxParam) {
                Log.d(TAG, "onNext: .requestStop....");

            }
        });
    }

    @Override
    public Range<Integer> getEvRange(EsParams esParams) {
        return null;
    }
}
