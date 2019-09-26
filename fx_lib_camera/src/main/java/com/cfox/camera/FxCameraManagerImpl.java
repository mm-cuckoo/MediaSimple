package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.controller.Controller;
import com.cfox.camera.controller.FxPhotoController;
import com.cfox.camera.controller.FxVideoController;

public class FxCameraManagerImpl implements FxCameraManager, FxCameraConfig {

    private Context mContext;
    private FxSurfaceManager mSurfaceManager;

    public FxCameraManagerImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public Controller photo() {
        return new FxPhotoController(mContext);
    }

    @Override
    public Controller video() {
        return new FxVideoController(mContext);
    }

    @Override
    public void setSurfaceManager(FxSurfaceManager surfaceManager) {
        mSurfaceManager = surfaceManager;
    }
}
