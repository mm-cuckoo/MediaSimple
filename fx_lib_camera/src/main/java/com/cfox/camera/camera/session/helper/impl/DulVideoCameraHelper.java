package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraDevice;

import com.cfox.camera.camera.session.IRequestBuilderManager;
import com.cfox.camera.camera.session.helper.IDulVideoCameraHelper;

public class DulVideoCameraHelper extends CameraHelper implements IDulVideoCameraHelper {
    @Override
    public int createPreviewTemplate() {
        return CameraDevice.TEMPLATE_PREVIEW;
    }

    @Override
    public IRequestBuilderManager getBuilderHelper() {
        return null;
    }
}
