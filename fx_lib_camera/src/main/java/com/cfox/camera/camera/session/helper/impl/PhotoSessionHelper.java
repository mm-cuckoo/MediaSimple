package com.cfox.camera.camera.session.helper.impl;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Log;
import android.util.Range;
import android.util.Size;

import androidx.annotation.NonNull;

import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.IBuilderHelper;
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
    private static final String TAG = "PhotoSessionHelper";

    private CaptureRequest.Builder mBuilder;

    private ICameraSession mCameraSession;
    private IPhotoCameraHelper mPhotoCameraHelper;
    private IBuilderHelper mBuilderHelper;

    public PhotoSessionHelper(ISessionManager sessionManager) {
        sessionManager.getCameraSession(1);
        mCameraSession = sessionManager.getCameraSession();
        mPhotoCameraHelper = new PhotoCameraHelper();
        mBuilderHelper = mPhotoCameraHelper.getBuilderHelper();
    }

    @Override
    public void openCamera(EsRequest request) {
        mCameraSession.onClose().subscribe();
    }

    @Override
    public void applyPreviewRepeatingBuilder(EsRequest request) throws CameraAccessException {
        mBuilderHelper.clear();
        mBuilderHelper.configBuilder(request);

        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);
        mBuilder = mCameraSession.onCreateRequestBuilder(mPhotoCameraHelper.createPreviewTemplate());
        mBuilder.addTarget(surfaceHelper.getSurface());

        mBuilderHelper.previewBuilder(mBuilder);
        request.put(Es.Key.REQUEST_BUILDER, mBuilder);
    }


    @Override
    public Observable<EsResult> onSendRepeatingRequest(EsRequest request) {
        mBuilderHelper.repeatingRequestBuilder(request, mBuilder);
        request.put(Es.Key.REQUEST_BUILDER, mBuilder);
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
        EsLog.d( "capture: ......3333...");
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(final ObservableEmitter<EsResult> emitter) throws Exception {
                boolean previewCapture = request.getBoolean(Es.Key.PREVIEW_CAPTURE, false);
                EsLog.d("subscribe: capture: ......3333...");

                if (previewCapture) {
                    mCameraSession.capture(request, null);
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
        EsLog.d( "captureStillPicture: .......");
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(final ObservableEmitter<EsResult> emitter) throws Exception {
                mCameraSession.stopRepeating();
                mCameraSession.capture(request, new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                                   @NonNull CaptureRequest request,
                                                   @NonNull TotalCaptureResult result) {
                        emitter.onNext(new EsResult());
                        EsLog.d("onCaptureCompleted: pic success .....");
                    }

                    @Override
                    public void onCaptureFailed(@NonNull CameraCaptureSession session,
                                                @NonNull CaptureRequest request,
                                                @NonNull CaptureFailure failure) {
                        Log.d(TAG, "onCaptureFailed: ........." +failure);
                    }
                });
            }
        });
    }

    @Override
    public ICameraSession getCameraSession(EsRequest request) {
        return mCameraSession;
    }

}
