package com.cfox.camera.model.module;


import android.graphics.ImageFormat;
import android.util.Log;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.camera.session.helper.ICameraSessionHelper;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;
import com.cfox.camera.utils.ThreadHandlerManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public abstract class BaseModule implements IModule {
    private static final String TAG = "BaseModule";

    private ICameraSessionHelper mCameraSessionHelper;
    private IBusiness mBusiness;

    BaseModule(ICameraSessionHelper cameraSessionHelper, IBusiness business) {
        this.mCameraSessionHelper = cameraSessionHelper;
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
                        Size pictureSizeForReq = (Size) request.getObj(FxRe.Key.PIC_SIZE);
                        Size pictureSize = getBusiness().getPictureSize(pictureSizeForReq, mCameraSessionHelper.getPictureSize(request));
                        request.put(FxRe.Key.PIC_SIZE, pictureSize);
                        Log.d(TAG, "apply: create  session ....." + request);
                        return mCameraSessionHelper.onCreatePreviewSession(request);
                    }
                }).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
                    @Override
                    public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                        Log.d(TAG, "apply: onSendRepeatingRequest......");
                        return mCameraSessionHelper.onSendPreviewRepeatingRequest(request);
                    }
                }).subscribeOn(AndroidSchedulers.from(ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_CAMERA).getLooper()));
    }

    @Override
    public Observable<FxResult> onCameraConfig(FxRequest request) {
        return mCameraSessionHelper.onSendRepeatingRequest(request);
    }

    private Observable<FxResult> onOpenCamera(FxRequest request) {
        return mCameraSessionHelper.onOpenCamera(request);
    }

    @Override
    public Observable<FxResult> requestCapture(FxRequest request) {
        return null;
    }

    @Override
    public Observable<FxResult> requestStop() {
        return mCameraSessionHelper.close().subscribeOn(AndroidSchedulers.from(ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_OTHER).getLooper()));
    }

    @Override
    public Range<Integer> getEvRange() {
        return mCameraSessionHelper.getEvRange();
    }
}

