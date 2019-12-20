package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraCharacteristics;
import android.util.Log;
import android.util.Range;
import android.util.Rational;
import android.util.Size;

import com.cfox.camera.camera.ICameraInfo;
import com.cfox.camera.camera.session.helper.ICameraHelper;
import com.cfox.camera.log.EsLog;

public abstract class CameraHelper implements ICameraHelper {
    private static final String TAG = "CameraHelper";

    private ICameraInfo mCameraInfo;

    @Override
    public void initCameraInfo(ICameraInfo cameraInfo) {
        mCameraInfo = cameraInfo;
    }

    @Override
    public boolean isAutoFocusSupported() {
        Float minFocusDist = getCharacteristics().get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
        Log.d(TAG, "isAutoFocusSupported: minFocusDist:" + minFocusDist);
        return minFocusDist != null && minFocusDist > 0;
    }

    @Override
    public boolean isRawSupported() {
        boolean rawSupported = false;
        int[] modes = getCharacteristics().get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
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
        Integer level = getCharacteristics().get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
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
    public int getValidAFMode(int targetMode) {
        int[] allAFMode = getCharacteristics().get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        for (int mode : allAFMode) {
            if (mode == targetMode) {
                return targetMode;
            }
        }
        EsLog.d("not support af mode:" + targetMode + " use mode:" + allAFMode[0]);
        return allAFMode[0];
    }

    @Override
    public int getValidAntiBandingMode(int targetMode) {
        int[] allABMode = getCharacteristics().get(
                CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES);
        for (int mode : allABMode) {
            if (mode == targetMode) {
                return targetMode;
            }
        }
        EsLog.d("not support anti banding mode:" + targetMode
                + " use mode:" + allABMode[0]);
        return allABMode[0];
    }

    @Override
    public boolean isMeteringSupport(boolean focusArea) {
        int regionNum;
        if (focusArea) {
            regionNum = getCharacteristics().get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF);
        } else {
            regionNum = getCharacteristics().get(CameraCharacteristics.CONTROL_MAX_REGIONS_AE);
        }
        return regionNum > 0;
    }

    @Override
    public float getMinimumDistance() {
        Float distance = getCharacteristics().get(
                CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
        if (distance == null) {
            return 0;
        }
        return distance;
    }

    @Override
    public boolean isFlashSupport() {
        Boolean support = getCharacteristics().get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        return support != null && support;
    }

    @Override
    public boolean canTriggerAf() {
        int[] allAFMode = getCharacteristics().get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        return  allAFMode != null && allAFMode.length > 1;
    }

    @Override
    public Range<Integer> getEvRange() {
        return getCharacteristics().get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
    }

    public Rational getEvStep() {
        return getCharacteristics().get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP);
    }

    private CameraCharacteristics getCharacteristics() {
        return mCameraInfo.getCharacteristics();
    }
}
