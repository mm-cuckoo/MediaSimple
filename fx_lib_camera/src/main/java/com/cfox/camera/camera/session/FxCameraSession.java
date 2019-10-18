package com.cfox.camera.camera.session;

import android.hardware.camera2.CameraAccessException;
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
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class FxCameraSession implements IFxCameraSession {
    private static final String TAG = "FxCameraSession";
    private static FxCameraSession sInstance;
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
        final CameraDevice cameraDevice = (CameraDevice) request.getObj(FxRe.Key.CAMERA_DEVICE);
        closeSession();
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                cameraDevice.createCaptureSession(surfaceHelper.getSurfaces(), new CameraCaptureSession.StateCallback() {
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
    public Observable<FxResult> capture(final FxRequest request) {
        Log.d(TAG, "capture: ......3333...");
        final CaptureRequest.Builder requestBuilder = (CaptureRequest.Builder) request.getObj(FxRe.Key.CAPTURE_REQUEST_BUILDER);
        Log.d(TAG, "apply:   builder ..1111." + requestBuilder.hashCode());
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                Log.d(TAG, "apply:   builder ..2222." + requestBuilder.hashCode());
                mCaptureSession.capture(requestBuilder.build(), new CameraCaptureSession.CaptureCallback() {

                    void onCapture(CaptureResult result) {
                        Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                        Log.d(TAG, "onCapture: af state   " + afState);
                        if (afState == null) {
                            Log.d(TAG, "onCapture: .......next");
                            emitter.onNext(new FxResult());
                        } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState
                                || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState){
                            Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                            Log.d(TAG, "onCapture: .....ae state    " + aeState);
                            if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                                Log.d(TAG, "onCapture: ..... next 1111");
                                emitter.onNext(new FxResult());
                            }
                        }
                    }

                    @Override
                    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {

                    }

                    @Override
                    public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                        onCapture(partialResult);
                    }

                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        onCapture(result);
                    }

                    @Override
                    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                        Log.d(TAG, "onCaptureFailed: .....");
                    }
                }, null);
            }
        });
    }

    @Override
    public Observable<FxResult> captureStillPicture(FxRequest request) {
        final CaptureRequest.Builder requestBuilder = (CaptureRequest.Builder) request.getObj(FxRe.Key.CAPTURE_REQUEST_BUILDER);
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                CameraCaptureSession.CaptureCallback CaptureCallback
                        = new CameraCaptureSession.CaptureCallback() {

                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                                   @NonNull CaptureRequest request,
                                                   @NonNull TotalCaptureResult result) {
//                    Log.d(TAG, mFile.toString());
//                    unlockFocus();
                        emitter.onNext(new FxResult());
                        Log.d(TAG, "onCaptureCompleted: pic success .....");
                    }
                };

                mCaptureSession.stopRepeating();
                mCaptureSession.abortCaptures();
                mCaptureSession.capture(requestBuilder.build(), CaptureCallback, null);
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
}
