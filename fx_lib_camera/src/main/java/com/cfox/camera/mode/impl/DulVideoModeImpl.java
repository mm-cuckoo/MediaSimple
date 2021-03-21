package com.cfox.camera.mode.impl;

import android.util.Size;

import com.cfox.camera.camera.session.helper.DulVideoSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.mode.BaseMode;
import com.cfox.camera.mode.DulVideoMode;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public class DulVideoModeImpl extends BaseMode implements DulVideoMode {
    private static final String TAG = "DulVideoModule";
    private final DulVideoSessionHelper mDulVideoSessionHelper;
    public DulVideoModeImpl(DulVideoSessionHelper cameraSessionHelper) {
        super(cameraSessionHelper);
        mDulVideoSessionHelper = cameraSessionHelper;
    }

    @Override
    public Observable<EsResult> requestPreview(EsRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);

        Size pictureSize = (Size) request.getObj(Es.Key.PIC_SIZE);
//        Size pictureSize = getBusiness().getPictureSize(pictureSizeForReq, mImageSessionHelper.getPictureSize(request));
        request.put(Es.Key.PIC_SIZE, pictureSize);

//        surfaceHelper.addSurface(mImageReaderHelper.createImageReader(request).getSurface());
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
