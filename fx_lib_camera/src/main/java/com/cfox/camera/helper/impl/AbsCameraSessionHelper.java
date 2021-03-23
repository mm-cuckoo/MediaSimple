package com.cfox.camera.helper.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.view.Surface;

import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.helper.CameraSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import java.util.ArrayList;
import java.util.List;

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
        return beforeOpenCamera(request)
                .flatMap(new Function<EsResult, ObservableSource<EsResult>>() {
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
        return applyPreviewPlan(request).flatMap(new Function<EsRequest, ObservableSource<EsResult>>() {
            @Override
            public ObservableSource<EsResult> apply(@NonNull EsRequest esRequest) {
                return getCameraSession(esRequest).onRepeatingRequest(esRequest);
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
            captureBuilder = getCameraSession(new EsRequest()).onCreateRequestBuilder(templateType);
            EsLog.d("surface size: ||||||||||||||---->" + surfaceList.size());
            for (Surface surface : surfaceList) {
                captureBuilder.addTarget(surface);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return captureBuilder;
    }



    /**
     * open camera
     */
    abstract Observable<EsResult> beforeOpenCamera(EsRequest request);

    abstract Observable<EsRequest> applyPreviewPlan(EsRequest request);

    public abstract DeviceSession getCameraSession(EsRequest request);
}
