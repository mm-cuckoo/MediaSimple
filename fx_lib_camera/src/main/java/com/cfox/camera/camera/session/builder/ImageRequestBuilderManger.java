package com.cfox.camera.camera.session.builder;

import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.camera.info.CameraInfoManagerImpl;
import com.cfox.camera.camera.session.IPhotoRequestBuilderManager;

public class ImageRequestBuilderManger extends RequestBuilderManager implements IPhotoRequestBuilderManager {


    public ImageRequestBuilderManger(CameraInfoManager cameraHelper) {
        super(cameraHelper);
    }

    @Override
    public void captureBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        builder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);
//        setExposureValue(builder, mEvValue);
    }
}
