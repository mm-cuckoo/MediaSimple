package com.cfox.module_camera;


import com.cfox.camera.FxCamera;
import com.cfox.camera.FxCameraManager;
import com.cfox.camera.controller.IController;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.FxReq;
import com.cfox.camera.utils.FxRequest;

public class CameraController {
    private FxCameraManager mFxCameraManager;
    private IController mCameraController;

    public void openPreview(SurfaceHelper helper) {

        FxRequest request = new FxRequest();
        request.put(FxReq.Key.CAMERA_ID, FxReq.Camera.ID.BACK.id);
        request.put(FxReq.Key.SURFACE_HELPER, helper);

        mFxCameraManager = FxCamera.getInstance().getCameraManager();
        mFxCameraManager.setSurfaceHelper(helper);
        mCameraController = mFxCameraManager.photo();
        mCameraController.startPreview(request);

    }
}
