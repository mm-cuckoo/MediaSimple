package com.cfox.camera.capture.business;

import android.util.Size;

import com.cfox.camera.IConfig;
import com.cfox.camera.IConfigWrapper;

public abstract class BaseBusiness implements Business {
    IConfigWrapper mConfigWrapper;

    protected BaseBusiness(IConfigWrapper configWrapper) {
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

    protected IConfig getConfig() {
        return mConfigWrapper.getConfig();
    }
}
