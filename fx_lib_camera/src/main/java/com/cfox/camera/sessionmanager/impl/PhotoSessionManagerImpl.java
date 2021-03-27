package com.cfox.camera.sessionmanager.impl;

import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.cfox.camera.camera.session.CameraSession;
import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.camera.info.CameraInfoManagerImpl;
import com.cfox.camera.camera.session.CameraSessionManager;
import com.cfox.camera.sessionmanager.PhotoSessionManager;
import com.cfox.camera.sessionmanager.ZoomHelper;
import com.cfox.camera.sessionmanager.callback.PhotoCaptureCallback;
import com.cfox.camera.sessionmanager.callback.PreviewCaptureCallback;
import com.cfox.camera.sessionmanager.req.SessionRequestManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.surface.SurfaceManager;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.utils.EsParams;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Predicate;

public class PhotoSessionManagerImpl extends AbsSessionManager implements PhotoSessionManager {

    private final PreviewCaptureCallback mPreviewCaptureCallback;
    private final PhotoCaptureCallback mPhotoCaptureCallback;
    private final CameraInfoManager mCameraInfoManager;
    private final SessionRequestManager mSessionRequestManager;
    private final ZoomHelper mZoomHelper;
    private final CameraSessionManager mCameraSessionManager;

    private CaptureRequest.Builder mPreviewBuilder;
    private CaptureRequest.Builder mCaptureBuilder;

    private CameraSession mPhotoSession;
    private SurfaceManager mSurfaceManager;

    public PhotoSessionManagerImpl(CameraSessionManager sessionManager) {
        mCameraSessionManager = sessionManager;
        mPreviewCaptureCallback = new PreviewCaptureCallback();
        mPhotoCaptureCallback = new PhotoCaptureCallback();
        mCameraInfoManager = CameraInfoManagerImpl.CAMERA_INFO_MANAGER;
        mSessionRequestManager = new SessionRequestManager(mCameraInfoManager);
        mZoomHelper = new ZoomHelper(mCameraInfoManager);
    }

    @Override
    public void init() {

    }

    @Override
    public CameraSession getCameraSession(EsParams esParams) {
        if (mPhotoSession == null) {
            mPhotoSession = mCameraSessionManager.createSession();
        }
        return mPhotoSession;
    }


    @Override
    public void onBeforeOpenCamera(EsParams esParams) {
        mPhotoSession = null;
        mPreviewBuilder = null;
        mCaptureBuilder = null;
        mCameraSessionManager.closeSession().subscribe(new CameraObserver<EsParams>());

    }

    @Override
    public Observable<EsParams> onPreviewRepeatingRequest(final EsParams esParams) {
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsParams> emitter) {
                mSurfaceManager = esParams.get(EsParams.Key.SURFACE_MANAGER);
                mPreviewCaptureCallback.applyPreview(mPhotoSession, getPreviewBuilder(), emitter);
                mSessionRequestManager.applyPreviewRequest(getPreviewBuilder());
                applyRequestMessage(getPreviewBuilder(), esParams);
                esParams.put(EsParams.Key.REQUEST_BUILDER, getPreviewBuilder());
                esParams.put(EsParams.Key.CAPTURE_CALLBACK, mPreviewCaptureCallback);
                mPhotoSession.onRepeatingRequest(esParams).subscribe(new CameraObserver<EsParams>());
            }
        }).filter(new Predicate<EsParams>() {
            @Override
            public boolean test(@NonNull EsParams esParams) {
                // 如果 preview 发来的信息是开始捕获， 会被拦截并进行capture
                Integer captureState = esParams.get(EsParams.Key.CAPTURE_STATE);
                if (captureState != null && captureState == EsParams.Value.CAPTURE_STATE.CAPTURE_START) {
                    mPhotoCaptureCallback.capture();
                    return true;
                }
                return false;
            }
        });
    }

    private void applyRequestMessage(CaptureRequest.Builder builder, EsParams esParams) {
        // zoom
        Float zoomValue = esParams.get(EsParams.Key.ZOOM_VALUE);
        if (zoomValue != null) {
            mSessionRequestManager.applyZoomRect(builder, mZoomHelper.getZoomRect(zoomValue));
        }

        // flash
        mSessionRequestManager.applyFlashRequest(builder, esParams.get(EsParams.Key.FLASH_STATE));
        // ev
        mSessionRequestManager.applyEvRange(builder, esParams.get(EsParams.Key.EV_SIZE));
    }

    private CaptureRequest.Builder getPreviewBuilder() {
        if (mPreviewBuilder == null) {
            mPreviewBuilder = createPreviewBuilder(mSurfaceManager.getPreviewSurface());
        }
        return mPreviewBuilder;
    }

    private CaptureRequest.Builder getCaptureBuilder() {
        if (mCaptureBuilder == null) {
            mCaptureBuilder = createCaptureBuilder(mSurfaceManager.getReaderSurface());
        }
        return mCaptureBuilder;
    }

    private CaptureRequest.Builder createCaptureBuilder(List<Surface> surfaceList) {
        return createBuilder(CameraDevice.TEMPLATE_STILL_CAPTURE, surfaceList);
    }

    @Override
    public Observable<EsParams> onRepeatingRequest(EsParams esParams) {
        EsLog.d("onSendRepeatingRequest: req:" + esParams);
        applyRequestMessage(getPreviewBuilder(), esParams);
        esParams.put(EsParams.Key.REQUEST_BUILDER, mPreviewBuilder);
        esParams.put(EsParams.Key.CAPTURE_CALLBACK, mPreviewCaptureCallback);
        return mPhotoSession.onRepeatingRequest(esParams);
    }


    @Override
    public Observable<EsParams> close(EsParams esParams) {
        return mCameraSessionManager.closeSession();
    }

    @Override
    public Observable<EsParams> capture(final EsParams esParams) {
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<EsParams> emitter) {
                CaptureRequest.Builder builder = getCaptureBuilder();
                builder.set(CaptureRequest.JPEG_ORIENTATION, esParams.get(EsParams.Key.PIC_ORIENTATION, 0));
                mSessionRequestManager.applyAllRequest(builder);
                mPhotoCaptureCallback.prepareCapture(mPhotoSession, builder, emitter);
                if (mCameraInfoManager.canTriggerAf()) {
                    mPreviewCaptureCallback.capture();
                } else {
                    mPhotoCaptureCallback.capture();
                }
            }
        }).filter(new Predicate<EsParams>() {
            @Override
            public boolean test(@NonNull EsParams esParams) {
                Integer captureState = esParams.get(EsParams.Key.CAPTURE_STATE);
                if (captureState != null
                        && (captureState == EsParams.Value.CAPTURE_STATE.CAPTURE_FAIL
                        || captureState == EsParams.Value.CAPTURE_STATE.CAPTURE_COMPLETED)) {
                    mPreviewCaptureCallback.resetPreviewState();
                }
                return false;
            }
        });
    }
}
