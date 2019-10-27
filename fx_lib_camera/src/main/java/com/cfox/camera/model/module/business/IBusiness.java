package com.cfox.camera.model.module.business;

import android.util.Size;

public interface IBusiness {

    Size getPreviewSize(Size size, Size[] supportSizes);
    Size getPictureSize(Size size, Size[] supportSizes);
    int getPictureOrientation(int cameraSensorOrientation);
}
