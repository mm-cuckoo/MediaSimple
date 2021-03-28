package com.cfox.camera.camera.info;

import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.log.EsLog;

public class CameraInfoManagerImpl implements CameraInfoManager {

    public static final CameraInfoManager CAMERA_INFO_MANAGER = new CameraInfoManagerImpl();

    private CameraInfo mCameraInfo;

    private CameraInfoManagerImpl(){}

    @Override
    public void initCameraInfo(CameraInfo cameraInfo) {
        mCameraInfo = cameraInfo;
    }

    @Override
    public boolean isAutoFocusSupported() {
        Float minFocusDist = getCharacteristics().get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
        EsLog.d("isAutoFocusSupported: minFocusDist:" + minFocusDist);
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
        EsLog.d("isLegacyLocked: INFO_SUPPORTED_HARDWARE_LEVEL:" + level);
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
        int[] allABMode = getCharacteristics().get(CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES);
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
        Integer regionNum;
        if (focusArea) {
            regionNum = getCharacteristics().get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF);
        } else {
            regionNum = getCharacteristics().get(CameraCharacteristics.CONTROL_MAX_REGIONS_AE);
        }
        if (regionNum == null) {
            regionNum = 0;
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

    @Override
    public Range<Float> getFocusRange() {
        Float minFocus = getCharacteristics().get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
        Float maxFocus = getCharacteristics().get(CameraCharacteristics.LENS_INFO_HYPERFOCAL_DISTANCE);
        return new Range<>(minFocus, maxFocus);
    }


    /**
     * 在这里拿到的值就是zoom 范围
     *  1 ~ MaxZoom
     * @return max zoom size
     */
    @Override
    public float getMaxZoom() {
        Float maxZoom = getCharacteristics().get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
        if (maxZoom == null) {
            maxZoom = 1f;
        } else {
            // 在这里扩大10倍
            maxZoom *= 10;
        }
        return maxZoom;
    }

    @Override
    public Rect getActiveArraySize() {
        return getCharacteristics().get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
    }

    @Override
    public CameraCharacteristics getCharacteristics() {
        return mCameraInfo.getCharacteristics();
    }
}
