package com.cfox.camera;

import android.content.Context;

public interface IFxCamera {

    IFxCameraLifecycle getLifecycle();
    FxSurfaceManager getSurfaceManager();
    void initCamera(Context context);
    void release();

}
