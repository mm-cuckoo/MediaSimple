package com.cfox.camera;

import android.content.Context;

public class FxCamera implements IFxCamera{

    private Context mContext;

    private FxCamera() {
    }

    private IFxCamera setContext(Context context) {
        this.mContext = context.getApplicationContext();
        return this;
    }

    public static IFxCamera init(Context context){
        return Create.sFxCamera.setContext(context);
    }

    public static IFxCamera getInstance() {
        Create.sFxCamera.checkContextUNLL();
        return Create.sFxCamera;
    }

    @Override
    public IFxCameraLifecycle getLifecycle() {
        return new CameraLifecycle();
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
