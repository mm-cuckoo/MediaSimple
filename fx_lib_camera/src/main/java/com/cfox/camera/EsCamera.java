package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.camera.info.CameraInfoHelper;
import com.cfox.camera.utils.ThreadHandlerManager;

public class EsCamera {
    private EsCamera() {
    }

    public static void init(Context context) {
        CameraInfoHelper.getInstance().load(context,
                ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_OTHER).getHandler());
    }

    public static void release() {
        ThreadHandlerManager.getInstance().release();
    }

}