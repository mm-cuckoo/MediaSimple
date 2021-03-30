package com.cfox.camera.camera.info;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

public class CameraInfoHelper {
    private static final CameraInfoHelper sCameraInfoHelper = new CameraInfoHelper();

    private final Object obj = new Object();
    private CameraManager mCameraManager;
    private boolean mIsLoadFinish = false;
    private final Map<String, CameraInfo> mCameraInfoMap = new HashMap<String, CameraInfo>();

    public static CameraInfoHelper getInstance() {
        return sCameraInfoHelper;
    }

    private CameraInfoHelper() {
    }

    public void load(Context context, Handler handler) {
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        LoadCameraInfoRunnable runnable = new LoadCameraInfoRunnable();
        if (handler ==null) {
            runnable.run();
        } else {
            handler.post(runnable);
        }
    }

    public CameraInfo getCameraInfo(String cameraId) {
        checkLoadFinish();
        return mCameraInfoMap.get(cameraId);
    }

    private void checkLoadFinish() {
        if (!mIsLoadFinish) {
            synchronized (obj) {
                if (!mIsLoadFinish) {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class LoadCameraInfoRunnable implements Runnable {
        @Override
        public void run() {
            synchronized (obj) {
                try {
                    for (String cameraId : mCameraManager.getCameraIdList()) {
                        CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraId);
                        mCameraInfoMap.put(cameraId, new CameraInfo(cameraId, cameraCharacteristics));
                    }
                    mIsLoadFinish = true;
                    obj.notifyAll();
                } catch (CameraAccessException e) {
                    obj.notifyAll();
                    e.printStackTrace();
                }
            }
        }
    }
}
