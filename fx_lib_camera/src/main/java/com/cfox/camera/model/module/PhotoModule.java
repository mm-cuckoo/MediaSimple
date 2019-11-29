package com.cfox.camera.model.module;

import android.graphics.ImageFormat;
import android.util.Log;
import android.util.Size;

import com.cfox.camera.camera.CameraInfo;
import com.cfox.camera.camera.CameraInfoHelper;
import com.cfox.camera.camera.session.helper.IPhotoSessionHelper;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public class PhotoModule extends BaseModule {
    private static final String TAG = "PhotoModule";
    private IPhotoSessionHelper mPhotoSessionHelper;
    private CameraInfo mCameraInfo;
    public PhotoModule(IPhotoSessionHelper photoSessionHelper, IBusiness business) {
        super(photoSessionHelper, business);
        mPhotoSessionHelper = photoSessionHelper;
    }

    @Override
    public Observable<FxResult> onStartPreview(FxRequest request) {
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);

        String cameraId = request.getString(FxRe.Key.CAMERA_ID);
        mCameraInfo = CameraInfoHelper.getInstance().getCameraInfo(cameraId);

        Size previewSizeForReq = (Size) request.getObj(FxRe.Key.PREVIEW_SIZE);
        Size previewSize = getBusiness().getPreviewSize(previewSizeForReq, mCameraInfo.getPreviewSize(surfaceHelper.getSurfaceClass()));
        surfaceHelper.setAspectRatio(previewSize);

        int imageFormat = request.getInt(FxRe.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        Size pictureSizeForReq = (Size) request.getObj(FxRe.Key.PIC_SIZE);
        Size pictureSize = getBusiness().getPictureSize(pictureSizeForReq, mCameraInfo.getPictureSize(imageFormat));
        request.put(FxRe.Key.PIC_SIZE, pictureSize);

        Log.d(TAG, "onStartPreview: preview width:" + previewSize.getWidth()  +
                "   preview height:" + previewSize.getHeight()  +
                "   preview size:" + previewSize);


        return startPreview(request);
    }

    @Override
    public Observable<FxResult> onCapture(FxRequest request) {
        Log.d(TAG, "onCapture: ......");
        int sensorOrientation = mCameraInfo.getSensorOrientation();
        int picOrientation = getBusiness().getPictureOrientation(sensorOrientation);
        request.put(FxRe.Key.PIC_ORIENTATION, picOrientation);
        return mPhotoSessionHelper.capture(request);
    }
}
