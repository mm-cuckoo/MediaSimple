package com.cfox.camera;

import android.util.Size;

public interface IConfig {

    Size getPhotoPreviewSize(Size size, Size[] supportSizes);

    Size getVideoPreviewSize(Size size, Size[] supportSizes);

    Size getPictureSize(Size size, Size[] supportSizes);

    int getPictureOrientation(int cameraSensorOrientation);
}
