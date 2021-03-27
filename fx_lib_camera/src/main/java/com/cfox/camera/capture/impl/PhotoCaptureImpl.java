package com.cfox.camera.capture.impl;


import android.graphics.ImageFormat;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.IConfigWrapper;
import com.cfox.camera.request.PreviewRequest;
import com.cfox.camera.camera.info.CameraInfo;
import com.cfox.camera.camera.info.CameraInfoHelper;
import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.camera.info.CameraInfoManagerImpl;
import com.cfox.camera.capture.CaptureStateListener;
import com.cfox.camera.capture.PhotoCapture;
import com.cfox.camera.capture.PreviewStateListener;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.mode.PhotoMode;
import com.cfox.camera.capture.business.Business;
import com.cfox.camera.capture.business.impl.PhotoBusinessImpl;
import com.cfox.camera.request.RepeatRequest;
import com.cfox.camera.surface.SurfaceManager;
import com.cfox.camera.surface.SurfaceProvider;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.utils.EsParams;

import io.reactivex.annotations.NonNull;

public class PhotoCaptureImpl implements PhotoCapture {

    private final PhotoMode mPhotoMode;
    private final CameraInfoManager mCameraInfoManager;
    private final Business mBusiness;

    public PhotoCaptureImpl(PhotoMode photoMode, IConfigWrapper configWrapper) {
        mPhotoMode = photoMode;
        mBusiness = new PhotoBusinessImpl(configWrapper);
        mCameraInfoManager = CameraInfoManagerImpl.CAMERA_INFO_MANAGER;
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
            public void onNext(@NonNull EsParams resultParams) {
                EsLog.d("onNext: .requestPreview...." + resultParams);


            }
        });
    }

    @Override
    public void onStartPreview(PreviewRequest request, PreviewStateListener listener) {
        SurfaceManager surfaceManager = new SurfaceManager(request.getSurfaceProvider());
        EsParams esParams = new EsParams();
        esParams.put(EsParams.Key.SURFACE_MANAGER, surfaceManager);
        esParams.put(EsParams.Key.CAMERA_ID, request.getCameraId());
        esParams.put(EsParams.Key.FLASH_STATE, request.getFlashState());

        // 切换Camera 信息管理中的 Camera 信息， 如前置camera  或 后置Camera
        CameraInfo cameraInfo = CameraInfoHelper.getInstance().getCameraInfo(request.getCameraId());
        mCameraInfoManager.initCameraInfo(cameraInfo);

        // 设置预览大小
        Size previewSizeForReq = request.getPreviewSize();
        Size previewSize = mBusiness.getPreviewSize(previewSizeForReq, mCameraInfoManager.getPreviewSize(surfaceManager.getPreviewSurfaceClass()));
        esParams.put(EsParams.Key.PREVIEW_SIZE, previewSize);
        surfaceManager.setAspectRatio(previewSize);

        // 设置图片大小
        Size pictureSizeForReq = request.getPictureSize();
        int imageFormat = request.getImageFormat();
        Size pictureSize = mBusiness.getPictureSize(pictureSizeForReq, mCameraInfoManager.getPictureSize(imageFormat));
        esParams.put(EsParams.Key.PIC_SIZE, pictureSize);

        EsLog.d("zoom size:" + mCameraInfoManager.getMaxZoom()  + "   zoom area:" + mCameraInfoManager.getActiveArraySize());

        mPhotoMode.requestPreview(esParams).subscribe(new CameraObserver<EsParams>(){
            @Override
            public void onNext(@NonNull EsParams resultParams) {
                EsLog.d("onNext: .requestPreview...." + resultParams);


            }
        });
    }

    @Override
    public void onCameraRepeating(EsParams esParams) {

        mPhotoMode.requestCameraRepeating(esParams).subscribe(new CameraObserver<EsParams>(){
            @Override
            public void onNext(@NonNull EsParams resultParams) {
                EsLog.d("onNext: .requestCameraConfig....");

            }
        });
    }

    @Override
    public void onCameraRepeating(RepeatRequest request) {
        EsParams esParams = new EsParams();
        Float zoomSize = request.getZoomSize();
        if (zoomSize != null) {
            esParams.put(EsParams.Key.ZOOM_VALUE, zoomSize);
        }

        Integer flash = request.getFlashState();
        if (flash != null) {
            esParams.put(EsParams.Key.FLASH_STATE, flash);
        }

        Integer ev = request.getEv();
        if (ev != null) {
            esParams.put(EsParams.Key.EV_SIZE, ev);
        }

        mPhotoMode.requestCameraRepeating(esParams).subscribe(new CameraObserver<EsParams>());
    }

    @Override
    public void onStop() {
        EsParams esParams = new EsParams();
        mPhotoMode.requestStop(esParams).subscribe(new CameraObserver<EsParams>());
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
    public void onCapture(CaptureStateListener listener) {
        EsParams esParams = new EsParams();
        int sensorOrientation = mCameraInfoManager.getSensorOrientation();
        int picOrientation = mBusiness.getPictureOrientation(sensorOrientation);
        esParams.put(EsParams.Key.PIC_ORIENTATION, picOrientation);
        mPhotoMode.requestCapture(esParams).subscribe(new CameraObserver<EsParams>(){
            @Override
            public void onNext(@NonNull EsParams resultParams) {
                EsLog.d("onNext: .requestCapture...." + resultParams);
            }
        });
    }
}
