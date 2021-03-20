package com.cfox.camera.mode.impl;

import android.util.Size;

import com.cfox.camera.camera.IReaderHelper;
import com.cfox.camera.camera.ImageReaderHelper;
import com.cfox.camera.camera.session.helper.IVideoSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.mode.VideoMode;
import com.cfox.camera.mode.BaseMode;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public class VideoModeImpl extends BaseMode implements VideoMode {
    private final IVideoSessionHelper mVideoSessionHelper;
    private final IReaderHelper mImageReaderHelper;
    public VideoModeImpl(IVideoSessionHelper videoSessionHelper) {
        super(videoSessionHelper);
        mVideoSessionHelper = videoSessionHelper;
        mImageReaderHelper = new ImageReaderHelper();
    }

    @Override
    public Observable<EsResult> requestPreview(EsRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);

        Size pictureSize = (Size) request.getObj(Es.Key.PIC_SIZE);
//        Size pictureSize = getBusiness().getPictureSize(pictureSizeForReq, mImageSessionHelper.getPictureSize(request));
        request.put(Es.Key.PIC_SIZE, pictureSize);

        surfaceHelper.addSurface(mImageReaderHelper.createImageReader(request).getSurface());
        request.put(Es.Key.SURFACE_CLASS, surfaceHelper.getSurfaceClass());

        Size previewSize = (Size) request.getObj(Es.Key.PREVIEW_SIZE);
//        Size previewSize = getBusiness().getPreviewSize(previewSizeForReq, mImageSessionHelper.getPreviewSize(request));
//        surfaceHelper.setAspectRatio(previewSize);

        EsLog.d("requestPreview: preview width:" + previewSize.getWidth()  +
                "   preview height:" + previewSize.getHeight()  +
                "   preview size:" + previewSize);


        return startPreview(request);
    }
}
