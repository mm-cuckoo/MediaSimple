package com.cfox.camera.camera;

import android.hardware.camera2.CameraCharacteristics;
import android.util.Size;

public interface ICameraInfo {
    String getCameraId();

    CameraCharacteristics getCharacteristics();

    Size[] getPictureSize(int format);

    Size[] getPreviewSize(Class klass);

    int getSensorOrientation();
}
