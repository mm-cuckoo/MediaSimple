package com.cfox.camera.camera.session.helper;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader;
import android.util.Log;

import androidx.annotation.NonNull;

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
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class PhotoSessionHelper extends AbsSessionHelper implements IPhotoSessionHelper {
    private static final String TAG = "PhotoSessionHelper";

    private IReaderHelper mImageReaderHelper;
    private CaptureRequest.Builder mBuilder;
    private List<ImageReader> mImageReaders = new ArrayList<>();
    private IPhotoSession mPhotoSession;
    private IBuilderPack mBuilderPack;

    public PhotoSessionHelper(IPhotoSession photoSession) {
        super(photoSession);
        mPhotoSession = photoSession;
        mBuilderPack = photoSession.getBuilderPack();
        mImageReaderHelper = new ImageReaderHelper();
    }

    @Override
    public CaptureRequest.Builder createPreviewRepeatingBuilder(FxRequest request) throws CameraAccessException {
        Log.d(TAG, "createPreviewRepeatingBuilder: "  + request);
        mBuilderPack.clear();
        mImageReaders.clear();
        mBuilderPack.configBuilder(request);
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        mBuilder = mPhotoSession.onCreateCaptureRequest(mPhotoSession.createPreviewTemplate());

//        ImageReader previewImageReader = mImageReaderHelper.createPreviewImageReader(request);
        ImageReader imageReader = mImageReaderHelper.createImageReader(request);

        surfaceHelper.addSurface(imageReader.getSurface());
//        surfaceHelper.addSurface(previewImageReader.getSurface());
        mBuilder.addTarget(surfaceHelper.getSurface());
//        mBuilder.addTarget(previewImageReader.getSurface());

        mBuilderPack.previewBuilder(mBuilder);
        mImageReaders.add(imageReader);

        return mBuilder;
    }

    @Override
    public Observable<FxResult> sendRepeatingRequest(FxRequest request) {
        mBuilderPack.repeatingRequestBuilder(request, mBuilder);
        request.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
        return mPhotoSession.onSendRepeatingRequest(request);
    }

    @Override
    public Observable<FxResult> capture(final FxRequest request) {
        Log.d(TAG, "capture: " + request);
        mBuilderPack.preCaptureBuilder(mBuilder);
        request.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
        final int picOrientation = request.getInt(FxRe.Key.PIC_ORIENTATION);
        return mPhotoSession.onCapture(request).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                Log.d(TAG, "apply: .....222..111......"  + mImageReaders.size()  +  "   picOrientation:" + picOrientation );
                FxRequest stRequest = new FxRequest();
                CaptureRequest.Builder captureBuilder = mPhotoSession.onCreateCaptureRequest(mPhotoSession.createStillCaptureTemplate());
                for (ImageReader reader : mImageReaders) {
                    Log.d(TAG, "apply:add target:width:"  + reader.getWidth()  + "  height: " + reader.getHeight()  + "  ImageFormat:" + reader.getImageFormat());
                    captureBuilder.addTarget(reader.getSurface());
                }
                mBuilderPack.captureBuilder(captureBuilder);
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, picOrientation);

                stRequest.put(FxRe.Key.REQUEST_BUILDER, captureBuilder);
                return mPhotoSession.onCaptureStillPicture(stRequest);
            }
        }).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                Log.d(TAG, "apply: re onCapture ");
                mBuilderPack.previewCaptureBuilder(mBuilder);
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
                mBuilderPack.previewBuilder(mBuilder);
                previewRequest.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
                return sendPreviewRepeatingRequest(previewRequest);
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

    public class CaptureCallback extends CameraCaptureSession.CaptureCallback {
        private ObservableEmitter<FxResult> mEmitter;
        public void setEmitter(ObservableEmitter<FxResult> emitter) {
            this.mEmitter = emitter;
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }
    }

}
