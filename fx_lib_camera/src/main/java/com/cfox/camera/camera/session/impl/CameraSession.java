package com.cfox.camera.camera.session.impl;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;

import androidx.annotation.NonNull;

import com.cfox.camera.EsException;
import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.camera.ICameraInfo;
import com.cfox.camera.camera.device.EsCameraDevice;
import com.cfox.camera.camera.device.IEsCameraDevice;
import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.helper.impl.PhotoSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.EasyError;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;
import com.cfox.camera.utils.ThreadHandlerManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class CameraSession implements ICameraSession {
    private CameraCaptureSession mCaptureSession;
    private IEsCameraDevice mEsCameraDevice;
    private CameraDevice mCameraDevice;
    private String mCameraId;

    public CameraSession(Context context) {
        mEsCameraDevice = EsCameraDevice.getsInstance(context);
    }

    @Override
    public Observable<EsResult> onOpenCamera(final EsRequest request) {
        // TODO: 19-12-1 check camera id
        mCameraId = request.getString(Es.Key.CAMERA_ID);
        return mEsCameraDevice.openCameraDevice(request).map(new Function<EsResult, EsResult>() {
            @Override
            public EsResult apply(EsResult result) throws Exception {
                mCameraDevice = (CameraDevice) result.getObj(Es.Key.CAMERA_DEVICE);
                ICameraInfo cameraInfo = CameraInfoHelper.getInstance().getCameraInfo(mCameraDevice.getId());
                request.put(Es.Key.CAMERA_INFO, cameraInfo);
                return result;
            }
        });
    }

    @Override
    public CaptureRequest.Builder onCreateRequestBuilder(int templateType) throws CameraAccessException {
        return mCameraDevice.createCaptureRequest(templateType);
    }

    public Observable<EsResult> onCreatePreviewSession(EsRequest request) {
        final ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);
        EsLog.d("onCreatePreviewSession: ---->" + surfaceHelper.getSurfaces().size());
        // TODO: 19-11-29 check  mCaptureSession is null
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(final ObservableEmitter<EsResult> emitter) throws Exception {
                mCameraDevice.createCaptureSession(surfaceHelper.getSurfaces(), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        EsLog.d("onConfigured: create session success .....");
                        mCaptureSession = session;
                        emitter.onNext(new EsResult());
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        emitter.onError(new EsException("Create Preview Session failed  ", EasyError.ERROR_CODE_CREATE_PREVIEW_SESSION));
                    }
                }, ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_CAMERA).getHandler());
            }
        });
    }

    @Override
    public Observable<EsResult> onRepeatingRequest(EsRequest request) {
        final CaptureRequest.Builder requestBuilder = (CaptureRequest.Builder) request.getObj(Es.Key.REQUEST_BUILDER);
        final PhotoSessionHelper.CameraCaptureSessionCallback captureCallback  =
                (PhotoSessionHelper.CameraCaptureSessionCallback) request.getObj(Es.Key.SESSION_CALLBACK);
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(ObservableEmitter<EsResult> emitter) throws Exception {
                captureCallback.setEmitter(emitter);
                mCaptureSession.setRepeatingRequest(requestBuilder.build(), captureCallback,
                        ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_CAMERA).getHandler());
            }
        });
    }

    @Override
    public Observable<EsResult> onClose() {
        EsLog.d("onClose: camera id:" + mCameraId);
        return mEsCameraDevice.closeCameraDevice(mCameraId).doOnNext(new Consumer<EsResult>() {
            @Override
            public void accept(EsResult result) throws Exception {
                EsLog.d("onClose: closeSession: .......");
                if (mCaptureSession != null) {
                    mCaptureSession.close();
                    mCaptureSession = null;
                }
            }
        });
    }

    @Override
    public void capture(EsRequest request, CameraCaptureSession.CaptureCallback captureCallback) throws CameraAccessException {
        CaptureRequest.Builder requestBuilder =
                (CaptureRequest.Builder) request.getObj(Es.Key.REQUEST_BUILDER);
        mCaptureSession.capture(requestBuilder.build(), captureCallback, null);
    }

    @Override
    public void stopRepeating() throws CameraAccessException {
        mCaptureSession.stopRepeating();
    }
}
