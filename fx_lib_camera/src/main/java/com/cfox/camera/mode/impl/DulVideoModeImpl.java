package com.cfox.camera.mode.impl;

import android.util.Size;

import com.cfox.camera.helper.DulVideoSessionHelper;
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

        Size pictureSize = (Size) request.getObj(Es.Key.PIC_SIZE);
        request.put(Es.Key.PIC_SIZE, pictureSize);
        Size previewSize = (Size) request.getObj(Es.Key.PREVIEW_SIZE);
        EsLog.d("requestPreview: preview width:" + previewSize.getWidth()  +
                "   preview height:" + previewSize.getHeight()  +
                "   preview size:" + previewSize);


        return startPreview(request);
    }
}
