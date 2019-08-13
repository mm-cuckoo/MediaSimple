package com.cfox.camera;

import android.content.Context;

public interface FxCameraEngine {
    void setContext(Context context);
    FxCameraLifecycle getLifecycle();
    FxSurfaceManager getSurfaceManager();
    FxCameraManager getCameraManager();
    void checkContextUNLL();

    void init();
}
