package com.cfox.module_camera;

import android.content.Context;
import android.util.Log;

import com.cfox.camera.camera.FxCameraDevice;
import com.cfox.camera.camera.FxCameraDeviceImpl;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.FxReq;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CameraController {
    private static final String TAG = "CameraController";
    private Context mContext;
    private FxCameraDevice mCameraDevice;
    public CameraController(Context context) {
        mContext = context.getApplicationContext();
        mCameraDevice = FxCameraDeviceImpl.getsInstance(mContext);
    }

    public void openPreview(SurfaceHelper helper) {

        FxRequest request = new FxRequest();
        request.put(FxReq.Key.CAMERA_ID, FxReq.Camera.ID.BACK.id);
        mCameraDevice.openCameraDevice(request).subscribe(new Observer<FxResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
            }

            @Override
            public void onNext(FxResult fxResult) {
                Log.d(TAG, "onNext: ");

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e);

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");

            }
        });

    }
}
