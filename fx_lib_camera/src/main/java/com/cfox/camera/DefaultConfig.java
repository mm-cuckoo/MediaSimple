package com.cfox.camera;

import android.util.Size;

import com.cfox.camera.log.EsLog;

import java.util.Arrays;

public class DefaultConfig implements IConfig {
    @Override
    public Size getPhotoPreviewSize(Size size, Size[] supportSizes) {
        EsLog.d("getPhotoPreviewSize: size:" + size  + "   supportSize:" + Arrays.toString(supportSizes));
        return size;
    }

    @Override
    public Size getVideoPreviewSize(Size size, Size[] supportSizes) {
        EsLog.d("getVideoPreviewSize: size:" + size  + "   supportSize:" + Arrays.toString(supportSizes));
        return size;
    }

    @Override
    public Size getPictureSize(Size size, Size[] supportSizes) {
        EsLog.d( "getPictureSize: size:" + size  + "   supportSize:" + Arrays.toString(supportSizes));
        for (Size size1 : supportSizes) {
            if (size.getWidth() <= size1.getWidth() && size.getWidth() / size.getHeight() == size1.getWidth() / size1.getHeight()) {
                size = size1;
            }
        }
        EsLog.d("getPictureSize: return picture size:" + size);
        return size;
    }

    @Override
    public int getPictureOrientation(int cameraSensorOrientation) {
        return cameraSensorOrientation;
    }
}
