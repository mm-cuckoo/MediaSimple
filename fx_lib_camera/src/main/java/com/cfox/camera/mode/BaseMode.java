package com.cfox.camera.mode;


import com.cfox.camera.sessionmanager.SessionManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.EsParams;
import com.cfox.camera.utils.WorkerHandlerManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public abstract class BaseMode implements IMode {

    private final SessionManager mSessionManager;

    protected BaseMode(SessionManager sessionManager) {
        this.mSessionManager = sessionManager;
    }

    @Override
    public void init() {
        mSessionManager.init();
    }

    public Observable<EsParams> requestPreview(EsParams esParams) {
        return applySurface(esParams).flatMap(new Function<EsParams, ObservableSource<EsParams>>() {
            @Override
            public ObservableSource<EsParams> apply(@NonNull EsParams esParams) {
                EsLog.d("open camera request ===>params:" + esParams);
                // open camera request
                return mSessionManager.onOpenCamera(esParams);
            }
        }).flatMap(new Function<EsParams, ObservableSource<EsParams>>() {
            @Override
            public ObservableSource<EsParams> apply(@NonNull EsParams esParams) throws Exception {
                EsLog.d("create session before ....." + esParams);
                // 创建 camera session
                return mSessionManager.onCreatePreviewSession(esParams);
            }
        }).flatMap(new Function<EsParams, ObservableSource<EsParams>>() {
            @Override
            public ObservableSource<EsParams> apply(@NonNull EsParams esParams) throws Exception {
                EsLog.d("onSendRepeatingRequest ......" + esParams);
                return mSessionManager.onPreviewRepeatingRequest(esParams);
            }
        }).subscribeOn(WorkerHandlerManager.getScheduler(WorkerHandlerManager.Tag.T_TYPE_CAMERA));
    }

    @Override
    public Observable<EsParams> requestCameraRepeating(EsParams esParams) {
        return mSessionManager.onRepeatingRequest(esParams);
    }

    @Override
    public Observable<EsParams> requestStop(EsParams esParams) {
        onRequestStop();
        return mSessionManager.close(esParams).subscribeOn(
                AndroidSchedulers.from(WorkerHandlerManager.getLooper(WorkerHandlerManager.Tag.T_TYPE_CAMERA)));
    }

    public void onRequestStop() { }

    protected abstract Observable<EsParams> applySurface(EsParams esParams);

}

