package com.cfox.camera.model.module;


import android.util.Range;
import android.util.Size;

import com.cfox.camera.camera.session.helper.ICameraSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;
import com.cfox.camera.utils.ThreadHandlerManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public abstract class BaseModule implements IModule {

    private ICameraSessionHelper mCameraSessionHelper;
    private IBusiness mBusiness;

    BaseModule(ICameraSessionHelper cameraSessionHelper, IBusiness business) {
        this.mCameraSessionHelper = cameraSessionHelper;
        this.mBusiness = business;
    }

    @Override
    public void init() {
        mCameraSessionHelper.init();
    }

    IBusiness getBusiness() {
        return mBusiness;
    }

    Observable<EsResult> startPreview(final EsRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);
        return Observable.combineLatest(surfaceHelper.isAvailable(), onOpenCamera(request),
                new BiFunction<EsResult, EsResult, EsRequest>() {
                    @Override
                    public EsRequest apply(EsResult result1, EsResult result2) throws Exception {
                        EsLog.d("open camera device success .....");
                        request.put(Es.Key.CAMERA_DEVICE, result2.getObj(Es.Key.CAMERA_DEVICE));
                        return request;
                    }
                }).flatMap(new Function<EsRequest, ObservableSource<EsResult>>() {
                    @Override
                    public ObservableSource<EsResult> apply(EsRequest fxRequest) throws Exception {
                        EsLog.d("create session before ....." + request);
                        return mCameraSessionHelper.onCreatePreviewSession(request);
                    }
                }).flatMap(new Function<EsResult, ObservableSource<EsResult>>() {
                    @Override
                    public ObservableSource<EsResult> apply(EsResult fxResult) throws Exception {
                        EsLog.d("onSendRepeatingRequest ......");
                        return mCameraSessionHelper.onSendPreviewRepeatingRequest(request);
                    }
                }).subscribeOn(AndroidSchedulers.from(ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_CAMERA).getLooper()));
    }

    @Override
    public Observable<EsResult> requestCameraConfig(EsRequest request) {
        return mCameraSessionHelper.onSendRepeatingRequest(request);
    }

    private Observable<EsResult> onOpenCamera(EsRequest request) {
        return mCameraSessionHelper.onOpenCamera(request);
    }

    @Override
    public Observable<EsResult> requestCapture(EsRequest request) {
        return null;
    }

    @Override
    public Observable<EsResult> requestStop(EsRequest request) {
        onRequestStop();
        return mCameraSessionHelper.close(request).subscribeOn(AndroidSchedulers.from(ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_OTHER).getLooper()));
    }

    public void onRequestStop() { }

    @Override
    public Range<Integer> getEvRange(EsRequest request) {
        return mCameraSessionHelper.getEvRange(request);
    }
}

