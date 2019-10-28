package com.cfox.camera.camera.session;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cfox.camera.FxException;
import com.cfox.camera.camera.device.IFxCameraDevice;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxError;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;
import com.cfox.camera.utils.ThreadHandlerManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;

public class CameraSession implements ICameraSession {
    private static final String TAG = "CameraSession";
    CameraCaptureSession mCaptureSession;
    private boolean mFirstFrameCompleted = false;

    public Observable<FxResult> onCreatePreviewSession(FxRequest request) {
        final ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        final CameraDevice cameraDevice = (CameraDevice) request.getObj(FxRe.Key.CAMERA_DEVICE);
        Log.d(TAG, "createPreviewSession: ---->" + surfaceHelper.getSurfaces().size());
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
                        emitter.onError(new FxException("Create Preview Session failed  ",FxError.ERROR_CODE_CREATE_PREVIEW_SESSION));
                    }
                }, ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_CAMERA).getHandler());
            }
        });
    }

    @Override
    public Observable<FxResult> onPreviewRepeatingRequest(FxRequest request) {
        mFirstFrameCompleted = false;
        final CaptureRequest.Builder requestBuilder = (CaptureRequest.Builder) request.getObj(FxRe.Key.REQUEST_BUILDER);
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(final ObservableEmitter<FxResult> emitter) throws Exception {
                mCaptureSession.setRepeatingRequest(requestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                        if (!mFirstFrameCompleted) {
                            mFirstFrameCompleted = true;
                            emitter.onNext(new FxResult());
                        }
                        Log.d(TAG, "onCaptureCompleted: .....");
                    }

                    @Override
                    public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                        super.onCaptureFailed(session, request, failure);
                        Log.d(TAG, "onCaptureFailed: ....");
                    }
                }, ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_CAMERA).getHandler());

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
