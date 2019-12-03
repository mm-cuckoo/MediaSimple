package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraCharacteristics;
import android.util.Log;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.camera.ICameraInfo;
import com.cfox.camera.camera.session.helper.ICameraHelper;

public abstract class CameraHelper implements ICameraHelper {
    private static final String TAG = "CameraHelper";

    private ICameraInfo mCameraInfo;

    @Override
    public void initCameraInfo(ICameraInfo cameraInfo) {
        mCameraInfo = cameraInfo;
    }

    @Override
    public boolean isAutoFocusSupported() {
        Float minFocusDist = mCameraInfo.getCharacteristics().get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
        Log.d(TAG, "isAutoFocusSupported: minFocusDist:" + minFocusDist);
        return minFocusDist != null && minFocusDist > 0;
    }

    @Override
    public boolean isRawSupported() {
        boolean rawSupported = false;
        int[] modes = mCameraInfo.getCharacteristics().get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
        for (int mode : modes) {
            if (mode == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_RAW) {
                rawSupported = true;
                break;
            }
        }
        return rawSupported;
    }

    @Override
    public boolean isLegacyLocked() {
        Integer level = mCameraInfo.getCharacteristics().get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        Log.d(TAG, "isLegacyLocked: INFO_SUPPORTED_HARDWARE_LEVEL:" + level);
        return level != null && level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY;
    }

    @Override
    public Size[] getPreviewSize(Class klass) {
        return mCameraInfo.getPreviewSize(klass);
    }

    @Override
    public Size[] getPictureSize(int format) {
        return mCameraInfo.getPictureSize(format);
    }

    @Override
    public int getSensorOrientation() {
        return mCameraInfo.getSensorOrientation();
    }

    @Override
    public Range<Integer> getEvRange() {
        return mCameraInfo.getCharacteristics().get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
    }

//    public Rational getEvStep() {
//        return mCameraInfo.getCharacteristics().get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP);
//    }
}
