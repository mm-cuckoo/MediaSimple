package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.capture.impl.DulVideoCaptureImpl;
import com.cfox.camera.capture.impl.ImageCaptureImpl;
import com.cfox.camera.capture.impl.VideoCaptureImpl;
import com.cfox.camera.capture.BaseCapture;
import com.cfox.camera.mode.CameraModeManager;
import com.cfox.camera.mode.DulVideoMode;
import com.cfox.camera.mode.ImageMode;
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
    public BaseCapture photoModule() {
        ImageMode imageMode = mCameraModule.initModule(CameraModeManager.ModuleFlag.MODULE_PHOTO);
        return new ImageCaptureImpl(imageMode, mConfigWrapper);
    }

    @Override
    public BaseCapture videoModule() {
        VideoMode videoMode = mCameraModule.initModule(CameraModeManager.ModuleFlag.MODULE_VIDEO);
        return new VideoCaptureImpl(videoMode);
    }

    @Override
    public BaseCapture dulVideoModule() {
        DulVideoMode dulVideoMode = mCameraModule.initModule(CameraModeManager.ModuleFlag.MODULE_DUL_VIDEO);
        return new DulVideoCaptureImpl(dulVideoMode);
    }
}
