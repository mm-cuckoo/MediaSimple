package com.cfox.camera;

import com.cfox.camera.controller.FxPhotoController;
import com.cfox.camera.controller.FxVideoController;

public class FxCameraManagerImpl implements FxCameraManager, FxCameraConfig {
    @Override
    public FxPhotoController photo() {
        return null;
    }

    @Override
    public FxVideoController video() {
        return null;
    }

    @Override
    public void setSurfaceManager(FxSurfaceManager surfaceManager) {

    }
}
