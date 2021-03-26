package com.cfox.module_camera;


import android.content.Context;
import android.os.Environment;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.EsCameraManager;
import com.cfox.camera.capture.Capture;
import com.cfox.camera.capture.PhotoCapture;
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

    void backCamera(SurfaceProviderImpl helper) {

        EsParams esParams = getRequest();
        esParams.put(EsParams.Key.CAMERA_ID, EsParams.Value.CAMERA_ID.BACK);
        esParams.put(EsParams.Key.CAMERA_FLASH_TYPE, EsParams.Value.FLASH_TYPE.OFF);

        mCameraCapture.onStartPreview(esParams, helper);
    }

    void fontCamera(SurfaceProviderImpl helper) {
        EsParams esParams = getRequest();
        esParams.put(EsParams.Key.CAMERA_ID, EsParams.Value.CAMERA_ID.FONT);
        mCameraCapture.onStartPreview(esParams, helper);
    }


    void stopCamera() {
        EsParams esParams = new EsParams();
        mCameraCapture.onStop(esParams);
    }


    private EsParams getRequest() {
        EsParams esParams = new EsParams();
        Size previewSize = new Size(1920, 1080);
        esParams.put(EsParams.Key.PREVIEW_SIZE, previewSize);
        Size picSize = new Size(1920, 1080);
        esParams.put(EsParams.Key.PIC_SIZE, picSize);
        esParams.put(EsParams.Key.PIC_FILE_PATH, Environment.getExternalStorageDirectory().getAbsoluteFile().getPath());
        return esParams;
    }

    void torchFlash() {
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.CAMERA_FLASH_TYPE, EsParams.Value.FLASH_TYPE.TORCH);
        mCameraCapture.onCameraConfig(esParams);
    }

    void autoFlash() {
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.CAMERA_FLASH_TYPE, EsParams.Value.FLASH_TYPE.AUTO);
        mCameraCapture.onCameraConfig(esParams);
    }

    void onFlash() {
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.CAMERA_FLASH_TYPE, EsParams.Value.FLASH_TYPE.ON);
        mCameraCapture.onCameraConfig(esParams);
    }

    void closeFlash() {
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.CAMERA_FLASH_TYPE, EsParams.Value.FLASH_TYPE.OFF);
        mCameraCapture.onCameraConfig(esParams);
    }

    void setEv(int value) {
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.EV_SIZE, value);
        mCameraCapture.onCameraConfig(esParams);
    }
    void setFocus(float value) {
        EsParams esParams = new EsParams();
        mCameraCapture.onCameraConfig(esParams);
    }

    void setZoom(float value) {
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.ZOOM_VALUE, value);
        mCameraCapture.onCameraConfig(esParams);
    }

    void capture() {
        if (mCameraCapture instanceof PhotoCapture) {
            EsParams esParams = new EsParams();
            ((PhotoCapture) mCameraCapture).onCapture(esParams);

        }
    }

    Range<Integer> getEvRange() {
        return mCameraCapture.getEvRange();
    }
}
