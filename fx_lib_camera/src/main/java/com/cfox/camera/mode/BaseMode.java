package com.cfox.camera.mode;


import com.cfox.camera.camera.session.helper.CameraSessionHelper;
import com.cfox.camera.log.EsLog;
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

public abstract class BaseMode implements IMode {

    private final CameraSessionHelper mCameraSessionHelper;

    protected BaseMode(CameraSessionHelper cameraSessionHelper) {
        this.mCameraSessionHelper = cameraSessionHelper;
    }

    @Override
    public void init() {
        mCameraSessionHelper.init();
    }

    protected Observable<EsResult> startPreview(final EsRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);
        return Observable.combineLatest(surfaceHelper.isAvailable(), onOpenCamera(request),
                new BiFunction<EsResult, EsResult, EsRequest>() {
                    @Override
                    public EsRequest apply(EsResult result1, EsResult result2) throws Exception {
                        EsLog.d("open camera device success ....." + request);
                        return request;
                    }
                }).flatMap(new Function<EsRequest, ObservableSource<EsResult>>() {
                    @Override
                    public ObservableSource<EsResult> apply(EsRequest fxRequest) throws Exception {
                        EsLog.d("create session before ....." + request);
                        // 创建 camera session
                        return mCameraSessionHelper.onCreatePreviewSession(request);
                    }
                }).flatMap(new Function<EsResult, ObservableSource<EsResult>>() {
                    @Override
                    public ObservableSource<EsResult> apply(EsResult fxResult) throws Exception {
                        EsLog.d("onSendRepeatingRequest ......" + request);
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
    public Observable<EsResult> requestStop(EsRequest request) {
        onRequestStop();
        return mCameraSessionHelper.close(request).subscribeOn(
                AndroidSchedulers.from(ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_OTHER).getLooper()));
    }

    public void onRequestStop() { }

//    @Override
//    public Range<Integer> getEvRange(EsRequest request) {
//        return mCameraSessionHelper.getEvRange(request);
//    }
}

