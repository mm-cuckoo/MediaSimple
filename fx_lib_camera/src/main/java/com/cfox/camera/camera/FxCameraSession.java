package com.cfox.camera.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Log;
import android.view.Surface;

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
    private CaptureRequest.Builder mRequestBuilder;

    static FxCameraSession getsInstance() {
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
    public Observable<FxResult> createPreviewSession(FxRequest fxRequest) {
        final ISurfaceHelper surfaceHelper = (ISurfaceHelper) fxRequest.getObj(FxRe.Key.SURFACE_HELPER);
        mCameraDevice = (CameraDevice) fxRequest.getObj(FxRe.Key.CAMERA_DEVICE);
        mRequestBuilder = (CaptureRequest.Builder) fxRequest.getObj(FxRe.Key.PREVIEW_BUILDER);
        closeSession();
        checkDeviceUNLL();
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                mCameraDevice.createCaptureSession(surfaceHelper.getSurfaces(), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        mCaptureSession = session;
                        try {
                            sendRepeatingRequest(emitter);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                }, null);
            }
        });
    }

    private void sendRepeatingRequest(ObservableEmitter<FxResult> emitter) throws CameraAccessException {
        mCaptureSession.setRepeatingRequest(mRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
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

            @Override
            public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
                super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);
            }

            @Override
            public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
                super.onCaptureSequenceAborted(session, sequenceId);
            }

            @Override
            public void onCaptureBufferLost(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull Surface target, long frameNumber) {
                super.onCaptureBufferLost(session, request, target, frameNumber);
            }
        }, null);
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
