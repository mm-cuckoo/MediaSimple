package com.cfox.camera.camera.session;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cfox.camera.FxException;
import com.cfox.camera.camera.CameraInfo;
import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.camera.device.FxCameraDevice;
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

public abstract class CameraSession implements ICameraSession {
    private static final String TAG = "CameraSession";
    CameraCaptureSession mCaptureSession;
    private boolean mFirstFrameCompleted = false;
    private IFxCameraDevice mFxCameraDevice;
    private CameraDevice mCameraDevice;
    private CameraInfo mCameraInfo;

    CameraSession(Context context) {
        mFxCameraDevice = FxCameraDevice.getsInstance(context);
    }

    @Override
    public Observable<FxResult> onOpenCamera(FxRequest request) {
        return mFxCameraDevice.openCameraDevice(request).map(new Function<FxResult, FxResult>() {
            @Override
            public FxResult apply(FxResult result) throws Exception {
                mCameraDevice = (CameraDevice) result.getObj(FxRe.Key.CAMERA_DEVICE);
                mCameraInfo = CameraInfoHelper.getInstance().getCameraInfo(mCameraDevice.getId());
                return result;
            }
        });
    }

    @Override
    public CaptureRequest.Builder onCreateCaptureRequest(int templateType) throws CameraAccessException {
        return mCameraDevice.createCaptureRequest(templateType);
    }

    public Observable<FxResult> onCreatePreviewSession(FxRequest request) {
        final ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        Log.d(TAG, "createPreviewSession: ---->" + surfaceHelper.getSurfaces().size());
        closeSession();
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
                            Log.d(TAG, "mFirstFrameCompleted  onCaptureCompleted: .....");
                        }
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
    public Observable<FxResult> onClose() {
        if (mCameraDevice == null) return null;
        return mFxCameraDevice.closeCameraDevice(/*mCameraDevice.getId()*/).map(new Function<FxResult, FxResult>() {
            @Override
            public FxResult apply(FxResult result) throws Exception {
                closeSession();
                return result;
            }
        });
    }

    private void closeSession() {
        Log.d(TAG, "closeSession: .......");
        if (mCaptureSession != null) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
    }

    @Override
    public boolean isAutoFocusSupported() {
        boolean autoFocusSupported = true;
        int[] afAvailableModes = mCameraInfo.getCharacteristics().get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);

        if (afAvailableModes.length == 0 || (afAvailableModes.length == 1
                && afAvailableModes[0] == CameraMetadata.CONTROL_AF_MODE_OFF)) {
            autoFocusSupported = false;
        }
        return autoFocusSupported;
    }

    @Override
    public boolean isRawSupported() {
        return false;
    }
}
