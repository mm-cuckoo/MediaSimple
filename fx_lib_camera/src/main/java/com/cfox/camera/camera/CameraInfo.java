package com.cfox.camera.camera;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Size;

public class CameraInfo implements ICameraInfo {
    private String mCameraId;
    private CameraCharacteristics mCharacteristics;

    CameraInfo(String cameraId, CameraCharacteristics characteristics) {
        this.mCameraId = cameraId;
        this.mCharacteristics = characteristics;
    }

    @Override
    public String getCameraId() {
        return mCameraId;
    }

    @Override
    public CameraCharacteristics getCharacteristics() {
        return mCharacteristics;
    }

    @Override
    public Size[] getPictureSize(int format) {
        Size[] sizes = null;
        StreamConfigurationMap map = mCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map != null) {
            sizes =  map.getOutputSizes(format);
        }
        return sizes;
    }

    @Override
    public Size[] getPreviewSize(Class klass) {
        Size[] sizes = null;
        StreamConfigurationMap map = mCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map != null) {
            sizes = map.getOutputSizes(klass);
        }
        return sizes;
    }

    @Override
    public int getSensorOrientation() {
        Integer orientation = mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        return orientation == null ? 0 : orientation;
    }
}
