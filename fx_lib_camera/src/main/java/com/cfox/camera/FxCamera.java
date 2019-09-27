package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.utils.ThreadHandlerManager;

public class FxCamera {
    private Context mContext;
    private FxCameraManager mFxCameraManager;

    private FxCamera() {
//        mFxCameraEngine = FxCameraEngineImpl.getInstance();
    }

    private FxCamera attr(Context context) {
        mFxCameraManager = new FxCameraManager(context);
        initCamera();
        return this;
    }

    private void initCamera() {
        CameraInfoHelper.getInstance().load(mContext,
                ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_LOAD).getHandler());
    }

    public static FxCamera init(Context context) {
        return Create.sFxCamera.attr(context);
    }

    public static FxCamera getInstance() {
        Create.sFxCamera.checkContextUNLL();
        return Create.sFxCamera;
    }

    public FxCameraManager getCameraManager() {
        return mFxCameraManager;
    }

    public void release() {
        ThreadHandlerManager.getInstance().release();
    }

    private void checkContextUNLL() {
        if (mContext == null) {
            throw new RuntimeException("FxCameraEngine context is null , " +
                    "place use FxCamera init(Context context) method set context !!!!!!!");
        }
    }

    private static class Create {
        static FxCamera sFxCamera = new FxCamera();
    }
}