package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.controller.FxPhotoController;
import com.cfox.camera.controller.FxVideoController;
import com.cfox.camera.controller.IPhotoController;
import com.cfox.camera.controller.IVideoController;
import com.cfox.camera.model.CameraModule;
import com.cfox.camera.model.ICameraModule;

public class FxCameraManager implements IFxCameraManager {

    private ICameraModule mCameraModule;
    private ConfigWrapper mConfigWrapper;

    FxCameraManager(Context context) {
        mConfigWrapper = new ConfigWrapper();
        mCameraModule = CameraModule.getInstance(context, mConfigWrapper);
    }

    @Override
    public IPhotoController photo() {
        mCameraModule.initModule(CameraModule.ModuleFlag.MODULE_PHOTO);
        return new FxPhotoController(mCameraModule);
    }

    @Override
    public IVideoController video() {
        mCameraModule.initModule(CameraModule.ModuleFlag.MODULE_VIDEO);
        return new FxVideoController(mCameraModule);
    }

    void setConfig(IConfig config) {
        mConfigWrapper.setConfig(config);
    }

    ConfigWrapper getConfigWrapper() {
        return mConfigWrapper;
    }
}
