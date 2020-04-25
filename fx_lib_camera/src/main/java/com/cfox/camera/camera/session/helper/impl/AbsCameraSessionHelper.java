package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraAccessException;
import android.util.Range;

import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.camera.ICameraInfo;
import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.helper.ICameraSessionHelper;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public abstract class AbsCameraSessionHelper implements ICameraSessionHelper {
    @Override
    public Observable<EsResult> onOpenCamera(final EsRequest request) {
        return Observable.combineLatest(
                beforeOpenCamera(request),
                getCameraSession(request).onOpenCamera(request), new BiFunction<EsResult, EsResult, EsResult>() {
                    @Override
                    public EsResult apply(EsResult esResult, EsResult esResult2) throws Exception {
                        return esResult2;
                    }
                });
    }

    @Override
    public Observable<EsResult> onCreatePreviewSession(EsRequest request) {
        return getCameraSession(request).onCreatePreviewSession(request);
    }

    @Override
    public Observable<EsResult> onSendPreviewRepeatingRequest(EsRequest request) throws CameraAccessException {
        applyPreviewRepeatingBuilder(request);
        return getCameraSession(request).onRepeatingRequest(request);
    }

    abstract Observable<EsResult> beforeOpenCamera(EsRequest request);

    public void applyPreviewRepeatingBuilder(EsRequest request) throws CameraAccessException {}

    @Override
    public Range<Integer> getEvRange(EsRequest request) {
        return null;
    }

    public abstract ICameraSession getCameraSession(EsRequest request);

    ICameraInfo getCameraInfo(String cameraId) {
        return CameraInfoHelper.getInstance().getCameraInfo(cameraId);
    }
}
