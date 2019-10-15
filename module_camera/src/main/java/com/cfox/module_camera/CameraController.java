package com.cfox.module_camera;


import com.cfox.camera.FxCamera;
import com.cfox.camera.FxCameraManager;
import com.cfox.camera.controller.IController;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

class CameraController {
    private FxCameraManager mFxCameraManager;
    private IController mCameraController;

    public CameraController() {
        mFxCameraManager = FxCamera.getInstance().getCameraManager();
    }

    void backCamera(SurfaceHelper helper) {

        FxRequest request = getRequest();
        request.put(FxRe.Key.CAMERA_ID, FxRe.Camera.ID.BACK.id);
        request.put(FxRe.Key.SURFACE_HELPER, helper);
        mCameraController = mFxCameraManager.photo();
        mCameraController.startPreview(request);

    }

    void fontCamera(SurfaceHelper helper) {
        FxRequest request = getRequest();
        request.put(FxRe.Key.CAMERA_ID, FxRe.Camera.ID.FONT.id);
        request.put(FxRe.Key.SURFACE_HELPER, helper);
        mCameraController.startPreview(request);
    }


    void stopCamera() {
        mCameraController.stop();
    }


    public FxRequest getRequest() {
        FxRequest request = new FxRequest();
        request.put(FxRe.Key.CAMERA_ID, FxRe.Camera.ID.BACK.id);
        request.put(FxRe.Key.PREVIEW_WIDTH, 1080);
        request.put(FxRe.Key.PREVIEW_HEIGHT, 1920);
        request.put(FxRe.Key.PIC_WIDTH, 1080);
        request.put(FxRe.Key.PIC_HEIGHT, 1920);
        return request;
    }
}
