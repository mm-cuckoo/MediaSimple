package com.cfox.module_camera;


import android.hardware.camera2.CaptureRequest;
import android.os.Environment;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.CameraConfig;
import com.cfox.camera.EsCamera;
import com.cfox.camera.EsCameraManager;
import com.cfox.camera.controller.ICameraController;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;

class EsyCameraController {
    private EsCameraManager mFxCameraManager;
    private ICameraController mCameraController;

    public EsyCameraController() {
        mFxCameraManager = EsCamera.getInstance().getCameraManager();
        mCameraController = mFxCameraManager.photoModule();
    }

    void photoModule() {
        mCameraController = mFxCameraManager.photoModule();
    }

    void videoModule() {
        mCameraController = mFxCameraManager.videoModule();
    }


    void dulVideoModule() {
        mCameraController = mFxCameraManager.dulVideoModule();
    }

    void backCamera(SurfaceHelper helper) {

        EsRequest request = getRequest();
        request.put(Es.Key.CAMERA_ID, Es.Camera.ID.BACK.id);
        request.put(Es.Key.SURFACE_HELPER, helper);
        CameraConfig cameraConfig = CameraConfig.getInstance();
        cameraConfig.push(CaptureRequest.FLASH_MODE, Es.FLASH_TYPE.OFF);
        request.put(Es.Key.CAMERA_CONFIG, cameraConfig);

        mCameraController.onStartPreview(request);
    }

    void fontCamera(SurfaceHelper helper) {
        EsRequest request = getRequest();
        request.put(Es.Key.CAMERA_ID, Es.Camera.ID.FONT.id);
        request.put(Es.Key.SURFACE_HELPER, helper);
        mCameraController.onStartPreview(request);
    }


    void stopCamera() {
        EsRequest request = new EsRequest();
        mCameraController.onStop(request);
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
        CameraConfig cameraConfig = CameraConfig.getInstance();
        cameraConfig.push(CaptureRequest.FLASH_MODE, Es.FLASH_TYPE.TORCH);
        EsRequest request = new EsRequest();
        request.put(Es.Key.CAMERA_CONFIG, cameraConfig);
        mCameraController.onCameraConfig(request);
    }

//    void autoFlash() {
//        CameraConfig cameraConfig = CameraConfig.getInstance();
//        cameraConfig.push(CaptureRequest.FLASH_MODE, FxRe.FLASH_TYPE.AUTO);
//        FxRequest request = new FxRequest();
//        request.put(FxRe.Key.CAMERA_CONFIG, cameraConfig);
//        mCameraController.requestCameraConfig(request);
//    }

    void closeFlash() {
        CameraConfig cameraConfig = CameraConfig.getInstance();
        cameraConfig.push(CaptureRequest.FLASH_MODE, Es.FLASH_TYPE.OFF);
        EsRequest request = new EsRequest();
        request.put(Es.Key.CAMERA_CONFIG, cameraConfig);
        mCameraController.onCameraConfig(request);
    }

    void setEv(int value) {
        CameraConfig cameraConfig = CameraConfig.getInstance();
        cameraConfig.push(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, value);
        EsRequest request = new EsRequest();
        request.put(Es.Key.CAMERA_CONFIG, cameraConfig);
        mCameraController.onCameraConfig(request);
    }

    void capture() {
//        FxRequest request = new FxRequest();
//        mCameraController.onCapture(request);
    }

    Range<Integer> getEvRange() {
        EsRequest request = new EsRequest();
        return mCameraController.getEvRange(request);
    }
}
