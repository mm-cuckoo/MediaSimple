package com.cfox.module_camera;


import android.content.Context;
import android.os.Environment;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.EsCameraManager;
import com.cfox.camera.capture.Capture;
import com.cfox.camera.capture.PhotoCapture;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsParams;

class EsyCameraController {
    private final EsCameraManager mCameraManager;
    private Capture mCameraCapture;

    public EsyCameraController(Context context) {
        mCameraManager = new EsCameraManager(context);
        mCameraCapture = mCameraManager.photoModule();
    }

    void photoModule() {
        mCameraCapture = mCameraManager.photoModule();
    }

    void videoModule() {
        mCameraCapture = mCameraManager.videoModule();
    }


    void dulVideoModule() {
        mCameraCapture = mCameraManager.dulVideoModule();
    }

    void backCamera(SurfaceProviderImpl helper) {

        EsParams esParams = getRequest();
        esParams.put(Es.Key.CAMERA_ID, Es.Camera.ID.BACK.id);
        esParams.put(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.OFF);

        mCameraCapture.onStartPreview(esParams, helper);
    }

    void fontCamera(SurfaceProviderImpl helper) {
        EsParams esParams = getRequest();
        esParams.put(Es.Key.CAMERA_ID, Es.Camera.ID.FONT.id);
        mCameraCapture.onStartPreview(esParams, helper);
    }


    void stopCamera() {
        EsParams esParams = new EsParams();
        mCameraCapture.onStop(esParams);
    }


    private EsParams getRequest() {
        EsParams esParams = new EsParams();
        Size previewSize = new Size(1920, 1080);
        esParams.put(Es.Key.PREVIEW_SIZE, previewSize);
        Size picSize = new Size(1920, 1080);
        esParams.put(Es.Key.PIC_SIZE, picSize);
        esParams.put(Es.Key.PIC_FILE_PATH, Environment.getExternalStorageDirectory().getAbsoluteFile().getPath());
        return esParams;
    }

//    void openFlash() {
//        CameraConfig cameraConfig = CameraConfig.getInstance();
//        cameraConfig.push(CaptureRequest.FLASH_MODE, FxRe.FLASH_TYPE.ON);
//        FxRequest request = new FxRequest();
//        request.put(FxRe.Key.CAMERA_CONFIG, cameraConfig);
//        mCameraController.requestCameraConfig(request);
//    }


    void torchFlash() {
        EsParams esParams = new EsParams();
        esParams.put(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.ON);
        mCameraCapture.onCameraConfig(esParams);
    }

//    void autoFlash() {
//        CameraConfig cameraConfig = CameraConfig.getInstance();
//        cameraConfig.push(CaptureRequest.FLASH_MODE, FxRe.FLASH_TYPE.AUTO);
//        FxRequest request = new FxRequest();
//        request.put(FxRe.Key.CAMERA_CONFIG, cameraConfig);
//        mCameraController.requestCameraConfig(request);
//    }

    void closeFlash() {
        EsParams esParams = new EsParams();
        esParams.put(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.OFF);
        mCameraCapture.onCameraConfig(esParams);
    }

    void setEv(int value) {
        EsParams esParams = new EsParams();
        mCameraCapture.onCameraConfig(esParams);
    }

    void capture() {
        if (mCameraCapture instanceof PhotoCapture) {
            EsParams esParams = new EsParams();
            ((PhotoCapture) mCameraCapture).onCapture(esParams);

        }
    }

    Range<Integer> getEvRange() {
        EsParams esParams = new EsParams();
        return mCameraCapture.getEvRange(esParams);
    }// 重新设计代码
}
