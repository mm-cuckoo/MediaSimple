package com.cfox.camera.camera.session.helper;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.util.Log;

import com.cfox.camera.CameraConfig;
import com.cfox.camera.camera.IReaderHelper;
import com.cfox.camera.camera.ImageReaderHelper;
import com.cfox.camera.camera.session.IPhotoSession;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class PhotoSessionHelper extends AbsSessionHelper implements IPhotoSessionHelper{
    private static final String TAG = "PhotoSessionHelper";

    private IReaderHelper mImageReaderHelper;
    private CaptureRequest.Builder mBuilder;
    private CameraDevice mCameraDevice;
    private List<ImageReader> mImageReaders = new ArrayList<>();
    private IPhotoSession mPhotoSession;

    public PhotoSessionHelper(IPhotoSession photoSession) {
        super(photoSession);
        mPhotoSession = photoSession;
        mImageReaderHelper = new ImageReaderHelper();
    }

    @Override
    public CaptureRequest.Builder createPreviewRepeatingBuilder(FxRequest request) throws CameraAccessException {
        Log.d(TAG, "createPreviewRepeatingBuilder: "  + request);
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        mCameraDevice = (CameraDevice) request.getObj(FxRe.Key.CAMERA_DEVICE);
        mBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

        ImageReader previewImageReader = mImageReaderHelper.createPreviewImageReader(request);
        ImageReader imageReader = mImageReaderHelper.createImageReader(request);

        surfaceHelper.addSurface(imageReader.getSurface());
        surfaceHelper.addSurface(previewImageReader.getSurface());
        mBuilder.addTarget(surfaceHelper.getSurface());
        mBuilder.addTarget(previewImageReader.getSurface());

        initCameraConfig();

        mImageReaders.clear();
        mImageReaders.add(imageReader);

        return mBuilder;
    }

    private void initCameraConfig() {
        mBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        mBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
    }

    @Override
    public Observable<FxResult> sendRepeatingRequest(FxRequest request) {
        configToBuilder(request);
        return mPhotoSession.onSendRepeatingRequest(request);
    }

    @Override
    public Observable<FxResult> capture(final FxRequest request) {
        Log.d(TAG, "capture: " + request);
        mBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
        request.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
        final int picOrientation = request.getInt(FxRe.Key.PIC_ORIENTATION);
        return mPhotoSession.onCapture(request).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                Log.d(TAG, "apply: .....222..111......"  + mImageReaders.size()  +  "   picOrientation:" + picOrientation );
                FxRequest stRequest = new FxRequest();
                CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                for (ImageReader reader : mImageReaders) {
                    Log.d(TAG, "apply:add target:width:"  + reader.getWidth()  + "  height: " + reader.getHeight()  + "  ImageFormat:" + reader.getImageFormat());
                    captureBuilder.addTarget(reader.getSurface());
                }
                captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, picOrientation);

                stRequest.put(FxRe.Key.REQUEST_BUILDER, captureBuilder);
                return mPhotoSession.onCaptureStillPicture(stRequest);
            }
        }).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                Log.d(TAG, "apply: re onCapture ");
                mBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
                mBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                FxRequest previewRequest = new FxRequest();
                previewRequest.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
                previewRequest.put(FxRe.Key.PREVIEW_CAPTURE, true);
                return mPhotoSession.onCapture(previewRequest);
            }
        }).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                Log.d(TAG, "apply: re onSendRepeatingRequest");
                FxRequest previewRequest = new FxRequest();
                previewRequest.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
                return mPhotoSession.onSendRepeatingRequest(previewRequest);
            }
        });
    }

    private void configToBuilder(FxRequest request) {
        CameraConfig cameraConfig = (CameraConfig) request.getObj(FxRe.Key.CAMERA_CONFIG);
        request.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
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
