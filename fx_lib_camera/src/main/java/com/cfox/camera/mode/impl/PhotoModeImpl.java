package com.cfox.camera.mode.impl;

import com.cfox.camera.camera.IReaderHelper;
import com.cfox.camera.camera.ImageReaderHelper;
import com.cfox.camera.camera.session.helper.PhotoSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.mode.PhotoMode;
import com.cfox.camera.mode.BaseMode;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 整理 request 和 surface 。 返回数据整理
 */
public class PhotoModeImpl extends BaseMode implements PhotoMode {
    private final PhotoSessionHelper mPhotoSessionHelper;
    private final IReaderHelper mImageReaderHelper;
    public PhotoModeImpl(PhotoSessionHelper photoSessionHelper) {
        super(photoSessionHelper);
        mPhotoSessionHelper = photoSessionHelper;
        mImageReaderHelper = new ImageReaderHelper();
    }

    @Override
    public Observable<EsResult> requestPreview(EsRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);

        surfaceHelper.addSurface(mImageReaderHelper.createImageReader(request).getSurface());
        EsLog.d("requestPreview: preview request:" + request);

        return startPreview(request);
    }

    @Override
    public void onRequestStop() {
        mImageReaderHelper.closeImageReaders();
    }

    @Override
    public Observable<EsResult> requestCapture(EsRequest request) {
        EsLog.d("requestCapture: ......");
//        int sensorOrientation = mImageSessionHelper.getSensorOrientation(request);
//        final int picOrientation = getBusiness().getPictureOrientation(sensorOrientation);
//        request.put(Es.Key.PIC_ORIENTATION, sensorOrientation);
        EsLog.d("capture: " + request);
//        mBuilderPack.preCaptureBuilder(mBuilder);
//        request.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
//        final int picOrientation = request.getInt(FxRe.Key.PIC_ORIENTATION);
        return mPhotoSessionHelper.capture(request).flatMap(new Function<EsResult, ObservableSource<EsResult>>() {
            @Override
            public ObservableSource<EsResult> apply(EsResult fxResult) throws Exception {
//                Log.d(TAG, "apply: .....222..111......"  + mImageReaders.size()  +  "   picOrientation:" + picOrientation );
                EsRequest stRequest = new EsRequest();
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
        }).flatMap(new Function<EsResult, ObservableSource<EsResult>>() {
            @Override
            public ObservableSource<EsResult> apply(EsResult fxResult) throws Exception {
                EsLog.d("apply: re requestCapture ");
//                mBuilderPack.previewCaptureBuilder(mBuilder);
                EsRequest previewRequest = new EsRequest();
//                previewRequest.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
                previewRequest.put(Es.Key.PREVIEW_CAPTURE, true);
                return mPhotoSessionHelper.capture(previewRequest);
            }
        })/*.flatMap(new Function<FxResult, ObservableSource<FxResult>>() {
            @Override
            public ObservableSource<FxResult> apply(FxResult fxResult) throws Exception {
                Log.d(TAG, "apply: re onSendRepeatingRequest");
                FxRequest previewRequest = new FxRequest();
//                mBuilderPack.previewBuilder(mBuilder);
                previewRequest.put(FxRe.Key.REQUEST_BUILDER, mBuilder);
                return onSendPreviewRepeatingRequest(previewRequest);
            }
        })*/;
    }
}
