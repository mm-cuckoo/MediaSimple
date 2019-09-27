package com.cfox.module_camera;

import android.content.Context;
import android.util.Log;

import com.cfox.camera.FxCamera;
import com.cfox.camera.FxCameraManager;
import com.cfox.camera.camera.IFxCameraDevice;
import com.cfox.camera.camera.FxCameraDevice;
import com.cfox.camera.controller.IController;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.IFxReq;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CameraController {
    private static final String TAG = "CameraController";
    private Context mContext;
    private FxCameraManager mFxCameraManager;
    private IController mCameraController;
    public CameraController(Context context) {
        mContext = context.getApplicationContext();
    }

    public void openPreview(SurfaceHelper helper) {

        FxRequest request = new FxRequest();
        request.put(IFxReq.Key.CAMERA_ID, IFxReq.Camera.ID.BACK.id);

        mFxCameraManager = FxCamera.getInstance().getCameraManager();
        mFxCameraManager.setSurfaceHelper(helper);
        mCameraController = mFxCameraManager.photo();
        mCameraController.startPreview(request).subscribe(new Observer<FxResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(FxResult fxResult) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });




//        mCameraDevice.openCameraDevice(request).subscribe(new Observer<FxResult>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "onSubscribe: ");
//            }
//
//            @Override
//            public void onNext(FxResult fxResult) {
//                Log.d(TAG, "onNext: ");
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "onError: " + e);
//
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete: ");
//
//            }
//        });

//        Observable.combineLatest(mCameraDevice.openCameraDevice(request), helper.isAvailable(), new BiFunction<FxResult, FxRequest, Object>() {
//            @Override
//            public Object apply(FxResult fxResult, FxRequest request) throws Exception {
//                return null;
//            }
//        }).subscribe(new Observer<Object>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "onSubscribe: .......");
//
//            }
//
//            @Override
//            public void onNext(Object fxResult) {
//                Log.d(TAG, "onNext: .......");
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "onError: ,,,,,,,");
//
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete: ,........");
//
//            }
//        });

    }
}
