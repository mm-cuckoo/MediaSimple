package com.cfox.camera.model;

import android.content.Context;
import android.util.Log;

import com.cfox.camera.camera.device.IFxCameraDevice;
import com.cfox.camera.camera.device.FxCameraDevice;
import com.cfox.camera.camera.session.PhotoSession;
import com.cfox.camera.camera.session.helper.PhotoSessionHelper;
import com.cfox.camera.camera.session.VideoSession;
import com.cfox.camera.camera.session.helper.VideoSessionHelper;
import com.cfox.camera.model.module.IModule;
import com.cfox.camera.model.module.PhotoModule;
import com.cfox.camera.model.module.VideoModule;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class CameraModule implements ICameraModule {
    private static final String TAG = "CameraModule";
    private static ICameraModule sCameraModule;
    private Map<ModuleFlag, IModule> mModuleMap = new HashMap<>(ModuleFlag.values().length);
    private IModule mCurrentModule;

    public enum ModuleFlag {
        MODULE_PHOTO,
        MODULE_VIDEO
    }

    public static ICameraModule getInstance(Context context) {
        if (sCameraModule == null) {
            synchronized (CameraModule.class) {
                if (sCameraModule == null) {
                    sCameraModule = new CameraModule(context);
                }
            }
        }
        return sCameraModule;
    }

    @Override
    public Observable<FxResult> startPreview(final FxRequest request) {
        return  mCurrentModule.onStartPreview(request);
    }

    @Override
    public void initModule(ModuleFlag moduleFlag) {
        Log.d(TAG, "initModule: module flag:" + moduleFlag);
        mCurrentModule = mModuleMap.get(moduleFlag);
    }

    @Override
    public Observable<FxResult> sendCameraConfig(FxRequest request) {
        return mCurrentModule.onCameraConfig(request);
    }

    @Override
    public Observable<FxResult> capture(FxRequest request) {
        return mCurrentModule.onCapture(request);
    }

    private CameraModule(Context context) {
        IFxCameraDevice cameraDevice = FxCameraDevice.getsInstance(context);
        mModuleMap.put(ModuleFlag.MODULE_PHOTO, new PhotoModule(cameraDevice, new PhotoSessionHelper(new PhotoSession())));
        mModuleMap.put(ModuleFlag.MODULE_VIDEO, new VideoModule(cameraDevice, new VideoSessionHelper(new VideoSession())));
    }

    @Override
    public Observable<FxResult> stop() {
        return mCurrentModule.onStop();
    }
}
