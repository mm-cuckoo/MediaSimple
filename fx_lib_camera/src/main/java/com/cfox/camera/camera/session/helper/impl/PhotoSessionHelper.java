package com.cfox.camera.camera.session.helper.impl;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;

import com.cfox.camera.camera.CameraInfo;
import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.camera.ICameraInfo;
import com.cfox.camera.camera.IReaderHelper;
import com.cfox.camera.camera.ImageReaderHelper;
import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.IPhotoCameraHelper;
import com.cfox.camera.camera.session.helper.IPhotoSessionHelper;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;



import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class PhotoSessionHelper extends AbsCameraSessionHelper implements IPhotoSessionHelper {
    private static final String TAG = "PhotoSessionHelper";

    private IReaderHelper mImageReaderHelper;
    private CaptureRequest.Builder mBuilder;

    private ICameraSession mCameraSession;
    private IPhotoCameraHelper mPhotoCameraHelper;

    public PhotoSessionHelper(ISessionManager sessionManager) {
        sessionManager.getCameraSession(1);
        mCameraSession = sessionManager.getCameraSession();
        mImageReaderHelper = new ImageReaderHelper();
        mPhotoCameraHelper = new PhotoCameraHelper();
    }

    @Override
    public void openCamera(FxRequest request) {
        mCameraSession.onClose().subscribe();
    }

    @Override
    public void applyPreviewRepeatingBuilder(FxRequest request) throws CameraAccessException {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        mBuilder = mCameraSession.onCreateRequestBuilder(mPhotoCameraHelper.createPreviewTemplate());

        ImageReader imageReader = mImageReaderHelper.createImageReader(request);

        surfaceHelper.addSurface(imageReader.getSurface());
        mBuilder.addTarget(surfaceHelper.getSurface());
        request.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
    }


    @Override
    public Observable<FxResult> onSendRepeatingRequest(FxRequest request) {
        request.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
        return mCameraSession.onRepeatingRequest(request);
    }

    @Override
    public Size[] getPictureSize(FxRequest request) {
        int imageFormat = request.getInt(FxRe.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        return mPhotoCameraHelper.getPictureSize(imageFormat);
    }

    @Override
    public Size[] getPreviewSize(FxRequest request) {
        String cameraId = request.getString(FxRe.Key.CAMERA_ID);
        Class klass = (Class) request.getObj(FxRe.Key.SURFACE_CLASS);
        mPhotoCameraHelper.initCameraInfo(getCameraInfo(cameraId));
        return mPhotoCameraHelper.getPreviewSize(klass);
    }

    @Override
    public int getSensorOrientation() {
        return mPhotoCameraHelper.getSensorOrientation();
    }

    @Override
    public Observable<FxResult> close() {
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                mCameraSession.onClose().subscribe();
                mImageReaderHelper.closeImageReaders();
            }
        });
    }


    @Override
    public Observable<FxResult> capture(final FxRequest request) {
        Log.d(TAG, "capture: ......3333...");
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                boolean previewCapture = request.getBoolean(FxRe.Key.PREVIEW_CAPTURE, false);
                Log.d(TAG, "subscribe: capture: ......3333...");

                if (previewCapture) {
                    mCameraSession.capture(request, null);
                    emitter.onNext(new FxResult());
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
    public Observable<FxResult> captureStillPicture(final FxRequest request) {
        Log.d(TAG, "captureStillPicture: .......");
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                mCameraSession.stopRepeating();
//                mCaptureSession.abortCaptures();
                mCameraSession.capture(request, new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                                   @NonNull CaptureRequest request,
                                                   @NonNull TotalCaptureResult result) {
                        emitter.onNext(new FxResult());
                        Log.d(TAG, "onCaptureCompleted: pic success .....");
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
    public ICameraSession getCameraSession(FxRequest request) {
        return mCameraSession;
    }

}
