package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraDevice;

import com.cfox.camera.camera.session.IRequestBuilderManager;
import com.cfox.camera.camera.session.helper.IPhotoCameraHelper;
import com.cfox.camera.camera.session.impl.PhtotoRequestBuilderManger;

public class PhotoCameraHelper extends CameraHelper implements IPhotoCameraHelper {
    @Override
    public int createPreviewTemplate() {
        return CameraDevice.TEMPLATE_PREVIEW;
    }

    @Override
    public IRequestBuilderManager getBuilderHelper() {
        return new PhtotoRequestBuilderManger(this);
    }

    @Override
    public int createStillCaptureTemplate() {
        return CameraDevice.TEMPLATE_STILL_CAPTURE;
    }


}
