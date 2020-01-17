package com.cfox.camera.camera.session.helper.impl;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Range;
import android.util.Size;

import androidx.annotation.NonNull;

import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.IPhotoRequestBuilderManager;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.IPhotoCameraHelper;
import com.cfox.camera.camera.session.helper.IPhotoSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class PhotoSessionHelper extends AbsCameraSessionHelper implements IPhotoSessionHelper {

    private CaptureRequest.Builder mPreviewBuilder;

    private ICameraSession mCameraSession;
    private IPhotoCameraHelper mPhotoCameraHelper;
    private IPhotoRequestBuilderManager mRequestBuilderManager;

    public PhotoSessionHelper(ISessionManager sessionManager) {
        sessionManager.getCameraSession(1);
        mCameraSession = sessionManager.getCameraSession();
        mPhotoCameraHelper = new PhotoCameraHelper();
        mRequestBuilderManager = (IPhotoRequestBuilderManager) mPhotoCameraHelper.getBuilderHelper();
    }

    @Override
    public void openCamera(EsRequest request) {
        mCameraSession.onClose().subscribe();
    }

    @Override
    public void applyPreviewRepeatingBuilder(EsRequest request) throws CameraAccessException {
        mPreviewBuilder = createPreviewBuilder(request);
        applyBuilderSettings(request);
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        request.put(Es.Key.SESSION_CALLBACK, mPreviewCallback.setType(CameraCaptureSessionCallback.TYPE_PREVIEW));
    }

    private CaptureRequest.Builder createPreviewBuilder(EsRequest request) throws CameraAccessException {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);
        CaptureRequest.Builder builder = mCameraSession.onCreateRequestBuilder(mPhotoCameraHelper.createPreviewTemplate());
        // TODO: 2020-01-11 adjust add more target
        builder.addTarget(surfaceHelper.getSurface());
        return builder;
    }

    private void applyBuilderSettings(EsRequest request) {
        int flashValue = request.getInt(Es.Key.CAMERA_FLASH_VALUE, Es.FLASH_TYPE.OFF);
        mRequestBuilderManager.getFlashRequest(getPreviewBuilder(), flashValue);
        mRequestBuilderManager.getPreviewRequest(getPreviewBuilder());
    }

    private CaptureRequest.Builder getPreviewBuilder() {
        return mPreviewBuilder;
    }


    @Override
    public Observable<EsResult> onSendRepeatingRequest(EsRequest request) {
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilder);
        return mCameraSession.onRepeatingRequest(request);
    }

    @Override
    public Size[] getPictureSize(EsRequest request) {
        int imageFormat = request.getInt(Es.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        return mPhotoCameraHelper.getPictureSize(imageFormat);
    }

    @Override
    public Size[] getPreviewSize(EsRequest request) {
        String cameraId = request.getString(Es.Key.CAMERA_ID);
        Class klass = (Class) request.getObj(Es.Key.SURFACE_CLASS);
        mPhotoCameraHelper.initCameraInfo(getCameraInfo(cameraId));
        return mPhotoCameraHelper.getPreviewSize(klass);
    }

    @Override
    public int getSensorOrientation(EsRequest request) {
        return mPhotoCameraHelper.getSensorOrientation();
    }

    @Override
    public Observable<EsResult> close(EsRequest request) {
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(ObservableEmitter<EsResult> emitter) throws Exception {
                mCameraSession.onClose().subscribe();
            }
        });
    }

    @Override
    public Range<Integer> getEvRange(EsRequest request) {
        return mPhotoCameraHelper.getEvRange();
    }

    @Override
    public Observable<EsResult> capture(final EsRequest request) {
        EsLog.d("capture: ......3333...");
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(final ObservableEmitter<EsResult> emitter) throws Exception {
                boolean previewCapture = request.getBoolean(Es.Key.PREVIEW_CAPTURE, false);
                EsLog.d("subscribe: capture: ......3333...");

                if (previewCapture) {
                    mCameraSession.capture(request);
                    emitter.onNext(new EsResult());
                    return;
                }

//                mCaptureTime = SystemClock.elapsedRealtime();
//                mCaptureCallback.setEmitter(emitter, FLAG_CAPTURE);
                // TODO: 19-12-1
//                mCameraSession.capture(request,mCaptureCallback);
            }
        });
    }

    @Override
    public Observable<EsResult> captureStillPicture(final EsRequest request) {
        EsLog.d("captureStillPicture: .......");
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(final ObservableEmitter<EsResult> emitter) throws Exception {
                mCameraSession.stopRepeating();
                mCameraSession.capture(request);
            }
        });
    }

    @Override
    public ICameraSession getCameraSession(EsRequest request) {
        return mCameraSession;
    }

    private CameraCaptureSessionCallback mPreviewCallback = new CameraCaptureSessionCallback();

    public class CameraCaptureSessionCallback extends CameraCaptureSession.CaptureCallback {

        private static final int TYPE_PREVIEW = 1;
        private static final int TYPE_REPEAT = 2;
        private static final int TYPE_CAPTURE = 3;

        private final Object mCaptureLock = new Object();

        private ObservableEmitter<EsResult> mEmitter;
        private int mType = 0;
        private int mAFState = -1;
        private boolean mFirstFrameCompleted = false;

        CameraCaptureSessionCallback setType(int type) {
            this.mType = type;
            if (type == TYPE_PREVIEW) {
                mFirstFrameCompleted = false;
            } else if (type == TYPE_CAPTURE) {

            }
            return this;
        }


        public CameraCaptureSessionCallback setEmitter(ObservableEmitter<EsResult> emitter) {
            this.mEmitter = emitter;
            return this;
        }


        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            updateAFState(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            if (!mFirstFrameCompleted) {
                mFirstFrameCompleted = true;
                EsResult esResult = new EsResult();
                esResult.put(Es.Key.FIRST_FRAME_CALLBACK, Es.Value.OK);
                mEmitter.onNext(esResult);
                EsLog.d("preview first frame call back");
            }

            updateAFState(result);
        }

        private void updateAFState(CaptureResult captureResult) {
            Integer afState = captureResult.get(CaptureResult.CONTROL_AF_STATE);
            if (afState != null && afState != mAFState) {
                mAFState = afState;
                EsResult result = new EsResult();
                result.put(Es.Key.AF_CHANGE_STATE, afState);
                mEmitter.onNext(result);
            }
        }
    }

}
