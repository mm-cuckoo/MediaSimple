package com.cfox.camera;

import android.util.Log;
import android.util.Size;

import java.util.Arrays;

public class DefaultConfig implements IConfig {
    private static final String TAG = "DefaultConfig";
    @Override
    public Size getPhotoPreviewSize(Size size, Size[] supportSizes) {
        Log.d(TAG, "getPhotoPreviewSize: size:" + size  + "   supportSize:" + Arrays.toString(supportSizes));
        return size;
    }

    @Override
    public Size getVideoPreviewSize(Size size, Size[] supportSizes) {
        Log.d(TAG, "getVideoPreviewSize: size:" + size  + "   supportSize:" + Arrays.toString(supportSizes));
        return size;
    }

    @Override
    public Size getPictureSize(Size size, Size[] supportSizes) {
        Log.d(TAG, "getPictureSize: size:" + size  + "   supportSize:" + Arrays.toString(supportSizes));
        for (Size size1 : supportSizes) {
            if (size.getWidth() <= size1.getWidth() && size.getWidth() / size.getHeight() == size1.getWidth() / size1.getHeight()) {
                size = size1;
            }
        }
        Log.d(TAG, "getPictureSize: return picture size:" + size);
        return size;
    }

    @Override
    public int getPictureOrientation(int cameraSensorOrientation) {
        return cameraSensorOrientation;
    }
}
