package com.cfox.camera.capture.business;

import android.util.Size;

import com.cfox.camera.ConfigStrategy;
import com.cfox.camera.ConfigWrapper;

public abstract class BaseBusiness implements Business {
    ConfigWrapper mConfigWrapper;

    protected BaseBusiness(ConfigWrapper configWrapper) {
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

    protected ConfigStrategy getConfig() {
        return mConfigWrapper.getConfig();
    }
}
