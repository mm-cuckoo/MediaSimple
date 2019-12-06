package com.cfox.camera.model.module;

import android.util.Log;
import android.util.Size;

import com.cfox.camera.camera.IReaderHelper;
import com.cfox.camera.camera.ImageReaderHelper;
import com.cfox.camera.camera.session.helper.IPhotoSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class PhotoModule extends BaseModule {
    private static final String TAG = "PhotoModule";
    private IPhotoSessionHelper mPhotoSessionHelper;
    private IReaderHelper mImageReaderHelper;
    public PhotoModule(IPhotoSessionHelper photoSessionHelper, IBusiness business) {
        super(photoSessionHelper, business);
        mPhotoSessionHelper = photoSessionHelper;
        mImageReaderHelper = new ImageReaderHelper();
    }

    @Override
    public Observable<EsResult> requestPreview(EsRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);
        surfaceHelper.addSurface(mImageReaderHelper.createImageReader(request).getSurface());
        request.put(Es.Key.SURFACE_CLASS, surfaceHelper.getSurfaceClass());
        Size previewSizeForReq = (Size) request.getObj(Es.Key.PREVIEW_SIZE);
        Size previewSize = getBusiness().getPreviewSize(previewSizeForReq, mPhotoSessionHelper.getPreviewSize(request));
        surfaceHelper.setAspectRatio(previewSize);
        EsLog.d("requestPreview: preview width:" + previewSize.getWidth()  +
                "   preview height:" + previewSize.getHeight()  +
                "   preview size:" + previewSize);


        return startPreview(request);
    }

    @Override
    public void onRequestStop() {
        mImageReaderHelper.closeImageReaders();
    }

    @Override
    public Observable<EsResult> requestCapture(EsRequest request) {
        EsLog.d("requestCapture: ......");
        int sensorOrientation = mPhotoSessionHelper.getSensorOrientation(request);
        final int picOrientation = getBusiness().getPictureOrientation(sensorOrientation);
        request.put(Es.Key.PIC_ORIENTATION, picOrientation);
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
