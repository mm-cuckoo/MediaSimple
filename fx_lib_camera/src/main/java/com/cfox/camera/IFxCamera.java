package com.cfox.camera;

import android.content.Context;

public interface IFxCamera {

    IFxCameraLifecycle getLifecycle();
    void initCamera(Context context);
    void release();

}
