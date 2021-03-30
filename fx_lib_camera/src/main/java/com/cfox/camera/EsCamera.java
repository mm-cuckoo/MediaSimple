package com.cfox.camera;

import android.content.Context;

import com.cfox.camera.camera.info.CameraInfoHelper;
import com.cfox.camera.utils.WorkerHandlerManager;

public class EsCamera {
    private static boolean isInit = false;
    private EsCamera() {
    }

    public static void init(Context context) {
        isInit = true;
        CameraInfoHelper.getInstance().load(context, WorkerHandlerManager.getHandler(WorkerHandlerManager.Tag.T_TYPE_OTHER));
    }

    public static void release() {
        WorkerHandlerManager.getInstance().release();
    }

    public static EsCameraManager createCameraManager(Context context) {
        return createCameraManager(context, null);
    }

    public static EsCameraManager createCameraManager(Context context, ConfigStrategy strategy) {
        if (!isInit) {
            init(context.getApplicationContext());
        }
        return new EsCameraManagerImpl(context, strategy);
    }

}