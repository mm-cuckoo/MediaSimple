package com.cfox.camera.model.module;


import android.hardware.camera2.CaptureRequest;
import android.util.Log;
import android.util.Size;

import com.cfox.camera.camera.CameraInfo;
import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.camera.device.IFxCameraDevice;
import com.cfox.camera.camera.session.ISessionHelper;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;
import com.cfox.camera.utils.ThreadHandlerManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public abstract class BaseModule implements IModule {
    private static final String TAG = "BaseModule";

    private IFxCameraDevice mCameraDevice;
    private ISessionHelper mSessionHelper;

    BaseModule(IFxCameraDevice cameraDevice, ISessionHelper sessionHelper) {
        this.mCameraDevice = cameraDevice;
        this.mSessionHelper = sessionHelper;
    }

    @Override
    public Observable<FxResult> onStartPreview(final FxRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        String cameraId = request.getString(FxRe.Key.CAMERA_ID);
        int previewWidth = request.getInt(FxRe.Key.PREVIEW_WIDTH);
        int previewHeight = request.getInt(FxRe.Key.PREVIEW_HEIGHT);
        CameraInfo cameraInfo = CameraInfoHelper.getInstance().getCameraInfo(cameraId);
        Size previewSize = cameraInfo.getPreviewSize(previewWidth, previewHeight, surfaceHelper.getSurfaceClass());
        Log.d(TAG, "onStartPreview: preview width:" + previewWidth  + "   preview height:" + previewHeight  + "   preview size:" + previewSize);
        surfaceHelper.setAspectRatio(previewSize);
        return Observable.combineLatest(surfaceHelper.isAvailable(), onOpenCamera(request),
                new BiFunction<FxRequest, FxResult, FxResult>() {
                    @Override
                    public FxResult apply(FxRequest request, FxResult fxResult) throws Exception {
                        return fxResult;
                    }
                }).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
                    @Override
                    public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                        Log.d(TAG, "apply: create  session .....");
                        request.put(FxRe.Key.CAMERA_DEVICE, fxResult.getObj(FxRe.Key.CAMERA_DEVICE));
                        return mSessionHelper.createPreviewSession(request);
                    }
                }).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
                    @Override
                    public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                        Log.d(TAG, "apply: sendRepeatingRequest......");
                        CaptureRequest.Builder builder = mSessionHelper.createRequestBuilder(request);
                        request.put(FxRe.Key.CAPTURE_REQUEST_BUILDER, builder);
                        return mSessionHelper.sendRepeatingRequest(request);
                    }
                });
    }

    @Override
    public Observable<FxResult> onCameraConfig(FxRequest request) {
        return mSessionHelper.sendRepeatingRequest(request);
    }

    @Override
    public Observable<FxResult> onOpenCamera(FxRequest request) {
        return mCameraDevice.openCameraDevice(request);
    }

    @Override
    public Observable<FxResult> onCapture(FxRequest request) {
        return null;
    }

    @Override
    public Observable<FxResult> onStop() {
        return Observable.create(new ObservableOnSubscribe<FxResult>() {
            @Override
            public void subscribe(ObservableEmitter<FxResult> emitter) throws Exception {
                mSessionHelper.closeSession();
                mCameraDevice.closeCameraDevice();
            }
        }).subscribeOn(AndroidSchedulers.from(ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_OTHER).getLooper()));
    }
}

