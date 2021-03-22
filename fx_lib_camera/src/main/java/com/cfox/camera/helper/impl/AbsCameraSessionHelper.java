package com.cfox.camera.helper.impl;

import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.helper.CameraSessionHelper;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public abstract class AbsCameraSessionHelper implements CameraSessionHelper {

    /**
     * open camera ， 在open camera 之前会调用beforeOpenCamera 方法进行关闭和初始化一些配置
     */
    @Override
    public Observable<EsResult> onOpenCamera(final EsRequest request) {
        return beforeOpenCamera(request).flatMap(new Function<EsResult, ObservableSource<EsResult>>() {
            @Override
            public ObservableSource<EsResult> apply(@NonNull EsResult esResult) throws Exception {
                return getCameraSession(request).onOpenCamera(request);
            }
        });
    }

    /**
     * 创建一个capture session
     */
    @Override
    public Observable<EsResult> onCreatePreviewSession(EsRequest request) {
        return getCameraSession(request).onCreateCaptureSession(request);
    }

    @Override
    public Observable<EsResult> onPreviewRepeatingRequest(final EsRequest request) {
        return Observable.create(new ObservableOnSubscribe<EsRequest>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsRequest> emitter) {
                applyPreviewRepeatingBuilder(request);
                emitter.onNext(request);
            }
        }).flatMap(new Function<EsRequest, ObservableSource<EsResult>>() {
            @Override
            public ObservableSource<EsResult> apply(@NonNull EsRequest esRequest) {
                return getCameraSession(esRequest).onRepeatingRequest(esRequest);
            }
        });
    }

    /**
     * open camera
     */
    abstract Observable<EsResult> beforeOpenCamera(EsRequest request);

    public void applyPreviewRepeatingBuilder(EsRequest request){}

    public abstract DeviceSession getCameraSession(EsRequest request);
}
