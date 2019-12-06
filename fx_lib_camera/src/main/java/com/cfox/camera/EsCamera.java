package com.cfox.camera;

import android.annotation.SuppressLint;
import android.content.Context;

import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.utils.ThreadHandlerManager;

public class EsCamera {
    private Context mContext;
    private EsCameraManager mCameraManager;

    private EsCamera() {
    }

    private EsCamera attr(Context context) {
        mContext = context.getApplicationContext();
        mCameraManager = new EsCameraManager(mContext);
        initCamera();
        return this;
    }

    private void initCamera() {
        CameraInfoHelper.getInstance().load(mContext,
                ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_OTHER).getHandler());
    }

    public static EsCamera init(Context context) {
        return Create.sEsCamera.attr(context);
    }

    public static EsCamera getInstance() {
        Create.sEsCamera.checkContextUNLL();
        return Create.sEsCamera;
    }

    public EsCameraManager getCameraManager() {
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
            throw new RuntimeException("Context is null , " +
                    "place use EsCamera init(Context context) method set context !!!!!!!");
        }
    }

    private static class Create {
        @SuppressLint("StaticFieldLeak")
        static EsCamera sEsCamera = new EsCamera();
    }
}