package com.cfox.camera.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.util.Log;

import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;

public class PhotoSessionHelper extends AbsBaseSessionHelper {
    private static final String TAG = "PhotoSessionHelper";

    private IReaderHelper mImageReaderHelper;

    public PhotoSessionHelper() {
        mImageReaderHelper = new ImageReaderHelper();
    }

    @Override
    public CaptureRequest.Builder createRequestBuilder(FxRequest request) throws CameraAccessException {
        CameraDevice cameraDevice = (CameraDevice) request.getObj(FxRe.Key.CAMERA_DEVICE);
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        builder.addTarget(surfaceHelper.getSurface());
        createImageReaderSurfaces(request);
        return builder;
    }
    private void createImageReaderSurfaces(FxRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        ImageReader imageReader = mImageReaderHelper.createImageReader(request);
        surfaceHelper.addSurface(imageReader.getSurface());
    }

    @Override
    public void closeSession() {
        super.closeSession();
        Log.d(TAG, "closeSession: close image readers");
        mImageReaderHelper.closeImageReaders();
    }
}
