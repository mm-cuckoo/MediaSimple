package com.cfox.camera.model;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.util.Log;

import com.cfox.camera.camera.IFxCameraDevice;
import com.cfox.camera.camera.FxCameraDevice;
import com.cfox.camera.camera.PhotoSessionHelper;
import com.cfox.camera.camera.VideoSessionHelper;
import com.cfox.camera.model.module.IModule;
import com.cfox.camera.model.module.PhotoModule;
import com.cfox.camera.model.module.VideoModule;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

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

    private CameraModule(Context context) {
        IFxCameraDevice cameraDevice = FxCameraDevice.getsInstance(context);
        mModuleMap.put(ModuleFlag.MODULE_PHOTO, new PhotoModule(cameraDevice, new PhotoSessionHelper()));
        mModuleMap.put(ModuleFlag.MODULE_VIDEO, new VideoModule(cameraDevice, new VideoSessionHelper()));
    }

    @Override
    public Observable<FxResult> onStop() {
        return mCurrentModule.onStop();
    }
}
