package com.cfox.camera.model;

import android.content.Context;
import android.util.Log;
import android.util.Range;

import com.cfox.camera.IConfigWrapper;
import com.cfox.camera.camera.session.helper.IDulVideoSessionHelper;
import com.cfox.camera.camera.session.helper.impl.DulVideoSessionHelper;
import com.cfox.camera.camera.session.impl.CameraSessionManager;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.IPhotoSessionHelper;
import com.cfox.camera.camera.session.helper.IVideoSessionHelper;
import com.cfox.camera.camera.session.helper.impl.PhotoSessionHelper;
import com.cfox.camera.camera.session.helper.impl.VideoSessionHelper;
import com.cfox.camera.model.module.DulVideoModule;
import com.cfox.camera.model.module.business.DulVideoBusiness;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.model.module.IModule;
import com.cfox.camera.model.module.business.PhotoBusiness;
import com.cfox.camera.model.module.PhotoModule;
import com.cfox.camera.model.module.business.VideoBusiness;
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
        MODULE_VIDEO,
        MODULE_DUL_VIDEO

    }

    public static ICameraModule getInstance(Context context, IConfigWrapper configWrapper) {
        if (sCameraModule == null) {
            synchronized (CameraModule.class) {
                if (sCameraModule == null) {
                    sCameraModule = new CameraModule(context, configWrapper);
                }
            }
        }
        return sCameraModule;
    }

    @Override
    public Observable<FxResult> startPreview(final FxRequest request) {
        return  mCurrentModule.requestPreview(request);
    }

    @Override
    public void initModule(ModuleFlag moduleFlag) {
        Log.d(TAG, "initModule: module flag:" + moduleFlag);
        mCurrentModule = mModuleMap.get(moduleFlag);
        assert mCurrentModule != null;
    }

    @Override
    public Observable<FxResult> sendCameraConfig(FxRequest request) {
        return mCurrentModule.onCameraConfig(request);
    }

    @Override
    public Observable<FxResult> capture(FxRequest request) {
        return mCurrentModule.requestCapture(request);
    }

    private CameraModule(Context context, IConfigWrapper configWrapper) {
        ISessionManager sessionManager = CameraSessionManager.getInstance(context);

        IBusiness business = new PhotoBusiness(configWrapper);
        IPhotoSessionHelper photoSessionHelper = new PhotoSessionHelper(sessionManager);
        mModuleMap.put(ModuleFlag.MODULE_PHOTO, new PhotoModule(photoSessionHelper, business));


        IVideoSessionHelper videoSessionHelper = new VideoSessionHelper(sessionManager);
        business = new VideoBusiness(configWrapper);
        mModuleMap.put(ModuleFlag.MODULE_VIDEO, new VideoModule(videoSessionHelper, business));


        IDulVideoSessionHelper dulVideoSessionHelper = new DulVideoSessionHelper(sessionManager);
        business = new DulVideoBusiness(configWrapper);
        mModuleMap.put(ModuleFlag.MODULE_DUL_VIDEO, new DulVideoModule(dulVideoSessionHelper, business));
    }

    @Override
    public Observable<FxResult> stop() {
        return mCurrentModule.requestStop();
    }

    @Override
    public Range<Integer> getEvRange() {
        return mCurrentModule.getEvRange();
    }
}
