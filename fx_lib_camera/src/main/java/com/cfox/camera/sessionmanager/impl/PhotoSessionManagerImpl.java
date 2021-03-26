package com.cfox.camera.sessionmanager.impl;

import android.annotation.SuppressLint;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.camera.info.CameraInfoManagerImpl;
import com.cfox.camera.camera.device.session.DeviceSessionManager;
import com.cfox.camera.sessionmanager.PhotoSessionManager;
import com.cfox.camera.sessionmanager.callback.PhotoCaptureCallback;
import com.cfox.camera.sessionmanager.callback.PreviewCaptureCallback;
import com.cfox.camera.sessionmanager.req.SessionRequestManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.surface.SurfaceManager;
import com.cfox.camera.utils.EsParams;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * 负责挣了并发送个camera session 动作
 */
public class PhotoSessionManagerImpl extends AbsSessionManager implements PhotoSessionManager, Consumer<EsParams>  {

    private final PreviewCaptureCallback PREVIEW_CAPTURE_CALLBACK = new PreviewCaptureCallback();
    private final PhotoCaptureCallback PHOTO_CAPTURE_CALLBACK = new PhotoCaptureCallback();
    private final CameraInfoManager CAMERA_INFO_MANAGER = CameraInfoManagerImpl.CAMERA_INFO_MANAGER;
    private final SessionRequestManager SESSION_REQUEST_MANAGER = new SessionRequestManager(CAMERA_INFO_MANAGER);

    private CaptureRequest.Builder mPreviewBuilder;
    private CaptureRequest.Builder mCaptureBuilder;

    private DeviceSession mPhotoSession;
    private final DeviceSessionManager mCameraSessionManager;
    private SurfaceManager mSurfaceManager;

    public PhotoSessionManagerImpl(DeviceSessionManager sessionManager) {
        mCameraSessionManager = sessionManager;
    }

    @SuppressLint("CheckResult")
    @Override
    public void init() {
        PREVIEW_CAPTURE_CALLBACK.getPreviewStateSubject().subscribe(this);
        PHOTO_CAPTURE_CALLBACK.getCaptureStateSubject().subscribe(this);
    }

    @Override
    public Observable<EsParams> cameraStatus() {
        return PREVIEW_CAPTURE_CALLBACK.getPreviewStateSubject();
    }

    @Override
    public DeviceSession getCameraSession(EsParams esParams) {
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
        mCameraSessionManager.closeSession().subscribe();

    }

    @Override
    Observable<EsParams> applyPreviewPlan(final EsParams esParams) {
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsParams> emitter) {
                mSurfaceManager = esParams.get(EsParams.Key.SURFACE_MANAGER);
                PREVIEW_CAPTURE_CALLBACK.applyPreview(mPhotoSession, getPreviewBuilder());
                SESSION_REQUEST_MANAGER.applyPreviewRequest(getPreviewBuilder());
                applyRequestMessage(getPreviewBuilder(), esParams);
                esParams.put(EsParams.Key.REQUEST_BUILDER, getPreviewBuilder());
                esParams.put(EsParams.Key.CAPTURE_CALLBACK, PREVIEW_CAPTURE_CALLBACK);
                emitter.onNext(esParams);
            }
        });
    }

    private void applyRequestMessage(CaptureRequest.Builder builder, EsParams esParams) {
        // flash
        SESSION_REQUEST_MANAGER.applyFlashRequest(builder, esParams.get(EsParams.Key.CAMERA_FLASH_TYPE));
        // zoom
        SESSION_REQUEST_MANAGER.applyZoomRect(builder, esParams.get(EsParams.Key.ZOOM_RECT));
        // ev
        SESSION_REQUEST_MANAGER.applyEvRange(getPreviewBuilder(), esParams.get(EsParams.Key.EV_SIZE));
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
        esParams.put(EsParams.Key.CAPTURE_CALLBACK, PREVIEW_CAPTURE_CALLBACK);
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
                SESSION_REQUEST_MANAGER.applyAllRequest(builder);
                PHOTO_CAPTURE_CALLBACK.prepareCapture(mPhotoSession, builder);

                if (CAMERA_INFO_MANAGER.canTriggerAf()) {
                    PREVIEW_CAPTURE_CALLBACK.capture();
                } else {
                    PHOTO_CAPTURE_CALLBACK.capture();
                }
            }
        });
    }

    @Override
    public void accept(EsParams esParams) {
        EsLog.d("capture call back:" + esParams);
        Integer state = esParams.get(EsParams.Key.CAPTURE_STATE);

        if (state == null) {
            return;
        }

        switch (state) {
            case EsParams.Value.CAPTURE_STATE.CAPTURE : {
                PHOTO_CAPTURE_CALLBACK.capture();
                break;
            }

            case EsParams.Value.CAPTURE_STATE.CAPTURE_COMPLETED :
            case EsParams.Value.CAPTURE_STATE.CAPTURE_FAIL : {
                PREVIEW_CAPTURE_CALLBACK.resetPreviewState();
                break;
            }
        }
    }
}
