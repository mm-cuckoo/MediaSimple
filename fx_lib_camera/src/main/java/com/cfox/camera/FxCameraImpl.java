package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.utils.ThreadHandlerManager;

public class FxCameraImpl implements FxCamera {
    private static final String TAG = "FxCamera";
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
        // TODO: 19-8-11 check set context
        return Create.sFxCamera;
    }

    @Override
    public FxCameraLifecycle getLifecycle() {
        return null;
    }

    @Override
    public FxSurfaceManager getSurfaceManager() {
        return null;
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