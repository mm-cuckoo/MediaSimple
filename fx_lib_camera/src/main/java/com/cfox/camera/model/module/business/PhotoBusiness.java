package com.cfox.camera.model.module.business;

import android.util.Size;

import com.cfox.camera.IConfigWrapper;

public class PhotoBusiness extends BaseBusiness {
    public PhotoBusiness(IConfigWrapper configWrapper) {
        super(configWrapper);
    }

    @Override
    public Size getPreviewSize(Size size, Size[] supportSizes) {
        return mConfigWrapper.getConfig().getPhotoPreviewSize(size, supportSizes);
    }
}
