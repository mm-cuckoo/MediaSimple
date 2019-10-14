package com.cfox.camera.camera;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.util.Log;
import android.util.Size;

import java.util.Collections;
import java.util.Comparator;

public class CameraInfo {
    private static final String TAG = "CameraInfo";

    private String mCameraId;
    private CameraCharacteristics mCharacteristics;
    private Size mDefaultSize = new Size(1080, 1920);


    public CameraInfo(String cameraId, CameraCharacteristics characteristics) {
        this.mCameraId = cameraId;
        this.mCharacteristics = characteristics;
        printf();

    }

    public String getCameraId() {
        return mCameraId;
    }


    public CameraCharacteristics getCharacteristics() {
        return mCharacteristics;
    }

    public Size getPicSize(int width, int height, int format) {
        Size size = mDefaultSize;
        StreamConfigurationMap map = mCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map != null) {
            size = chooseSize(width, height, map.getOutputSizes(format));
        }
        return size;
    }

    private Size chooseSize(int width, int height, Size[] sizes) {
        return null;
    }


    public Size getPreviewSize(int width, int height, Class klass) {
        Size size = mDefaultSize;
        StreamConfigurationMap map = mCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map != null) {
            size = chooseSize(width, height, map.getOutputSizes(klass));
        }
        return size;
    }

    public void printf() {
        StreamConfigurationMap map = mCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
    }

    private void chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            Log.e(TAG, " id:" + getCameraId() + " size:width:" + size.getWidth() + "   height:" + size.getHeight());
        }
    }
}
