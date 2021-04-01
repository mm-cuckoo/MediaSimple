package com.cfox.camera.sessionmanager.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import android.util.Pair;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.cfox.camera.camera.session.CameraSession;
import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.camera.info.CameraInfoManagerImpl;
import com.cfox.camera.camera.session.CameraSessionManager;
import com.cfox.camera.sessionmanager.FocusHelper;
import com.cfox.camera.sessionmanager.PhotoSessionManager;
import com.cfox.camera.sessionmanager.ZoomHelper;
import com.cfox.camera.sessionmanager.callback.PhotoCaptureCallback;
import com.cfox.camera.sessionmanager.callback.PreviewCaptureCallback;
import com.cfox.camera.sessionmanager.req.SessionRequestManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.EsParams;


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
    private final CameraSessionManager mCameraSessionManager;
    private final ZoomHelper mZoomHelper;
    private final FocusHelper mFocusHelper;

    private CaptureRequest.Builder mCaptureBuilder;
    private CameraSession mPhotoSession;

    public PhotoSessionManagerImpl(CameraSessionManager sessionManager) {
        mCameraSessionManager = sessionManager;
        mPreviewCaptureCallback = new PreviewCaptureCallback();
        mPhotoCaptureCallback = new PhotoCaptureCallback();
        mCameraInfoManager = CameraInfoManagerImpl.CAMERA_INFO_MANAGER;
        mSessionRequestManager = new SessionRequestManager(mCameraInfoManager);
        mZoomHelper = new ZoomHelper(mCameraInfoManager);
        mFocusHelper = new FocusHelper(mCameraInfoManager);
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
        mCaptureBuilder = null;
        mSessionRequestManager.resetApply();
        mCameraSessionManager.closeSession().subscribe(new CameraObserver<EsParams>());

    }

    @Override
    public Observable<EsParams> onPreviewRepeatingRequest(final EsParams esParams) {
        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsParams> emitter) {
                mFocusHelper.init(esParams.get(EsParams.Key.PREVIEW_SIZE));
                mPreviewCaptureCallback.applyPreview(mPhotoSession, getPreviewBuilder(), emitter);
                mSessionRequestManager.applyPreviewRequest(getPreviewBuilder());
                applyPreviewRequest(getPreviewBuilder(), esParams);
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
                    return false;
                }
                return true;
            }
        });
    }

    private void applyPreviewRequest(CaptureRequest.Builder builder, EsParams esParams) {
        mSessionRequestManager.applyZoomRect(builder, mZoomHelper.getZoomRect(esParams.get(EsParams.Key.ZOOM_VALUE, 1f)));// zoom
        mSessionRequestManager.applyFlashRequest(builder, esParams.get(EsParams.Key.FLASH_STATE));// flash
        mSessionRequestManager.applyEvRange(builder, esParams.get(EsParams.Key.EV_SIZE)); // ev
    }

    private CaptureRequest.Builder getCaptureBuilder() {
        if (mCaptureBuilder == null) {
            mCaptureBuilder = createCaptureBuilder(getSurfaceManager().getReaderSurface());
        }
        return mCaptureBuilder;
    }

    private CaptureRequest.Builder createCaptureBuilder(List<Surface> surfaceList) {
        return createBuilder(CameraDevice.TEMPLATE_STILL_CAPTURE, surfaceList);
    }

    @Override
    public Observable<EsParams> onRepeatingRequest(final EsParams esParams) {

        return Observable.create(new ObservableOnSubscribe<EsParams>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<EsParams> emitter) {
                EsLog.d("onSendRepeatingRequest: req:" + esParams);
                esParams.put(EsParams.Key.REQUEST_BUILDER, getPreviewBuilder());
                esParams.put(EsParams.Key.CAPTURE_CALLBACK, mPreviewCaptureCallback);
                flashRepeatingRequest(getPreviewBuilder(), esParams);
                zoomRepeatingRequest(getPreviewBuilder(),esParams);
                evRepeatingRequest(getPreviewBuilder(), esParams);
                afTriggerRepeatingRequest(getPreviewBuilder(), esParams);
                resetFocusRepeatingRequest(getPreviewBuilder(), esParams);
            }
        });
    }

    private void resetFocusRepeatingRequest(CaptureRequest.Builder builder, EsParams esParams) {
        Boolean isResetFocus = esParams.get(EsParams.Key.RESET_FOCUS, false);
        if (isResetFocus) {
            mSessionRequestManager.applyFocusModeRequest(builder, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            mPhotoSession.onRepeatingRequest(esParams).subscribe();
        }
    }

    private void afTriggerRepeatingRequest(CaptureRequest.Builder builder, EsParams esParams) {
        Pair<Float, Float> afTriggerValue = esParams.get(EsParams.Key.AF_TRIGGER);
        if (afTriggerValue == null) {
            return;
        }
        MeteringRectangle focusRect = mFocusHelper.getAFArea(afTriggerValue.first, afTriggerValue.second);
        MeteringRectangle meterRect = mFocusHelper.getAEArea(afTriggerValue.first, afTriggerValue.second);

        mSessionRequestManager.applyTouch2FocusRequest(getPreviewBuilder(), focusRect, meterRect);
        mPhotoSession.onRepeatingRequest(esParams).subscribe();
        try {
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
            EsParams afTriggerParams = new EsParams();
            afTriggerParams.put(EsParams.Key.REQUEST_BUILDER, getPreviewBuilder());
            mPhotoSession.capture(afTriggerParams);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void flashRepeatingRequest(final CaptureRequest.Builder builder, final EsParams esParams) {
        final Integer flashState = esParams.get(EsParams.Key.FLASH_STATE);
        final Integer currFlashMode = mSessionRequestManager.getCurrFlashMode();
        if (flashState == null || currFlashMode.equals(flashState)) {
            return;
        }

        mSessionRequestManager.applyFlashRequest(builder, flashState);
        mPhotoSession.onRepeatingRequest(esParams).subscribe();
    }

    private void evRepeatingRequest(CaptureRequest.Builder builder,EsParams esParams) {
        Integer evSize = esParams.get(EsParams.Key.EV_SIZE);
        if (evSize == null) {
            return;
        }

        mSessionRequestManager.applyEvRange(builder, evSize);
        mPhotoSession.onRepeatingRequest(esParams).subscribe();
    }

    private void zoomRepeatingRequest(CaptureRequest.Builder builder,EsParams esParams) {
        Float zoomValue = esParams.get(EsParams.Key.ZOOM_VALUE);
        if (zoomValue == null) {
            return;
        }
        mSessionRequestManager.applyZoomRect(builder, mZoomHelper.getZoomRect(zoomValue));
        mPhotoSession.onRepeatingRequest(esParams).subscribe();
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
                return true;
            }
        });
    }
}
