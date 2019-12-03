package com.cfox.camera.model.module;

import android.graphics.ImageFormat;
import android.util.Log;
import android.util.Size;

import com.cfox.camera.camera.session.helper.IPhotoSessionHelper;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class PhotoModule extends BaseModule {
    private static final String TAG = "PhotoModule";
    private IPhotoSessionHelper mPhotoSessionHelper;
    public PhotoModule(IPhotoSessionHelper photoSessionHelper, IBusiness business) {
        super(photoSessionHelper, business);
        mPhotoSessionHelper = photoSessionHelper;
    }

    @Override
    public Observable<FxResult> requestPreview(FxRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);

        request.put(FxRe.Key.SURFACE_CLASS, surfaceHelper.getSurfaceClass());

        Size previewSizeForReq = (Size) request.getObj(FxRe.Key.PREVIEW_SIZE);
        Size previewSize = getBusiness().getPreviewSize(previewSizeForReq, mPhotoSessionHelper.getPreviewSize(request));
        surfaceHelper.setAspectRatio(previewSize);

        Log.d(TAG, "requestPreview: preview width:" + previewSize.getWidth()  +
                "   preview height:" + previewSize.getHeight()  +
                "   preview size:" + previewSize);


        return startPreview(request);
    }

    @Override
    public Observable<FxResult> requestCapture(FxRequest request) {
        Log.d(TAG, "requestCapture: ......");
        int sensorOrientation = mPhotoSessionHelper.getSensorOrientation();
        final int picOrientation = getBusiness().getPictureOrientation(sensorOrientation);
        request.put(FxRe.Key.PIC_ORIENTATION, picOrientation);
        Log.d(TAG, "capture: " + request);
//        mBuilderPack.preCaptureBuilder(mBuilder);
//        request.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
//        final int picOrientation = request.getInt(FxRe.Key.PIC_ORIENTATION);
        return mPhotoSessionHelper.capture(request).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
//                Log.d(TAG, "apply: .....222..111......"  + mImageReaders.size()  +  "   picOrientation:" + picOrientation );
                FxRequest stRequest = new FxRequest();
//                CaptureRequest.Builder captureBuilder = mCameraSession.onCreateRequestBuilder(mPhotoCameraHelper.createStillCaptureTemplate());
//                for (ImageReader reader : mImageReaders) {
//                    Log.d(TAG, "apply:add target:width:"  + reader.getWidth()  + "  height: " + reader.getHeight()  + "  ImageFormat:" + reader.getImageFormat());
//                    captureBuilder.addTarget(reader.getSurface());
//                }
////                mBuilderPack.captureBuilder(captureBuilder);
//                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, picOrientation);
//
//                stRequest.put(FxRe.Key.REQUEST_BUILDER, captureBuilder);
                return mPhotoSessionHelper.captureStillPicture(stRequest);
            }
        }).flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                Log.d(TAG, "apply: re requestCapture ");
//                mBuilderPack.previewCaptureBuilder(mBuilder);
                FxRequest previewRequest = new FxRequest();
//                previewRequest.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
                previewRequest.put(FxRe.Key.PREVIEW_CAPTURE, true);
                return mPhotoSessionHelper.capture(previewRequest);
            }
        })/*.flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                Log.d(TAG, "apply: re onSendRepeatingRequest");
                FxRequest previewRequest = new FxRequest();
//                mBuilderPack.previewBuilder(mBuilder);
                previewRequest.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
                return sendPreviewRepeatingRequest(previewRequest);
            }
        })*/;
    }
}
