package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.utils.ThreadHandlerManager;

public class FxCameraImpl implements FxCamera {
    private FxCameraEngine mFxCameraEngine;

    private FxCameraImpl() {
        mFxCameraEngine = FxCameraEngineImpl.getInstance();
    }

    private FxCamera setContext(Context context) {
        mFxCameraEngine.setContext(context.getApplicationContext());
        initCamera();
        return this;
    }

    public static FxCamera init(Context context) {
        return Create.sFxCamera.setContext(context);
    }

    public static FxCamera getInstance() {
        Create.sFxCamera.mFxCameraEngine.checkContextUNLL();
        return Create.sFxCamera;
    }

    @Override
    public FxCameraLifecycle getLifecycle() {
        return mFxCameraEngine.getLifecycle();
    }

    @Override
    public FxSurfaceManager getSurfaceManager() {
        return mFxCameraEngine.getSurfaceManager();
    }

    @Override
    public FxCameraManager getCameraManager() {
        return mFxCameraEngine.getCameraManager();
    }

    @Override
    public void setSurfaceManager(FxSurfaceManager surfaceManager) {
        mFxCameraEngine.setSurfaceManager(surfaceManager);
    }

    private void initCamera() {
        mFxCameraEngine.init();
    }

    @Override
    public void release() {
        ThreadHandlerManager.getInstance().release();
    }

    private static class Create {
        static FxCameraImpl sFxCamera = new FxCameraImpl();
    }
}