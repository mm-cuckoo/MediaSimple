package com.cfox.camera.capture.business.impl;

import android.util.Size;

import com.cfox.camera.ConfigWrapper;
import com.cfox.camera.capture.business.BaseBusiness;

public class PhotoBusinessImpl extends BaseBusiness {
    public PhotoBusinessImpl(ConfigWrapper configWrapper) {
        super(configWrapper);
    }

    @Override
    public Size getPreviewSize(Size size, Size[] supportSizes) {
        return getConfig().getPreviewSize(size, supportSizes);
    }
}
