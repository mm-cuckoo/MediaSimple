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
import com.cfox.camera.log.EsLog;
import com.cfox.camera.mode.PhotoMode;
import com.cfox.camera.capture.business.Business;
import com.cfox.camera.capture.business.impl.PhotoBusinessImpl;
import com.cfox.camera.surface.SurfaceManager;
import com.cfox.camera.surface.SurfaceProvider;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsParams;

// 整理发送
public class PhotoCaptureImpl implements PhotoCapture {

    private final PhotoMode mPhotoMode;
    private final CameraInfoManager mCameraInfoManager = CameraInfoManagerImpl.CAMERA_INFO_MANAGER;
    private final Business mBusiness;
    private SurfaceManager mSurfaceManager;
    public PhotoCaptureImpl(PhotoMode photoMode, IConfigWrapper configWrapper) {
        mPhotoMode = photoMode;
        mBusiness = new PhotoBusinessImpl(configWrapper);
    }

    @Override
    public void onStartPreview(EsParams esParams, SurfaceProvider surfaceProvider) {
        mSurfaceManager = new SurfaceManager(surfaceProvider);
        esParams.put(Es.Key.SURFACE_MANAGER, mSurfaceManager);
        // 切换Camera 信息管理中的 Camera 信息， 如前置camera  或 后置Camera
        String cameraId = esParams.getString(Es.Key.CAMERA_ID);
        CameraInfo cameraInfo = CameraInfoHelper.getInstance().getCameraInfo(cameraId);
        mCameraInfoManager.initCameraInfo(cameraInfo);

        // 设置预览大小
        Size previewSizeForReq = (Size) esParams.getObj(Es.Key.PREVIEW_SIZE);
        Size previewSize = mBusiness.getPreviewSize(previewSizeForReq, mCameraInfoManager.getPreviewSize(mSurfaceManager.getPreviewSurfaceClass()));
        mSurfaceManager.setAspectRatio(previewSize);

        // 设置图片大小
        Size pictureSizeForReq = (Size) esParams.getObj(Es.Key.PIC_SIZE);
        int imageFormat = esParams.getInt(Es.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        Size pictureSize = mBusiness.getPictureSize(pictureSizeForReq, mCameraInfoManager.getPictureSize(imageFormat));
        esParams.put(Es.Key.PIC_SIZE, pictureSize);

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
    public Range<Integer> getEvRange(EsParams esParams) {
        return mCameraInfoManager.getEvRange();
    }

    @Override
    public void onCapture(EsParams esParams) {
        int sensorOrientation = mCameraInfoManager.getSensorOrientation();
        int picOrientation = mBusiness.getPictureOrientation(sensorOrientation);
        esParams.put(Es.Key.PIC_ORIENTATION, picOrientation);
        mPhotoMode.requestCapture(esParams).subscribe(new CameraObserver<EsParams>(){
            @Override
            public void onNext(EsParams resultParams) {
                EsLog.d("onNext: .requestCapture....");

            }
        });
    }
}
