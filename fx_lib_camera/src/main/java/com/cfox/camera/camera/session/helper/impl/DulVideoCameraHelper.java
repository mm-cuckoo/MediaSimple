package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraDevice;

import com.cfox.camera.camera.session.helper.ICameraSessionHelper;
import com.cfox.camera.camera.session.helper.IDulVideoCameraHelper;
import com.cfox.camera.camera.session.helper.IPhotoCameraHelper;

public class DulVideoCameraHelper extends CameraHelper implements IDulVideoCameraHelper {
    @Override
    public int createPreviewTemplate() {
        return CameraDevice.TEMPLATE_PREVIEW;
    }
}
