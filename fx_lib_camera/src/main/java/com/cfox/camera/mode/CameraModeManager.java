package com.cfox.camera.mode;

import android.content.Context;
import android.util.Log;

import com.cfox.camera.sessionmanager.DulVideoSessionManager;
import com.cfox.camera.sessionmanager.impl.DulVideoSessionManagerImpl;
import com.cfox.camera.camera.device.session.DeviceSessionManagerImpl;
import com.cfox.camera.camera.device.session.DeviceSessionManager;
import com.cfox.camera.sessionmanager.PhotoSessionManager;
import com.cfox.camera.sessionmanager.VideoSessionManger;
import com.cfox.camera.sessionmanager.impl.PhotoSessionManagerImpl;
import com.cfox.camera.sessionmanager.impl.VideoSessionMangerImpl;
import com.cfox.camera.mode.impl.PhotoModeImpl;
import com.cfox.camera.mode.impl.VideoModeImpl;

import java.util.HashMap;
import java.util.Map;

public class CameraModeManager {
    private static final String TAG = "CameraModule";
    private volatile static CameraModeManager sCameraModeManager;
    private final Map<ModuleFlag, IMode> mModuleMap = new HashMap<>(ModuleFlag.values().length);
    private IMode mCurrentModule;

    public enum ModuleFlag {
        MODULE_PHOTO,
        MODULE_VIDEO,
        MODULE_DUL_VIDEO

    }

    public static CameraModeManager getInstance(Context context) {
        if (sCameraModeManager == null) {
            synchronized (CameraModeManager.class) {
                if (sCameraModeManager == null) {
                    sCameraModeManager = new CameraModeManager(context);
                }
            }
        }
        return sCameraModeManager;
    }

    public <T extends IMode> T initModule(ModuleFlag moduleFlag) {
        Log.d(TAG, "initModule: module flag:" + moduleFlag);
        IMode cameraMode = mModuleMap.get(moduleFlag);
        cameraMode.init();
        return (T) cameraMode;
    }


    private CameraModeManager(Context context) {
        DeviceSessionManager sessionManager = DeviceSessionManagerImpl.getInstance(context);

        PhotoSessionManager photoSessionManager = new PhotoSessionManagerImpl(sessionManager);
        mModuleMap.put(ModuleFlag.MODULE_PHOTO, new PhotoModeImpl(photoSessionManager));


        VideoSessionManger videoSessionManger = new VideoSessionMangerImpl(sessionManager);
        mModuleMap.put(ModuleFlag.MODULE_VIDEO, new VideoModeImpl(videoSessionManger));


        DulVideoSessionManager dulVideoSessionManager = new DulVideoSessionManagerImpl(sessionManager);
//        mModuleMap.put(ModuleFlag.MODULE_DUL_VIDEO, new DulVideoModeImpl(dulVideoSessionManager));
    }

}
