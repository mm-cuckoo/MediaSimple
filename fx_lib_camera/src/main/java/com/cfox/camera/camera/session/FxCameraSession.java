package com.cfox.camera.camera.session;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class FxCameraSession implements IFxCameraSession {
    private static final String TAG = "FxCameraSession";
    private static FxCameraSession sInstance;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;

    public static FxCameraSession getsInstance() {
        if (sInstance == null) {
            synchronized (FxCameraSession.class) {
                if (sInstance == null) {
                    sInstance = new FxCameraSession();
                }
            }
        }
        return sInstance;
    }

    @Override
    public Observable<FxResult> createPreviewSession(FxRequest request) {
        final ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        mCameraDevice = (CameraDevice) request.getObj(FxRe.Key.CAMERA_DEVICE);
        closeSession();
        checkDeviceUNLL();
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                mCameraDevice.createCaptureSession(surfaceHelper.getSurfaces(), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        Log.d(TAG, "onConfigured: create session success .....");
                        mCaptureSession = session;
                        emitter.onNext(new FxResult());
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                }, null);
            }
        });
    }

    public Observable<FxResult> sendRepeatingRequest(FxRequest request) {
        final CaptureRequest.Builder requestBuilder = (CaptureRequest.Builder) request.getObj(FxRe.Key.CAPTURE_REQUEST_BUILDER);
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                mCaptureSession.setRepeatingRequest(requestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                        super.onCaptureStarted(session, request, timestamp, frameNumber);
                    }

                    @Override
                    public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                        super.onCaptureProgressed(session, request, partialResult);
                    }

                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                    }

                    @Override
                    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                        super.onCaptureFailed(session, request, failure);
                    }
                }, null);
            }
        });

    }

    @Override
    public Observable<FxRequest> capture(final FxRequest request) {
        final CaptureRequest.Builder requestBuilder = (CaptureRequest.Builder) request.getObj(FxRe.Key.CAPTURE_REQUEST_BUILDER);
        return Observable.create(new ObservableOnSubscribe<FxRequest>() {
            @Override
            public void subscribe(ObservableEmitter<FxRequest> emitter) throws Exception {
                mCaptureSession.capture(requestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {

                    }

                    @Override
                    public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {

                    }

                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {

                    }

                    @Override
                    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {

                    }
                }, null);
            }
        });
    }

    @Override
    public void closeSession() {
        Log.d(TAG, "closeSession: .......");
        if (mCaptureSession != null) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
    }

    private void checkDeviceUNLL() {
        if (mCameraDevice == null) {
            throw new RuntimeException("FxCameraSessionImpl CameraDevice is null , " +
                    "place use FxCameraSession setCameraDevice(CameraDevice cameraDevice) method set CameraDevice !!!!!!!");
        }
    }
}
