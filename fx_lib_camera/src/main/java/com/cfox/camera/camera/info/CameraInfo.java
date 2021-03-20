package com.cfox.camera.camera.info;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Size;

public class CameraInfo {
    private String mCameraId;
    private CameraCharacteristics mCharacteristics;

    CameraInfo(String cameraId, CameraCharacteristics characteristics) {
        this.mCameraId = cameraId;
        this.mCharacteristics = characteristics;
    }

    public String getCameraId() {
        return mCameraId;
    }

    public CameraCharacteristics getCharacteristics() {
        return mCharacteristics;
    }

    public Size[] getPictureSize(int format) {
        Size[] sizes = null;
        StreamConfigurationMap map = mCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map != null) {
            sizes =  map.getOutputSizes(format);
        }
        return sizes;
    }

    public Size[] getPreviewSize(Class klass) {
        Size[] sizes = null;
        StreamConfigurationMap map = mCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map != null) {
            sizes = map.getOutputSizes(klass);
        }
        return sizes;
    }

    public int getSensorOrientation() {
        Integer orientation = mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        return orientation == null ? 0 : orientation;
    }
}
