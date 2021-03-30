package com.cfox.camera.capture.business.impl;

import android.util.Size;

import com.cfox.camera.ConfigWrapper;
import com.cfox.camera.capture.business.BaseBusiness;

public class VideoBusinessImpl extends BaseBusiness {
    public VideoBusinessImpl(ConfigWrapper configWrapper) {
        super(configWrapper);
    }

    @Override
    public Size getPreviewSize(Size size, Size[] supportSizes) {
        return getConfig().getPreviewSize(size, supportSizes);
    }
}
