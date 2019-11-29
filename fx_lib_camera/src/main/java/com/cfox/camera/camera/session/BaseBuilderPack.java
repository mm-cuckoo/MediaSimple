package com.cfox.camera.camera.session;

import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.util.Log;
import android.util.Range;

import com.cfox.camera.CameraConfig;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseBuilderPack implements IBuilderPack  {
    private static final String TAG = "BaseBuilderPack";

    private ICameraSession mCameraSession;
    private Map<CaptureRequest.Key<Integer>, Integer> mConfigMap = new HashMap<>();

    private int mEvValue = 0;

    BaseBuilderPack(ICameraSession cameraSession) {
        this.mCameraSession = cameraSession;
    }

    @Override
    public void clear() {
        mConfigMap.clear();
    }

    @Override
    public void configBuilder(FxRequest request) {
        setBuilder(request, null);
    }

    @Override
    public void repeatingRequestBuilder(FxRequest request, CaptureRequest.Builder builder) {
        setBuilder(request, builder);
    }

    @Override
    public void previewBuilder(CaptureRequest.Builder builder) {
        Log.d(TAG, "previewBuilder: ");
        builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        if (mCameraSession.isAutoFocusSupported()) {
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_IDLE);
        }
        builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.FLASH_MODE_SINGLE);
        builder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);

        setExposureValue(builder, mEvValue);
    }

    @Override
    public void preCaptureBuilder(CaptureRequest.Builder builder) {
        setExposureValue(builder, mEvValue);
        if (mCameraSession.isAutoFocusSupported()) {
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
        }

        if (!mCameraSession.isLegacyLocked()) {
            builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_START);
        }
    }

    @Override
    public void captureBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        builder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);
        setExposureValue(builder, mEvValue);
    }

    @Override
    public void previewCaptureBuilder(CaptureRequest.Builder builder) {
        if (mCameraSession.isAutoFocusSupported()) {
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
        }
        if (!mCameraSession.isLegacyLocked()) {
            builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_CANCEL);
        }
        setExposureValue(builder, mEvValue);

    }

    private void setBuilder(FxRequest request, CaptureRequest.Builder builder) {
        CameraConfig cameraConfig = (CameraConfig) request.getObj(FxRe.Key.CAMERA_CONFIG);
        if (cameraConfig == null) return;
        for (Map.Entry<CaptureRequest.Key<Integer>, Integer> value : cameraConfig.getValue()) {
            Log.d(TAG, "CaptureRequest: key:" + value.getKey()  + "   value:" + value.getValue());
            mConfigMap.put(value.getKey(), value.getValue());
            if (builder == null) return;
            if (CaptureRequest.FLASH_MODE.equals(value.getKey())) {
                if (value.getValue() == FxRe.FLASH_TYPE.CLOSE
                        || value.getValue() == FxRe.FLASH_TYPE.TORCH)
                applyFlash(builder);
            } else if (CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION.equals(value.getKey())) {
                setExposureValue(builder, value.getValue());
            } else {
                builder.set(value.getKey(), value.getValue());
            }
        }
    }

    private void setExposureValue(CaptureRequest.Builder builder, int value) {
        Log.d(TAG, "setExposureValue: value:" + value);

        if (value == mEvValue) return;

        Range<Integer> evRange = mCameraSession.getEvRange();

        if (value >= evRange.getUpper()) {
            value = evRange.getUpper();
        } else if (value <= evRange.getLower()) {
            value = evRange.getLower();
        }

        mEvValue = value;
        builder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, value);
    }


    private void applyFlash(CaptureRequest.Builder builder) {
        int flashType = FxRe.FLASH_TYPE.CLOSE;
        if (mConfigMap.containsKey(CaptureRequest.FLASH_MODE)) {
            flashType =  mConfigMap.get(CaptureRequest.FLASH_MODE);
        }
        Log.d(TAG, "applyFlash: flashType:" + flashType);
        switch (flashType) {
//            case FxRe.FLASH_TYPE.AUTO:
//                builder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
//                break;
//            case FxRe.FLASH_TYPE.OPEN:
//                builder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
//                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
//                break;
            case FxRe.FLASH_TYPE.CLOSE:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                break;
            case FxRe.FLASH_TYPE.TORCH:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                break;
        }
    }
}
