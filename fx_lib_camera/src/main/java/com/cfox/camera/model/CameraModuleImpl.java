package com.cfox.camera.model;

import android.content.Context;
import android.hardware.camera2.CameraDevice;

import com.cfox.camera.camera.FxCameraDevice;
import com.cfox.camera.camera.FxCameraDeviceImpl;
import com.cfox.camera.camera.FxCameraSession;
import com.cfox.camera.camera.FxCameraSessionImpl;
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

public class CameraModuleImpl implements CameraModule {
    private static CameraModule sCameraModule;
    private Map<ModuleFlag, IModule> mModuleMap = new HashMap<>(2);
    private IModule mCurrentModule;


    public static CameraModule getInstance(Context context) {
        if (sCameraModule == null) {
            synchronized (CameraModuleImpl.class) {
                if (sCameraModule == null) {
                    sCameraModule = new CameraModuleImpl(context);
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

    public CameraModuleImpl(Context context) {
        FxCameraDevice cameraDevice = FxCameraDeviceImpl.getsInstance(context);
        FxCameraSession cameraSession = FxCameraSessionImpl.getsInstance();
        mModuleMap.put(ModuleFlag.MODULE_PHOTO, new PhotoModule(cameraDevice, cameraSession));
        mModuleMap.put(ModuleFlag.MODULE_VIDEO, new VideoModule(cameraDevice, cameraSession));
    }
}
