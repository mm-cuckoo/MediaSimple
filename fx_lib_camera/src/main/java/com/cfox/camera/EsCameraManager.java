package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.controller.DulVideoController;
import com.cfox.camera.controller.PhotoController;
import com.cfox.camera.controller.VideoController;
import com.cfox.camera.controller.ICameraController;
import com.cfox.camera.model.CameraModule;
import com.cfox.camera.model.ICameraModule;

public class EsCameraManager implements IEsCameraManager {

    private ICameraModule mCameraModule;
    private ConfigWrapper mConfigWrapper;

    EsCameraManager(Context context) {
        mConfigWrapper = new ConfigWrapper();
        mCameraModule = CameraModule.getInstance(context, mConfigWrapper);
    }

    @Override
    public ICameraController photoModule() {
        mCameraModule.initModule(CameraModule.ModuleFlag.MODULE_PHOTO);
        return new PhotoController(mCameraModule);
    }

    @Override
    public ICameraController videoModule() {
        mCameraModule.initModule(CameraModule.ModuleFlag.MODULE_VIDEO);
        return new VideoController(mCameraModule);
    }

    @Override
    public ICameraController dulVideoModule() {
        mCameraModule.initModule(CameraModule.ModuleFlag.MODULE_DUL_VIDEO);
        return new DulVideoController(mCameraModule);
    }

    void setConfig(IConfig config) {
        mConfigWrapper.setConfig(config);
    }
}
