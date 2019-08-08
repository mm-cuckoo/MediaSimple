package com.cfox.camera.camera;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.util.Size;

public class CameraInfo {

    private String mCameraId;
    private CameraCharacteristics mCharacteristics;

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

    public void printf() {
        StreamConfigurationMap map = mCharacteristics
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
    }

    private void chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
//            Log.e(TAG, " video size:width:" + size.getWidth() + "   height:" + size.getHeight());
        }
    }
}
