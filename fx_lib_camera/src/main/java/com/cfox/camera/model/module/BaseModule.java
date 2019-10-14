package com.cfox.camera.model.module;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;
import android.util.Log;

import com.cfox.camera.camera.IFxCameraDevice;
import com.cfox.camera.camera.ISessionHelper;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;
import com.cfox.camera.utils.ThreadHandlerManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public abstract class BaseModule implements IModule {
    private static final String TAG = "BaseModule";

    private IFxCameraDevice mCameraDevice;
    private ISessionHelper mSessionHelper;

    public BaseModule(IFxCameraDevice cameraDevice, ISessionHelper sessionHelper) {
        this.mCameraDevice = cameraDevice;
        this.mSessionHelper = sessionHelper;
    }

    @Override
    public Observable<FxResult> onStartPreview(final FxRequest request) {
        SurfaceHelper mSurfaceHelper = (SurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        return Observable.combineLatest(mSurfaceHelper.isAvailable(), onOpenCamera(request),
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
                    CaptureRequest.Builder builder = mSessionHelper.createRequestBuilder(request);
                    request.put(FxRe.Key.PREVIEW_BUILDER, builder);
                    return mSessionHelper.createPreviewSession(request);
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
    public Observable<FxResult> onOpenCamera(FxRequest request) {
        return mCameraDevice.openCameraDevice(request);
    }

    @Override
    public Observable<FxResult> onStop() {
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                mSessionHelper.closeSession();
                mCameraDevice.closeCameraDevice();
            }
        }).subscribeOn(AndroidSchedulers.from(ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_OTHER).getLooper()));
    }
}

