package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.controller.IController;
import com.cfox.camera.controller.FxPhotoController;
import com.cfox.camera.controller.FxVideoController;
import com.cfox.camera.surface.ISurfaceHelper;

public class FxCameraManager implements IFxCameraManager {

    private Context mContext;
    private ISurfaceHelper mSurfaceHelper;

    public FxCameraManager(Context context) {
        this.mContext = context;
    }

    @Override
    public IController photo() {
        return new FxPhotoController(mContext, mSurfaceHelper);
    }

    @Override
    public IController video() {
        return new FxVideoController(mContext, mSurfaceHelper);
    }

    @Override
    public void setSurfaceHelper(ISurfaceHelper surfaceHelper) {
        mSurfaceHelper = surfaceHelper;
    }
}
