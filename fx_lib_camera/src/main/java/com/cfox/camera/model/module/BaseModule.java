package com.cfox.camera.model.module;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.camera.IFxCameraDevice;
import com.cfox.camera.camera.IFxCameraSession;
import com.cfox.camera.camera.ISessionHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public abstract class BaseModule implements IModule {

    private IFxCameraDevice mCameraDevice;
    private ISessionHelper mSessionHelper;

    public BaseModule(IFxCameraDevice cameraDevice, ISessionHelper sessionHelper) {
        this.mCameraDevice = cameraDevice;
        this.mSessionHelper = sessionHelper;
    }

    @Override
    public Observable<FxResult> onStartPreview(FxRequest request) throws CameraAccessException {

        CaptureRequest.Builder builder = mSessionHelper.createRequestBuilder(request);
        request.put(FxRe.Key.PREVIEW_BUILDER, builder);
        return mSessionHelper.createPreviewSession(request);
    }

    @Override
    public Observable<FxResult> openCamera(FxRequest request) {
        return mCameraDevice.openCameraDevice(request);
    }
}

