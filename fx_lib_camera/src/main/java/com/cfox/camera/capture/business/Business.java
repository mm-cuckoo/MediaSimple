package com.cfox.camera.capture.business;

import android.util.Size;

public interface Business {

    Size getPreviewSize(Size size, Size[] supportSizes);
    Size getPictureSize(Size size, Size[] supportSizes);
    int getPictureOrientation(int cameraSensorOrientation);
}
