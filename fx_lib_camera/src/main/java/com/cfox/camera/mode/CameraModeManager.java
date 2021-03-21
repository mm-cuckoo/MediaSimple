package com.cfox.camera.mode;

import android.content.Context;
import android.util.Log;

import com.cfox.camera.camera.session.helper.DulVideoSessionHelper;
import com.cfox.camera.camera.session.helper.impl.DulVideoSessionHelperImpl;
import com.cfox.camera.camera.device.session.DeviceSessionManager;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.PhotoSessionHelper;
import com.cfox.camera.camera.session.helper.VideoSessionHelper;
import com.cfox.camera.camera.session.helper.impl.PhotoSessionHelperImpl;
import com.cfox.camera.camera.session.helper.impl.VideoSessionHelperImpl;
import com.cfox.camera.mode.impl.DulVideoModeImpl;
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
        ISessionManager sessionManager = DeviceSessionManager.getInstance(context);

        PhotoSessionHelper photoSessionHelper = new PhotoSessionHelperImpl(sessionManager);
        mModuleMap.put(ModuleFlag.MODULE_PHOTO, new PhotoModeImpl(photoSessionHelper));


        VideoSessionHelper videoSessionHelper = new VideoSessionHelperImpl(sessionManager);
        mModuleMap.put(ModuleFlag.MODULE_VIDEO, new VideoModeImpl(videoSessionHelper));


        DulVideoSessionHelper dulVideoSessionHelper = new DulVideoSessionHelperImpl(sessionManager);
        mModuleMap.put(ModuleFlag.MODULE_DUL_VIDEO, new DulVideoModeImpl(dulVideoSessionHelper));
    }

}
