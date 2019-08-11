package com.cfox.camera;

import android.content.Context;
import android.util.Log;

import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.utils.ThreadHandlerManager;

public class FxCamera implements IFxCamera , IFxCameraLifecycle{
    private static final String TAG = "FxCamera";
    private Context mContext;

    private FxCamera() {
    }

    private FxCamera setContext(Context context) {
        this.mContext = context.getApplicationContext();
        initCamera(context);
        return this;
    }

    public static FxCamera init(Context context){
        return Create.sFxCamera.setContext(context);
    }

    public static FxCamera getInstance() {
        Create.sFxCamera.checkContextUNLL();
        return Create.sFxCamera;
    }

    @Override
    public FxCameraLifecycle getLifecycle() {
        return new FxCameraLifecycle(this);
    }

    @Override
    public FxSurfaceManager getSurfaceManager() {
        return null;
    }

    @Override
    public void initCamera(Context context) {
        Log.d(TAG, "init camera .....");
        CameraInfoHelper.getInstance().load(context,
                ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_LOAD).getHandler());
    }

    @Override
    public void release() {
        ThreadHandlerManager.getInstance().release();
    }

    private static class Create {
        static FxCamera sFxCamera = new FxCamera();
}

    private void checkContextUNLL() {
        if (mContext == null) {
            throw new RuntimeException("FxCamera context is null , place use init set context !!!!!!!");
        }
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
}
