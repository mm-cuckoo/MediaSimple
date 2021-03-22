package com.cfox.camera.mode.impl;

import com.cfox.camera.mode.IReaderHelper;
import com.cfox.camera.mode.ImageReaderHelper;
import com.cfox.camera.helper.PhotoSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.mode.PhotoMode;
import com.cfox.camera.mode.BaseMode;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

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

        surfaceHelper.addCaptureSurface(mImageReaderHelper.createImageReader(request).getSurface());
        EsLog.d("requestPreview: preview request:" + request);

        return startPreview(request);
    }

    @Override
    public void onRequestStop() {
        mImageReaderHelper.closeImageReaders();
    }

    @Override
    public Observable<EsResult> requestCapture(EsRequest request) {
        EsLog.d("requestCapture: ......" + request);
        return mPhotoSessionHelper.capture(request);
    }
}
