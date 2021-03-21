package com.cfox.module_camera;


import android.content.Context;
import android.hardware.camera2.CaptureRequest;
import android.os.Environment;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.EsCameraManager;
import com.cfox.camera.capture.Capture;
import com.cfox.camera.capture.PhotoCapture;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;

class EsyCameraController {
    private final EsCameraManager mFxCameraManager;
    private Capture mCameraCapture;

    public EsyCameraController(Context context) {
        mFxCameraManager = new EsCameraManager(context);
        mCameraCapture = mFxCameraManager.photoModule();
    }

    void photoModule() {
        mCameraCapture = mFxCameraManager.photoModule();
    }

    void videoModule() {
        mCameraCapture = mFxCameraManager.videoModule();
    }


    void dulVideoModule() {
        mCameraCapture = mFxCameraManager.dulVideoModule();
    }

    void backCamera(SurfaceHelper helper) {

        EsRequest request = getRequest();
        request.put(Es.Key.CAMERA_ID, Es.Camera.ID.BACK.id);
        request.put(Es.Key.SURFACE_HELPER, helper);
        request.put(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.OFF);

        mCameraCapture.onStartPreview(request);
    }

    void fontCamera(SurfaceHelper helper) {
        EsRequest request = getRequest();
        request.put(Es.Key.CAMERA_ID, Es.Camera.ID.FONT.id);
        request.put(Es.Key.SURFACE_HELPER, helper);
        mCameraCapture.onStartPreview(request);
    }


    void stopCamera() {
        EsRequest request = new EsRequest();
        mCameraCapture.onStop(request);
    }


    private EsRequest getRequest() {
        EsRequest request = new EsRequest();
        Size previewSize = new Size(1920, 1080);
        request.put(Es.Key.PREVIEW_SIZE, previewSize);
        Size picSize = new Size(1920, 1080);
        request.put(Es.Key.PIC_SIZE, picSize);
        request.put(Es.Key.PIC_FILE_PATH, Environment.getExternalStorageDirectory().getAbsoluteFile().getPath());
        return request;
    }

//    void openFlash() {
//        CameraConfig cameraConfig = CameraConfig.getInstance();
//        cameraConfig.push(CaptureRequest.FLASH_MODE, FxRe.FLASH_TYPE.ON);
//        FxRequest request = new FxRequest();
//        request.put(FxRe.Key.CAMERA_CONFIG, cameraConfig);
//        mCameraController.requestCameraConfig(request);
//    }


    void torchFlash() {
        EsRequest request = new EsRequest();
        request.put(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.ON);
        mCameraCapture.onCameraConfig(request);
    }

//    void autoFlash() {
//        CameraConfig cameraConfig = CameraConfig.getInstance();
//        cameraConfig.push(CaptureRequest.FLASH_MODE, FxRe.FLASH_TYPE.AUTO);
//        FxRequest request = new FxRequest();
//        request.put(FxRe.Key.CAMERA_CONFIG, cameraConfig);
//        mCameraController.requestCameraConfig(request);
//    }

    void closeFlash() {
        EsRequest request = new EsRequest();
        request.put(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.OFF);
        mCameraCapture.onCameraConfig(request);
    }

    void setEv(int value) {
        EsRequest request = new EsRequest();
        mCameraCapture.onCameraConfig(request);
    }

    void capture() {
        if (mCameraCapture instanceof PhotoCapture) {
            EsRequest request = new EsRequest();
            ((PhotoCapture) mCameraCapture).onCapture(request);

        }
    }

    Range<Integer> getEvRange() {
        EsRequest request = new EsRequest();
        return mCameraCapture.getEvRange(request);
    }// 重新设计代码
}
