package com.cfox.camera;

public interface FxCamera {

    FxCameraLifecycle getLifecycle();
    FxSurfaceManager getSurfaceManager();
    FxCameraManager getCameraManager();
    void setSurfaceManager(FxSurfaceManager surfaceManager);
    void release();

}
