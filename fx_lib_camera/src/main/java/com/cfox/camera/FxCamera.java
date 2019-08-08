package com.cfox.camera;

import android.content.Context;

public class FxCamera implements IFxCamera{

    private Context mContext;

    private FxCamera() {
    }

    private FxCamera setContext(Context context) {
        this.mContext = context.getApplicationContext();
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
    public CameraLifecycle getLifecycle() {
        return new CameraLifecycle();
    }

    @Override
    public void release() {

    }

    private static class Create {
        static FxCamera sFxCamera = new FxCamera();
    }

    private void checkContextUNLL() {
        if (mContext == null) {
            throw new RuntimeException("FxCamera context is null , place use init set context !!!!!!!");
        }
    }
}
