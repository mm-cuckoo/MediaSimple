package com.cfox.camera.capture.impl;


import android.graphics.ImageFormat;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.IConfigWrapper;
import com.cfox.camera.camera.info.CameraInfo;
import com.cfox.camera.camera.info.CameraInfoHelper;
import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.camera.info.CameraInfoManagerImpl;
import com.cfox.camera.capture.PhotoCapture;
import com.cfox.camera.sessionmanager.ZoomHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.mode.PhotoMode;
import com.cfox.camera.capture.business.Business;
import com.cfox.camera.capture.business.impl.PhotoBusinessImpl;
import com.cfox.camera.surface.SurfaceManager;
import com.cfox.camera.surface.SurfaceProvider;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.utils.EsParams;

// 整理发送
public class PhotoCaptureImpl implements PhotoCapture {

    private final PhotoMode mPhotoMode;
    private final CameraInfoManager mCameraInfoManager = CameraInfoManagerImpl.CAMERA_INFO_MANAGER;
    private final Business mBusiness;

    public PhotoCaptureImpl(PhotoMode photoMode, IConfigWrapper configWrapper) {
        mPhotoMode = photoMode;
        mBusiness = new PhotoBusinessImpl(configWrapper);
    }

    @Override
    public void onStartPreview(EsParams esParams, SurfaceProvider surfaceProvider) {
        SurfaceManager surfaceManager = new SurfaceManager(surfaceProvider);
        esParams.put(EsParams.Key.SURFACE_MANAGER, surfaceManager);
        // 切换Camera 信息管理中的 Camera 信息， 如前置camera  或 后置Camera
        String cameraId = esParams.get(EsParams.Key.CAMERA_ID);
        CameraInfo cameraInfo = CameraInfoHelper.getInstance().getCameraInfo(cameraId);
        mCameraInfoManager.initCameraInfo(cameraInfo);

        // 设置预览大小
        Size previewSizeForReq = esParams.get(EsParams.Key.PREVIEW_SIZE);
        Size previewSize = mBusiness.getPreviewSize(previewSizeForReq, mCameraInfoManager.getPreviewSize(surfaceManager.getPreviewSurfaceClass()));
        surfaceManager.setAspectRatio(previewSize);

        // 设置图片大小
        Size pictureSizeForReq = esParams.get(EsParams.Key.PIC_SIZE);
        int imageFormat = esParams.get(EsParams.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        Size pictureSize = mBusiness.getPictureSize(pictureSizeForReq, mCameraInfoManager.getPictureSize(imageFormat));
        esParams.put(EsParams.Key.PIC_SIZE, pictureSize);

        EsLog.d("zoom size:" + mCameraInfoManager.getMaxZoom()  + "   zoom area:" + mCameraInfoManager.getActiveArraySize());

        mPhotoMode.requestPreview(esParams).subscribe(new CameraObserver<EsParams>(){
            @Override
            public void onNext(EsParams resultParams) {
                EsLog.d("onNext: .requestPreview....");


            }
        });
    }

    @Override
    public void onCameraConfig(EsParams esParams) {

        mPhotoMode.requestCameraConfig(esParams).subscribe(new CameraObserver<EsParams>(){
            @Override
            public void onNext(EsParams resultParams) {
                EsLog.d("onNext: .requestCameraConfig....");

            }
        });
    }

    @Override
    public void onStop(EsParams esParams) {
        mPhotoMode.requestStop(esParams).subscribe(new CameraObserver<EsParams>(){
            @Override
            public void onNext(EsParams resultParams) {
                EsLog.d("onNext: .requestStop....");

            }
        });
    }

    @Override
    public Range<Integer> getEvRange() {
        return mCameraInfoManager.getEvRange();
    }

    @Override
    public Range<Float> getFocusRange() {
        return mCameraInfoManager.getFocusRange();
    }

    @Override
    public void onCapture(EsParams esParams) {
        int sensorOrientation = mCameraInfoManager.getSensorOrientation();
        int picOrientation = mBusiness.getPictureOrientation(sensorOrientation);
        esParams.put(EsParams.Key.PIC_ORIENTATION, picOrientation);
        mPhotoMode.requestCapture(esParams).subscribe(new CameraObserver<EsParams>(){
            @Override
            public void onNext(EsParams resultParams) {
                EsLog.d("onNext: .requestCapture....");

            }
        });
    }
}
