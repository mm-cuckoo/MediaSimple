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
    private IConfig mConfig;

    FxCameraManager(Context context) {
        mCameraModule = CameraModule.getInstance(context);
    }

    @Override
    public IPhotoController photo() {
        mCameraModule.initModule(CameraModule.ModuleFlag.MODULE_PHOTO, getConfig());
        return new FxPhotoController(mCameraModule);
    }

    @Override
    public IVideoController video() {
        mCameraModule.initModule(CameraModule.ModuleFlag.MODULE_VIDEO, getConfig());
        return new FxVideoController(mCameraModule);
    }

    void setConfig(IConfig config) {
        mConfig = config;
    }

    IConfig getConfig() {
        if (mConfig == null) {
            mConfig = new DefaultConfig();
        }
        return mConfig;
    }
}
