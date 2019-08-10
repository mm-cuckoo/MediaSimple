package com.cfox.camera;

import android.content.Context;

public class FxCameraEngine implements IFxCameraEngine {
    private Context mContext;

    private static class Create {
        static FxCameraEngine sFxCameraEngine = new FxCameraEngine();
    }

    private void checkContextUNLL() {
        if (mContext == null) {
            throw new RuntimeException("FxCamera context is null , place use init set context !!!!!!!");
        }
    }
}
