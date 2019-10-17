package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.controller.CameraController;
import com.cfox.camera.controller.FxPhotoController;
import com.cfox.camera.controller.FxVideoController;
import com.cfox.camera.controller.IPhotoController;
import com.cfox.camera.controller.IVideoController;
import com.cfox.camera.model.CameraModule;
import com.cfox.camera.model.ICameraModule;

public class FxCameraManager implements IFxCameraManager {

    private ICameraModule mCameraModule;

    FxCameraManager(Context context) {
        mCameraModule = CameraModule.getInstance(context);
    }

    @Override
    public IPhotoController photo() {
        mCameraModule.initModule(CameraModule.ModuleFlag.MODULE_PHOTO);
        return new FxPhotoController(new CameraController(mCameraModule));
    }

    @Override
    public IVideoController video() {
        mCameraModule.initModule( CameraModule.ModuleFlag.MODULE_VIDEO);
        return new FxVideoController(new CameraController(mCameraModule));
    }
}
