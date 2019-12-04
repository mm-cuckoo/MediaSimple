package com.cfox.camera.model.module;

import android.util.Log;
import android.util.Size;

import com.cfox.camera.camera.session.helper.ICameraSessionHelper;
import com.cfox.camera.camera.session.helper.IDulVideoSessionHelper;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public class DulVideoModule extends BaseModule {
    private static final String TAG = "DulVideoModule";
    private IDulVideoSessionHelper mDulVideoSessionHelper;
    public DulVideoModule(ICameraSessionHelper cameraSessionHelper, IBusiness business) {
        super(cameraSessionHelper, business);
    }

    @Override
    public Observable<FxResult> requestPreview(FxRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);

        request.put(FxRe.Key.SURFACE_CLASS, surfaceHelper.getSurfaceClass());

        Size previewSizeForReq = (Size) request.getObj(FxRe.Key.PREVIEW_SIZE);
        Size previewSize = getBusiness().getPreviewSize(previewSizeForReq, mDulVideoSessionHelper.getPreviewSize(request));
        surfaceHelper.setAspectRatio(previewSize);

        Log.d(TAG, "requestPreview: preview width:" + previewSize.getWidth()  +
                "   preview height:" + previewSize.getHeight()  +
                "   preview size:" + previewSize);


        return startPreview(request);
    }
}
