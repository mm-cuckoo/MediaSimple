package com.cfox.module_camera;


import android.hardware.camera2.CaptureRequest;
import android.os.Environment;
import android.util.Size;

import com.cfox.camera.CameraConfig;
import com.cfox.camera.FxCamera;
import com.cfox.camera.FxCameraManager;
import com.cfox.camera.controller.IPhotoController;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;

class CameraController {
    private FxCameraManager mFxCameraManager;
    private IPhotoController mCameraController;

    public CameraController() {
        mFxCameraManager = FxCamera.getInstance().getCameraManager();
    }

    void backCamera(SurfaceHelper helper) {

        FxRequest request = getRequest();
        request.put(FxRe.Key.CAMERA_ID, FxRe.Camera.ID.BACK.id);
        request.put(FxRe.Key.SURFACE_HELPER, helper);
        CameraConfig cameraConfig = CameraConfig.getInstance();
        cameraConfig.push(CaptureRequest.FLASH_MODE, FxRe.FLASH_TYPE.CLOSE);
        request.put(FxRe.Key.CAMERA_CONFIG, cameraConfig);
        mCameraController = mFxCameraManager.photo();
        mCameraController.onStartPreview(request);
    }

    void fontCamera(SurfaceHelper helper) {
        FxRequest request = getRequest();
        request.put(FxRe.Key.CAMERA_ID, FxRe.Camera.ID.FONT.id);
        request.put(FxRe.Key.SURFACE_HELPER, helper);
        mCameraController.onStartPreview(request);
    }


    void stopCamera() {
        mCameraController.onStop();
    }


    private FxRequest getRequest() {
        FxRequest request = new FxRequest();
        Size previewSize = new Size(1920, 1080);
        request.put(FxRe.Key.PREVIEW_SIZE, previewSize);
        Size picSize = new Size(1920, 1080);
        request.put(FxRe.Key.PIC_SIZE, picSize);
        request.put(FxRe.Key.PIC_FILE_PATH, Environment.getExternalStorageDirectory().getAbsoluteFile().getPath());
        return request;
    }

    void openFlash() {
        CameraConfig cameraConfig = CameraConfig.getInstance();
        cameraConfig.push(CaptureRequest.FLASH_MODE, FxRe.FLASH_TYPE.OPEN);
        FxRequest request = new FxRequest();
        request.put(FxRe.Key.CAMERA_CONFIG, cameraConfig);
        mCameraController.onCameraConfig(request);
    }


    void torchFlash() {
        CameraConfig cameraConfig = CameraConfig.getInstance();
        cameraConfig.push(CaptureRequest.FLASH_MODE, FxRe.FLASH_TYPE.TORCH);
        FxRequest request = new FxRequest();
        request.put(FxRe.Key.CAMERA_CONFIG, cameraConfig);
        mCameraController.onCameraConfig(request);
    }

    void autoFlash() {
        CameraConfig cameraConfig = CameraConfig.getInstance();
        cameraConfig.push(CaptureRequest.FLASH_MODE, FxRe.FLASH_TYPE.AUTO);
        FxRequest request = new FxRequest();
        request.put(FxRe.Key.CAMERA_CONFIG, cameraConfig);
        mCameraController.onCameraConfig(request);
    }

    void closeFlash() {
        CameraConfig cameraConfig = CameraConfig.getInstance();
        cameraConfig.push(CaptureRequest.FLASH_MODE, FxRe.FLASH_TYPE.CLOSE);
        FxRequest request = new FxRequest();
        request.put(FxRe.Key.CAMERA_CONFIG, cameraConfig);
        mCameraController.onCameraConfig(request);
    }

    void capture() {
        FxRequest request = new FxRequest();
        mCameraController.onCapture(request);
    }
}
