package com.cfox.camera.model.module.business;

import android.util.Size;

import com.cfox.camera.IConfigWrapper;

public class BaseBusiness implements IBusiness {
    IConfigWrapper mConfigWrapper;

    BaseBusiness(IConfigWrapper configWrapper) {
        this.mConfigWrapper = configWrapper;
    }

    @Override
    public Size getPreviewSize(Size size, Size[] supportSizes) {
        return null;
    }

    @Override
    public Size getPictureSize(Size size, Size[] supportSizes) {
        return mConfigWrapper.getConfig().getPictureSize(size, supportSizes);
    }

    @Override
    public int getPictureOrientation(int cameraSensorOrientation) {
        return mConfigWrapper.getConfig().getPictureOrientation(cameraSensorOrientation);
    }
}
