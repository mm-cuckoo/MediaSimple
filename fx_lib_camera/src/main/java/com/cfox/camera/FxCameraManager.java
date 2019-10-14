package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.controller.IController;
import com.cfox.camera.controller.FxPhotoController;
import com.cfox.camera.controller.FxVideoController;

public class FxCameraManager implements IFxCameraManager {

    private Context mContext;

    public FxCameraManager(Context context) {
        this.mContext = context;
    }

    @Override
    public IController photo() {
        return new FxPhotoController(mContext);
    }

    @Override
    public IController video() {
        return new FxVideoController(mContext);
    }
}
