package com.cfox.camera.camera.session.impl;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cfox.camera.FxException;
import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.camera.ICameraInfo;
import com.cfox.camera.camera.device.FxCameraDevice;
import com.cfox.camera.camera.device.IFxCameraDevice;
import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxError;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;
import com.cfox.camera.utils.ThreadHandlerManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class CameraSession implements ICameraSession {
    private static final String TAG = "CameraSession";
    private CameraCaptureSession mCaptureSession;
    private IFxCameraDevice mFxCameraDevice;
    private CameraDevice mCameraDevice;
    private String mCameraId;

    public CameraSession(Context context) {
        mFxCameraDevice = FxCameraDevice.getsInstance(context);
    }

    @Override
    public Observable<FxResult> onOpenCamera(final FxRequest request) {
        // TODO: 19-12-1 check camera id
        mCameraId = request.getString(FxRe.Key.CAMERA_ID);
        return mFxCameraDevice.openCameraDevice(request).map(new Function<FxResult, FxResult>() {
            @Override
            public FxResult apply(FxResult result) throws Exception {
              ICameraInfo cameraInfo = CameraInfoHelper.getInstance().getCameraInfo(mCameraDevice.getId());
              mCameraDevice = (CameraDevice) result.getObj(FxRe.Key.CAMERA_DEVICE);
              request.put(FxRe.Key.CAMERA_INFO, cameraInfo);
              return result;
            }
        });
    }

    @Override
    public CaptureRequest.Builder onCreateRequestBuilder(int templateType) throws CameraAccessException {
        return mCameraDevice.createCaptureRequest(templateType);
    }

    public Observable<FxResult> onCreatePreviewSession(FxRequest request) {
        final ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        Log.d(TAG, "createPreviewSession: ---->" + surfaceHelper.getSurfaces().size());
        // TODO: 19-11-29 check  mCaptureSession is null
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
                        emitter.onError(new FxException("Create Preview Session failed  ",FxError.ERROR_CODE_CREATE_PREVIEW_SESSION));
                    }
                }, ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_CAMERA).getHandler());
            }
        });
    }

    @Override
    public Observable<FxResult> onRepeatingRequest(FxRequest request) {
        final CaptureRequest.Builder requestBuilder = (CaptureRequest.Builder) request.getObj(FxRe.Key.REQUEST_BUILDER);
        final CameraCaptureSession.CaptureCallback captureCallback  = (CameraCaptureSession.CaptureCallback) request.getObj(FxRe.Key.SESSION_CAPTURE_CALLBACK);
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                mCaptureSession.setRepeatingRequest(requestBuilder.build(), captureCallback,
                        ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_CAMERA).getHandler());
            }
        });
    }

    @Override
    public Observable<FxResult> onClose() {
        return mFxCameraDevice.closeCameraDevice(mCameraId).doOnNext(new Consumer<FxResult>() {
            @Override
            public void accept(FxResult result) throws Exception {
                Log.d(TAG, "closeSession: .......");
                if (mCaptureSession != null) {
                    mCaptureSession.close();
                    mCaptureSession = null;
                }
            }
        });
    }

    @Override
    public void capture(FxRequest request, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException {
        CaptureRequest.Builder requestBuilder =
                (CaptureRequest.Builder) request.getObj(FxRe.Key.REQUEST_BUILDER);
        mCaptureSession.capture(requestBuilder.build(), captureCallback, null);
    }

    @Override
    public void stopRepeating() throws CameraAccessException {
        mCaptureSession.stopRepeating();
    }
}
