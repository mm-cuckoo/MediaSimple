package com.cfox.module_camera;


import android.content.Context;
import android.graphics.ImageFormat;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.EsCamera;
import com.cfox.camera.EsCameraManager;
import com.cfox.camera.request.FlashState;
import com.cfox.camera.request.PreviewRequest;
import com.cfox.camera.capture.Capture;
import com.cfox.camera.capture.PhotoCapture;
import com.cfox.camera.capture.PreviewStateListener;
import com.cfox.camera.request.RepeatRequest;
import com.cfox.module_camera.reader.CaptureImageReader;

class EsyCameraController {
    private final EsCameraManager mCameraManager;
    private Capture mCameraCapture;

    public EsyCameraController(Context context) {
        mCameraManager = EsCamera.createCameraManager(context);
        mCameraCapture = mCameraManager.photoModule();
    }

    void photoModule() {
        mCameraCapture = mCameraManager.photoModule();
    }

    void videoModule() {
        mCameraCapture = mCameraManager.videoModule();
    }

    void backCamera(SurfaceProviderImpl helper, PreviewStateListener listener) {

        PreviewRequest.Builder builder = getRequest();
        builder.openBackCamera()
                .setSurfaceProvider(helper);

        mCameraCapture.onStartPreview(builder.builder(),listener);
    }

    void fontCamera(SurfaceProviderImpl helper, PreviewStateListener listener) {
        PreviewRequest.Builder builder = getRequest();
        builder.openFontCamera()
                .setSurfaceProvider(helper);
        mCameraCapture.onStartPreview(builder.builder(), listener);
    }


    void stopCamera() {
        mCameraCapture.onStop();
    }


    private PreviewRequest.Builder getRequest() {

        Size previewSize = new Size(1920, 1080);
        Size picSize = new Size(1920, 1080);
        return PreviewRequest.createBuilder()
                .setPreviewSize(previewSize)
                .setPictureSize(picSize, ImageFormat.JPEG)
                .setFlash(FlashState.OFF)
//                .addImageReaderProvider(new PreviewImageReader())
                .addImageReaderProvider(new CaptureImageReader());
    }

    void torchFlash() {
        RepeatRequest.Builder builder = RepeatRequest.createBuilder();
//        builder.setFlash(EsParams.Value.FLASH_STATE.TORCH);
        mCameraCapture.onCameraRepeating(builder.builder());
    }

    void autoFlash() {
        RepeatRequest.Builder builder = RepeatRequest.createBuilder();
        builder.setFlash(FlashState.AUTO);
        mCameraCapture.onCameraRepeating(builder.builder());
    }

    void onFlash() {
        RepeatRequest.Builder builder = RepeatRequest.createBuilder();
        builder.setFlash(FlashState.ON);
        mCameraCapture.onCameraRepeating(builder.builder());
    }

    void closeFlash() {
        RepeatRequest.Builder builder = RepeatRequest.createBuilder();
        builder.setFlash(FlashState.OFF);
        mCameraCapture.onCameraRepeating(builder.builder());
    }

    void setEv(int value) {
        RepeatRequest.Builder builder = RepeatRequest.createBuilder();
        builder.setEv(value);
        mCameraCapture.onCameraRepeating(builder.builder());
    }
    void setFocus(float touchX, float touchY) {
        RepeatRequest.Builder builder = RepeatRequest.createBuilder();
        builder.setAfTouchXY(touchX, touchY);
        mCameraCapture.onCameraRepeating(builder.builder());
    }

    public void resetFocus() {
        RepeatRequest.Builder builder = RepeatRequest.createBuilder();
        builder.resetFocus();
        mCameraCapture.onCameraRepeating(builder.builder());
    }

    void setZoom(float value) {
        RepeatRequest.Builder builder = RepeatRequest.createBuilder();
        builder.setZoom(value);
        mCameraCapture.onCameraRepeating(builder.builder());
    }

    void capture() {
        if (mCameraCapture instanceof PhotoCapture) {
            ((PhotoCapture) mCameraCapture).onCapture(null);

        }
    }

    Range<Integer> getEvRange() {
        return mCameraCapture.getEvRange();
    }


}
