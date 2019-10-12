package com.cfox.camera.model;

import android.content.Context;
import android.util.Log;

import com.cfox.camera.camera.IFxCameraDevice;
import com.cfox.camera.camera.FxCameraDevice;
import com.cfox.camera.camera.IFxCameraSession;
import com.cfox.camera.camera.FxCameraSession;
import com.cfox.camera.model.module.IModule;
import com.cfox.camera.model.module.PhotoModule;
import com.cfox.camera.model.module.VideoModule;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.FxReq;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

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
    public Observable<FxResult> startPreview(FxRequest request) {
        SurfaceHelper mSurfaceHelper = (SurfaceHelper) request.getObj(FxReq.Key.SURFACE_HELPER);
        return Observable.combineLatest(mSurfaceHelper.isAvailable(), mCurrentModule.openCamera(request),
                new BiFunction<FxRequest, FxResult, FxResult>() {
            @Override
            public FxResult apply(FxRequest request, FxResult fxResult) throws Exception {
                return fxResult;
            }
        });
    }

    @Override
    public void initModule(ModuleFlag moduleFlag) {
        Log.d(TAG, "initModule: module flag:" + moduleFlag);
        mCurrentModule = mModuleMap.get(moduleFlag);
    }

    private CameraModule(Context context) {
        IFxCameraDevice cameraDevice = FxCameraDevice.getsInstance(context);
        IFxCameraSession cameraSession = FxCameraSession.getsInstance();
        mModuleMap.put(ModuleFlag.MODULE_PHOTO, new PhotoModule(cameraDevice, cameraSession));
        mModuleMap.put(ModuleFlag.MODULE_VIDEO, new VideoModule(cameraDevice, cameraSession));
    }
}
