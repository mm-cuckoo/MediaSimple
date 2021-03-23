package com.cfox.camera.mode.impl;

import android.util.Size;

import com.cfox.camera.mode.IReaderHelper;
import com.cfox.camera.mode.ImageReaderHelper;
import com.cfox.camera.helper.VideoSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.mode.VideoMode;
import com.cfox.camera.mode.BaseMode;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public class VideoModeImpl extends BaseMode implements VideoMode {
    private final VideoSessionHelper mVideoSessionHelper;
    private final IReaderHelper mImageReaderHelper;
    public VideoModeImpl(VideoSessionHelper videoSessionHelper) {
        super(videoSessionHelper);
        mVideoSessionHelper = videoSessionHelper;
        mImageReaderHelper = new ImageReaderHelper();
    }

    @Override
    public Observable<EsResult> requestPreview(EsRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);

        Size pictureSize = (Size) request.getObj(Es.Key.PIC_SIZE);
        request.put(Es.Key.PIC_SIZE, pictureSize);

        surfaceHelper.addCaptureSurface(mImageReaderHelper.createImageReader(request).getSurface());

        Size previewSize = (Size) request.getObj(Es.Key.PREVIEW_SIZE);

        EsLog.d("requestPreview: preview width:" + previewSize.getWidth()  +
                "   preview height:" + previewSize.getHeight()  +
                "   preview size:" + previewSize);


        return startPreview(request);
    }
}
