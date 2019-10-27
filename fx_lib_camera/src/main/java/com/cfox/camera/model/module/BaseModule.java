package com.cfox.camera.model.module;


import android.hardware.camera2.CaptureRequest;
import android.util.Log;

import com.cfox.camera.camera.device.IFxCameraDevice;
import com.cfox.camera.camera.session.helper.ISessionHelper;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.surface.ISurfaceHelper;
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
    private IBusiness mBusiness;

    BaseModule(IFxCameraDevice cameraDevice, ISessionHelper sessionHelper, IBusiness business) {
        this.mCameraDevice = cameraDevice;
        this.mSessionHelper = sessionHelper;
        this.mBusiness = business;
    }

    IBusiness getBusiness() {
        return mBusiness;
    }

    Observable<FxResult> startPreview(final FxRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        return Observable.combineLatest(surfaceHelper.isAvailable(), onOpenCamera(request),
                new BiFunction<FxResult, FxResult, FxRequest>() {
                    @Override
                    public FxRequest apply(FxResult result1, FxResult result2) throws Exception {
                        Log.d(TAG, "apply: open camera device success .....");
                        request.put(FxRe.Key.CAMERA_DEVICE, result2.getObj(FxRe.Key.CAMERA_DEVICE));
                        return request;
                    }
                }).flatMap(new Function<FxRequest, ObservableSource<FxResult>>() {
                    @Override
                    public ObservableSource<FxResult> apply(FxRequest fxRequest) throws Exception {
                        Log.d(TAG, "apply: create  session ....." + request);
                        CaptureRequest.Builder builder = mSessionHelper.createPreviewRepeatingBuilder(request);
                        request.put(FxRe.Key.REQUEST_BUILDER, builder);
                        return mSessionHelper.createPreviewSession(request);
                    }
                }).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
                    @Override
                    public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                        Log.d(TAG, "apply: sendRepeatingRequest......");
                        return mSessionHelper.sendPreviewRepeatingRequest(request);
                    }
                }).subscribeOn(AndroidSchedulers.from(ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_CAMERA).getLooper()));
    }

    @Override
    public Observable<FxResult> onCameraConfig(FxRequest request) {
        return mSessionHelper.sendRepeatingRequest(request);
    }

    @Override
    public Observable<FxResult> onOpenCamera(FxRequest request) {
        return mCameraDevice.openCameraDevice(request);
    }

    @Override
    public Observable<FxResult> onCapture(FxRequest request) {
        return null;
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

