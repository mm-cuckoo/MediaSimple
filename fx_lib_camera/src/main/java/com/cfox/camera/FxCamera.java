package com.cfox.camera;

public interface FxCamera {

    FxCameraLifecycle getLifecycle();
    FxSurfaceManager getSurfaceManager();
    FxCameraManager getCameraManager();
    void release();

}
