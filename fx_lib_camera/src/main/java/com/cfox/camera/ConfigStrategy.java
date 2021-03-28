package com.cfox.camera;

import android.util.Size;

public interface ConfigStrategy {

    Size getPreviewSize(Size size, Size[] supportSizes);

    Size getPictureSize(Size size, Size[] supportSizes);

    int getPictureOrientation(int cameraSensorOrientation);
}
