package com.cfox.camera.model;

import android.content.Context;

import com.cfox.camera.camera.IFxCameraDevice;
import com.cfox.camera.camera.FxCameraDevice;
import com.cfox.camera.camera.IFxCameraSession;
import com.cfox.camera.camera.FxCameraSession;
import com.cfox.camera.model.module.IModule;
import com.cfox.camera.model.module.PhotoModule;
import com.cfox.camera.model.module.VideoModule;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class CameraModule implements ICameraModule {
    private static ICameraModule sCameraModule;
    private Map<ModuleFlag, IModule> mModuleMap = new HashMap<>(2);
    private IModule mCurrentModule;


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
    public Observable<FxResult> openCamera(FxRequest request) {
        return mCurrentModule.openCamera(request);
    }

    @Override
    public Observable<FxResult> changeModule(final ModuleFlag moduleFlag) {
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                mCurrentModule = mModuleMap.get(moduleFlag);
            }
        });
    }


    public enum ModuleFlag {
        MODULE_PHOTO,
        MODULE_VIDEO
    }

    public CameraModule(Context context) {
        IFxCameraDevice cameraDevice = FxCameraDevice.getsInstance(context);
        IFxCameraSession cameraSession = FxCameraSession.getsInstance();
        mModuleMap.put(ModuleFlag.MODULE_PHOTO, new PhotoModule(cameraDevice, cameraSession));
        mModuleMap.put(ModuleFlag.MODULE_VIDEO, new VideoModule(cameraDevice, cameraSession));
    }
}
