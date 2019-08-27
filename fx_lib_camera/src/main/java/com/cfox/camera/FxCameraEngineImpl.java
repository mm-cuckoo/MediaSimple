package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.utils.ThreadHandlerManager;

public class FxCameraEngineImpl implements FxCameraEngine, FxCameraLifecycle {
    private Context mContext;
    private FxSurfaceManager mSurfaceManager;
    private FxCameraManager mCameraManager;
    private FxCameraLifecycle mCameraLifecycle;

    private FxCameraEngineImpl() {
        mSurfaceManager = new FxSurfaceManagerImpl();
        mCameraManager = new FxCameraManagerImpl();
        mCameraLifecycle = new FxCameraLifecycleImpl(this);
    }

    static FxCameraEngine getInstance() {
        return Create.sFxCameraEngine;
    }

    @Override
    public void setContext(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public FxCameraLifecycle getLifecycle() {
        return mCameraLifecycle;
    }

    @Override
    public FxSurfaceManager getSurfaceManager() {
        return mSurfaceManager;
    }

    @Override
    public FxCameraManager getCameraManager() {
        return mCameraManager;
    }

    @Override
    public void setSurfaceManager(FxSurfaceManager surfaceManager) {
        ((FxCameraConfig)mCameraManager).setSurfaceManager(surfaceManager);
    }

    @Override
    public void init() {
        checkContextUNLL();
        CameraInfoHelper.getInstance().load(mContext,
                ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_LOAD).getHandler());
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    private static class Create {
        static FxCameraEngineImpl sFxCameraEngine = new FxCameraEngineImpl();
    }

    public void checkContextUNLL() {
        if (mContext == null) {
            throw new RuntimeException("FxCameraEngine context is null , " +
                    "place use FxCamera init(Context context) method set context !!!!!!!");
        }
    }
}
