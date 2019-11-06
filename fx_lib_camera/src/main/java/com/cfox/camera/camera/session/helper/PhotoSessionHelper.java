package com.cfox.camera.camera.session.helper;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.util.Log;

import com.cfox.camera.camera.IReaderHelper;
import com.cfox.camera.camera.ImageReaderHelper;
import com.cfox.camera.camera.session.IBuilderPack;
import com.cfox.camera.camera.session.IPhotoSession;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class PhotoSessionHelper extends AbsSessionHelper implements IPhotoSessionHelper{
    private static final String TAG = "PhotoSessionHelper";

    private IReaderHelper mImageReaderHelper;
    private CaptureRequest.Builder mBuilder;
    private List<ImageReader> mImageReaders = new ArrayList<>();
    private IPhotoSession mPhotoSession;
    private IBuilderPack mBuilderPack;

    public PhotoSessionHelper(IPhotoSession photoSession, IBuilderPack builderPack) {
        super(photoSession);
        mPhotoSession = photoSession;
        mBuilderPack = builderPack;
        mImageReaderHelper = new ImageReaderHelper();
    }

    @Override
    public CaptureRequest.Builder createPreviewRepeatingBuilder(FxRequest request) throws CameraAccessException {
        Log.d(TAG, "createPreviewRepeatingBuilder: "  + request);
        mBuilderPack.clear();
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        mBuilder = mPhotoSession.onCreateCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

//        ImageReader previewImageReader = mImageReaderHelper.createPreviewImageReader(request);
        ImageReader imageReader = mImageReaderHelper.createImageReader(request);

        surfaceHelper.addSurface(imageReader.getSurface());
//        surfaceHelper.addSurface(previewImageReader.getSurface());
        mBuilder.addTarget(surfaceHelper.getSurface());
//        mBuilder.addTarget(previewImageReader.getSurface());

        mBuilderPack.configToBuilder(request, mBuilder);

        mImageReaders.clear();
        mImageReaders.add(imageReader);

        return mBuilder;
    }

    @Override
    public Observable<FxResult> sendRepeatingRequest(FxRequest request) {
        mBuilderPack.configToBuilder(request, mBuilder);
        request.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
        return mPhotoSession.onSendRepeatingRequest(request);
    }

    @Override
    public Observable<FxResult> capture(final FxRequest request) {
        Log.d(TAG, "capture: " + request);
        mBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
        mBuilderPack.configToBuilder(request, mBuilder, false);
        request.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
        final int picOrientation = request.getInt(FxRe.Key.PIC_ORIENTATION);
        return mPhotoSession.onCapture(request).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                Log.d(TAG, "apply: .....222..111......"  + mImageReaders.size()  +  "   picOrientation:" + picOrientation );
                FxRequest stRequest = new FxRequest();
                CaptureRequest.Builder captureBuilder = mPhotoSession.onCreateCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                for (ImageReader reader : mImageReaders) {
                    Log.d(TAG, "apply:add target:width:"  + reader.getWidth()  + "  height: " + reader.getHeight()  + "  ImageFormat:" + reader.getImageFormat());
                    captureBuilder.addTarget(reader.getSurface());
                }
                mBuilderPack.appendPreviewBuilder(captureBuilder);
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, picOrientation);

                stRequest.put(FxRe.Key.REQUEST_BUILDER, captureBuilder);
                return mPhotoSession.onCaptureStillPicture(stRequest);
            }
        }).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                Log.d(TAG, "apply: re onCapture ");
                mBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
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

    @Override
    public Observable<FxResult> close() {
        return super.close().map(new Function<FxResult, FxResult>() {
            @Override
            public FxResult apply(FxResult result) throws Exception {
                Log.d(TAG, "closeSession: close image readers");
                mImageReaderHelper.closeImageReaders();
                return result;
            }
        });
    }
}
