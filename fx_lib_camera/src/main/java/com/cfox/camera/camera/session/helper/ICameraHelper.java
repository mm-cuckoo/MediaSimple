package com.cfox.camera.camera.session.helper;

import android.util.Range;
import android.util.Size;

import com.cfox.camera.camera.ICameraInfo;

public interface ICameraHelper {

    void initCameraInfo(ICameraInfo cameraInfo);
    //    IBuilderPack getBuilderPack();
    int createPreviewTemplate();
    boolean isAutoFocusSupported();
    boolean isRawSupported();
    boolean isLegacyLocked();
    int getSensorOrientation();
    Size[] getPictureSize(int format);
    Size[] getPreviewSize(Class klass);
    Range<Integer> getEvRange();
}
