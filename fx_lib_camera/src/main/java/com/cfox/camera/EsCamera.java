package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.camera.info.CameraInfoHelper;
import com.cfox.camera.utils.WorkerHandlerManager;

public class EsCamera {
    private EsCamera() {
    }

    public static void init(Context context) {
        CameraInfoHelper.getInstance().load(context, WorkerHandlerManager.getHandler(WorkerHandlerManager.Tag.T_TYPE_OTHER));
    }

    public static void release() {
        WorkerHandlerManager.getInstance().release();
    }

}