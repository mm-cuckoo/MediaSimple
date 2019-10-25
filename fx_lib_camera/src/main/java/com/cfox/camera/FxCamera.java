package com.cfox.camera;

import android.annotation.SuppressLint;
import android.content.Context;

import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.utils.ThreadHandlerManager;

public class FxCamera {
    private Context mContext;
    private FxCameraManager mCameraManager;

    private FxCamera() {
    }

    private FxCamera attr(Context context) {
        mContext = context.getApplicationContext();
        mCameraManager = new FxCameraManager(mContext);
        initCamera();
        return this;
    }

    private void initCamera() {
        CameraInfoHelper.getInstance().load(mContext,
                ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_OTHER).getHandler());
    }

    public static FxCamera init(Context context) {
        return Create.sFxCamera.attr(context);
    }

    public static FxCamera getInstance() {
        Create.sFxCamera.checkContextUNLL();
        return Create.sFxCamera;
    }

    public FxCameraManager getCameraManager() {
        return mCameraManager;
    }

    public void setConfig(IConfig config) {
        checkContextUNLL();
        mCameraManager.setConfig(config);
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
        @SuppressLint("StaticFieldLeak")
        static FxCamera sFxCamera = new FxCamera();
    }
}