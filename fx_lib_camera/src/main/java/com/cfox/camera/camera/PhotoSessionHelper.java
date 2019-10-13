package com.cfox.camera.camera;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;

import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;

public class PhotoSessionHelper extends AbsBaseSessionHelper {
    @Override
    public CaptureRequest.Builder createRequestBuilder(FxRequest fxRequest) throws CameraAccessException {
        CameraDevice cameraDevice = (CameraDevice) fxRequest.getObj(FxRe.Key.CAMERA_DEVICE);
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) fxRequest.getObj(FxRe.Key.SURFACE_HELPER);
        CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        builder.addTarget(surfaceHelper.getSurface());
        createSurfaces(surfaceHelper);
        return builder;
    }

    private void createSurfaces (ISurfaceHelper surfaceHelper) {
        ImageReader imageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 2);
        surfaceHelper.addSurface(imageReader.getSurface());
    }
}
