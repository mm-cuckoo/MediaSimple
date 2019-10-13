package com.cfox.camera.model;

import android.content.Context;
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
        SurfaceHelper mSurfaceHelper = (SurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        return Observable.combineLatest(mSurfaceHelper.isAvailable(), mCurrentModule.openCamera(request),
                new BiFunction<FxRequest, FxResult, FxResult>() {
            @Override
            public FxResult apply(FxRequest request, FxResult fxResult) throws Exception {
                return fxResult;
            }
        }).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                String openStatus = fxResult.getString(FxRe.Key.OPEN_CAMERA_STATUS, FxRe.Value.OPEN_FAIL);
                Log.d(TAG, "apply: open status :" + openStatus);
                if (openStatus.equals(FxRe.Value.OPEN_SUCCESS)) {
                    request.put(FxRe.Key.CAMERA_DEVICE, fxResult.getObj(FxRe.Key.CAMERA_DEVICE));
                    return mCurrentModule.onStartPreview(request);
                }
                return Observable.create(new ObservableOnSubscribe<FxResult>() {
                    @Override
                    public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {

                    }
                });
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
        mModuleMap.put(ModuleFlag.MODULE_PHOTO, new PhotoModule(cameraDevice, new PhotoSessionHelper()));
        mModuleMap.put(ModuleFlag.MODULE_VIDEO, new VideoModule(cameraDevice, new VideoSessionHelper()));
    }
}
