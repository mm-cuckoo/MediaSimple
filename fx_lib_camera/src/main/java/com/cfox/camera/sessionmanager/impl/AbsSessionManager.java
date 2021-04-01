package com.cfox.camera.sessionmanager.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.view.Surface;

import com.cfox.camera.camera.session.CameraSession;
import com.cfox.camera.sessionmanager.SessionManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.surface.SurfaceManager;
import com.cfox.camera.EsParams;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

abstract class AbsSessionManager implements SessionManager {

    /**
     * 在这个抽象类中会维护一个 CameraSession {@link CameraSession} 供内部使用，
     * 这个实例在 {@link AbsSessionManager#onBeforeOpenCamera(EsParams)}  方法调用后被实例
     */
    private CameraSession mCameraSession;
    /**
     * 对外提供预览 builder
     */
    private CaptureRequest.Builder mPreviewBuilder;
    /**
     * surface 管理
     */
    private SurfaceManager mSurfaceManager;

    /**
     * 向外提供surface 管理，该对象在 onOpenCamera 方法后被实例
     * @return 返回 {@link SurfaceManager}
     */
    SurfaceManager getSurfaceManager() {
        return mSurfaceManager;
    }

    public abstract CameraSession getCameraSession(EsParams esParams);

    /**
     * open camera ， 在open camera 之前会调用 onBeforeOpenCamera 方法进行关闭和初始化一些配置
     */
    @Override
    public Observable<EsParams> onOpenCamera(final EsParams esParams) {
        mSurfaceManager = esParams.get(EsParams.Key.SURFACE_MANAGER);
        mPreviewBuilder = null;// SurfaceManager 被更新，preview builder 需要更新
        return beforeOpenCamera(esParams).flatMap(new Function<EsParams, ObservableSource<EsParams>>() {
            @Override
            public ObservableSource<EsParams> apply(@NonNull EsParams esParams) {
                EsLog.d("session open camera ===>" + esParams);
                mCameraSession = getCameraSession(esParams);
                return mCameraSession.onOpenCamera(esParams);
            }
        });
    }

    private Observable<EsParams> beforeOpenCamera(final EsParams esParams) {
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsParams> emitter) {
                onBeforeOpenCamera(esParams);
                emitter.onNext(esParams);
            }
        });
    }

    abstract void onBeforeOpenCamera(EsParams esParams);


    CaptureRequest.Builder getPreviewBuilder() {
        if (mPreviewBuilder == null) {
            mPreviewBuilder = createPreviewBuilder(mSurfaceManager.getPreviewSurface());
        }
        return mPreviewBuilder;
    }

    /**
     * 创建一个capture session
     */
    @Override
    public Observable<EsParams> onCreatePreviewSession(EsParams esParams) {
        return mCameraSession.onCreateCaptureSession(esParams);
    }

    CaptureRequest.Builder createPreviewBuilder( List<Surface> surfaceList) {
        return createBuilder(CameraDevice.TEMPLATE_PREVIEW, surfaceList);
    }

    CaptureRequest.Builder createBuilder(int templateType, List<Surface> surfaceList) {
        CaptureRequest.Builder captureBuilder = null;
        try {
            captureBuilder = mCameraSession.onCreateRequestBuilder(templateType);
            EsLog.d("surface size: ||||||||||||||---->" + surfaceList.size());
            for (Surface surface : surfaceList) {
                captureBuilder.addTarget(surface);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return captureBuilder;
    }


}
