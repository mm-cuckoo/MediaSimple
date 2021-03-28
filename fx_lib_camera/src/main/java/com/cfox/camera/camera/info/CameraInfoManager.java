package com.cfox.camera.camera.info;

import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.util.Range;
import android.util.Size;


public interface CameraInfoManager {

    void initCameraInfo(CameraInfo cameraInfo);

//    int createPreviewTemplate();

    boolean isAutoFocusSupported();

    boolean isRawSupported();

    boolean isLegacyLocked();

    int getSensorOrientation();

    int getValidAFMode(int targetMode);

    int getValidAntiBandingMode(int targetMode);

    boolean isMeteringSupport(boolean focusArea);

    float getMinimumDistance();

    boolean isFlashSupport();

    boolean canTriggerAf();

    Size[] getPictureSize(int format);

    Size[] getPreviewSize(Class klass);

    Range<Integer> getEvRange();

    Range<Float> getFocusRange();

    float getMaxZoom();

    Rect getActiveArraySize();

    CameraCharacteristics getCharacteristics();

}
