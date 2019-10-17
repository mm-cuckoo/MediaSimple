package com.cfox.camera.camera.session;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.util.Log;

import com.cfox.camera.CameraConfig;
import com.cfox.camera.camera.IReaderHelper;
import com.cfox.camera.camera.ImageReaderHelper;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import java.util.Map;

import io.reactivex.Observable;

public class PhotoSessionHelper extends AbsBaseSessionHelper {
    private static final String TAG = "PhotoSessionHelper";

    private IReaderHelper mImageReaderHelper;
    private CaptureRequest.Builder mBuilder;

    public PhotoSessionHelper(IFxCameraSession cameraSession) {
        super(cameraSession);
        mImageReaderHelper = new ImageReaderHelper();
    }

    @Override
    public CaptureRequest.Builder createRequestBuilder(FxRequest request) throws CameraAccessException {
        CameraDevice cameraDevice = (CameraDevice) request.getObj(FxRe.Key.CAMERA_DEVICE);
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        mBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        initCameraConfig();
        mBuilder.addTarget(surfaceHelper.getSurface());
        createImageReaderSurfaces(request);
        return mBuilder;
    }

    private void initCameraConfig() {
        mBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
    }

    private void createImageReaderSurfaces(FxRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        ImageReader imageReader = mImageReaderHelper.createImageReader(request);
        surfaceHelper.addSurface(imageReader.getSurface());
    }

    @Override
    public Observable<FxResult> sendRepeatingRequest(FxRequest request) {
        configToBuilder(request);
        return super.sendRepeatingRequest(request);
    }

    private void configToBuilder(FxRequest request) {
        CameraConfig cameraConfig = (CameraConfig) request.getObj(FxRe.Key.CAMERA_CONFIG);
        request.put(FxRe.Key.CAPTURE_REQUEST_BUILDER, mBuilder);
        if (cameraConfig == null) return;
        for (Map.Entry<CaptureRequest.Key<Integer>, Integer> value : cameraConfig.getValue()) {
            Log.d(TAG, "CaptureRequest: key:" + value.getKey()  + "   value:" + value.getValue());
            mBuilder.set(value.getKey(), value.getValue());
        }
    }

    @Override
    public void closeSession() {
        super.closeSession();
        Log.d(TAG, "closeSession: close image readers");
        mImageReaderHelper.closeImageReaders();
    }
}
