package com.cfox.camera.model.module;

import android.util.Log;
import android.util.Size;

import com.cfox.camera.camera.session.helper.IDulVideoSessionHelper;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public class DulVideoModule extends BaseModule {
    private static final String TAG = "DulVideoModule";
    private IDulVideoSessionHelper mDulVideoSessionHelper;
    public DulVideoModule(IDulVideoSessionHelper cameraSessionHelper, IBusiness business) {
        super(cameraSessionHelper, business);
        mDulVideoSessionHelper = cameraSessionHelper;
    }

    @Override
    public Observable<EsResult> requestPreview(EsRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);

        request.put(Es.Key.SURFACE_CLASS, surfaceHelper.getSurfaceClass());

        Size previewSizeForReq = (Size) request.getObj(Es.Key.PREVIEW_SIZE);
        Size previewSize = getBusiness().getPreviewSize(previewSizeForReq, mDulVideoSessionHelper.getPreviewSize(request));
        Log.d(TAG, "requestPreview: " + previewSize);
        surfaceHelper.setAspectRatio(previewSize);

        Log.d(TAG, "requestPreview: preview width:" + previewSize.getWidth()  +
                "   preview height:" + previewSize.getHeight()  +
                "   preview size:" + previewSize);


        return startPreview(request);
    }
}
