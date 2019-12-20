package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraDevice;

import com.cfox.camera.camera.session.helper.IBuilderHelper;
import com.cfox.camera.camera.session.helper.IPhotoCameraHelper;

public class PhotoCameraHelper extends CameraHelper implements IPhotoCameraHelper {
    @Override
    public int createPreviewTemplate() {
        return CameraDevice.TEMPLATE_PREVIEW;
    }

    @Override
    public IBuilderHelper getBuilderHelper() {
        return new BuilderHelper(this);
    }

    @Override
    public int createStillCaptureTemplate() {
        return CameraDevice.TEMPLATE_STILL_CAPTURE;
    }


}
