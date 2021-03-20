package com.cfox.camera.mode;

import android.content.Context;
import android.util.Log;

import com.cfox.camera.camera.session.helper.IDulVideoSessionHelper;
import com.cfox.camera.camera.session.helper.impl.DulVideoSessionHelper;
import com.cfox.camera.camera.device.session.DeviceSessionManager;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.ImageSessionHelper;
import com.cfox.camera.camera.session.helper.IVideoSessionHelper;
import com.cfox.camera.camera.session.helper.impl.ImageSessionHelperImpl;
import com.cfox.camera.camera.session.helper.impl.VideoSessionHelperImpl;
import com.cfox.camera.mode.impl.DulVideoModeImpl;
import com.cfox.camera.mode.impl.ImageModeImpl;
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

        ImageSessionHelper photoSessionHelper = new ImageSessionHelperImpl(sessionManager);
        mModuleMap.put(ModuleFlag.MODULE_PHOTO, new ImageModeImpl(photoSessionHelper));


        IVideoSessionHelper videoSessionHelper = new VideoSessionHelperImpl(sessionManager);
        mModuleMap.put(ModuleFlag.MODULE_VIDEO, new VideoModeImpl(videoSessionHelper));


        IDulVideoSessionHelper dulVideoSessionHelper = new DulVideoSessionHelper(sessionManager);
        mModuleMap.put(ModuleFlag.MODULE_DUL_VIDEO, new DulVideoModeImpl(dulVideoSessionHelper));
    }

}
