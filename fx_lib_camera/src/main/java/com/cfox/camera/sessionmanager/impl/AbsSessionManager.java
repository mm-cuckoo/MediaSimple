package com.cfox.camera.sessionmanager.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.view.Surface;

import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.sessionmanager.SessionManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.EsParams;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

abstract class AbsSessionManager implements SessionManager {

    /**
     * open camera ， 在open camera 之前会调用beforeOpenCamera 方法进行关闭和初始化一些配置
     */
    @Override
    public Observable<EsParams> onOpenCamera(final EsParams esParams) {
        return beforeOpenCamera(esParams).flatMap(new Function<EsParams, ObservableSource<EsParams>>() {
            @Override
            public ObservableSource<EsParams> apply(@NonNull EsParams esParams) {
                EsLog.d("session open camera ===>" + esParams);
                return getCameraSession(esParams).onOpenCamera(esParams);
            }
        });
    }

    private Observable<EsParams> beforeOpenCamera(final EsParams esParams) {
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsParams> emitter) throws Exception {
                onBeforeOpenCamera(esParams);
                emitter.onNext(esParams);
            }
        });
    }

    abstract void onBeforeOpenCamera(EsParams esParams);

    /**
     * 创建一个capture session
     */
    @Override
    public Observable<EsParams> onCreatePreviewSession(EsParams esParams) {
        return getCameraSession(esParams).onCreateCaptureSession(esParams);
    }

    @Override
    public Observable<EsParams> onPreviewRepeatingRequest(final EsParams param) {
        return applyPreviewPlan(param).flatMap(new Function<EsParams, ObservableSource<EsParams>>() {
            @Override
            public ObservableSource<EsParams> apply(@NonNull EsParams esParams) {
                return getCameraSession(esParams).onRepeatingRequest(esParams);
            }
        });
    }

    CaptureRequest.Builder createPreviewBuilder(Surface surface) {
        List<Surface> previewSurfaceList = new ArrayList<>();
        previewSurfaceList.add(surface);
        return createBuilder(CameraDevice.TEMPLATE_PREVIEW, previewSurfaceList);
    }

    CaptureRequest.Builder createBuilder(int templateType, List<Surface> surfaceList) {
        CaptureRequest.Builder captureBuilder = null;
        try {
            captureBuilder = getCameraSession(new EsParams()).onCreateRequestBuilder(templateType);
            EsLog.d("surface size: ||||||||||||||---->" + surfaceList.size());
            for (Surface surface : surfaceList) {
                captureBuilder.addTarget(surface);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return captureBuilder;
    }

    abstract Observable<EsParams> applyPreviewPlan(EsParams esParams);

    public abstract DeviceSession getCameraSession(EsParams esParams);
}
