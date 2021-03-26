package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.capture.impl.PhotoCaptureImpl;
import com.cfox.camera.capture.impl.VideoCaptureImpl;
import com.cfox.camera.capture.Capture;
import com.cfox.camera.mode.CameraModeManager;
import com.cfox.camera.mode.PhotoMode;
import com.cfox.camera.mode.VideoMode;

public class EsCameraManager implements IEsCameraManager {

    private final CameraModeManager mCameraModule;
    private final ConfigWrapper mConfigWrapper;

    public EsCameraManager(Context context) {
        this(context, null);
    }

    public EsCameraManager(Context context, IConfig config) {
        mConfigWrapper = new ConfigWrapper(config);
        mCameraModule = CameraModeManager.getInstance(context.getApplicationContext());
    }

    @Override
    public Capture photoModule() {
        PhotoMode photoMode = mCameraModule.initModule(CameraModeManager.ModuleFlag.MODULE_PHOTO);
        return new PhotoCaptureImpl(photoMode, mConfigWrapper);
    }

    @Override
    public Capture videoModule() {
        VideoMode videoMode = mCameraModule.initModule(CameraModeManager.ModuleFlag.MODULE_VIDEO);
        return new VideoCaptureImpl(videoMode);
    }
}
