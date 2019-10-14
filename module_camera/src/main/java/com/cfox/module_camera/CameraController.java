package com.cfox.module_camera;


import com.cfox.camera.FxCamera;
import com.cfox.camera.FxCameraManager;
import com.cfox.camera.controller.IController;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;

class CameraController {
    private FxCameraManager mFxCameraManager;
    private IController mCameraController;

    void openPreview(SurfaceHelper helper) {

        FxRequest request = new FxRequest();
        request.put(FxRe.Key.CAMERA_ID, FxRe.Camera.ID.BACK.id);
        request.put(FxRe.Key.SURFACE_HELPER, helper);

        mFxCameraManager = FxCamera.getInstance().getCameraManager();
        mCameraController = mFxCameraManager.photo();
        mCameraController.startPreview(request);

    }

    void switchCamera(SurfaceHelper helper) {
        FxRequest request = new FxRequest();
        request.put(FxRe.Key.CAMERA_ID, FxRe.Camera.ID.FONT.id);
        request.put(FxRe.Key.SURFACE_HELPER, helper);

        mFxCameraManager = FxCamera.getInstance().getCameraManager();
        mCameraController.startPreview(request);
    }


    void stopCamera() {
        mCameraController.stop();
    }
}
