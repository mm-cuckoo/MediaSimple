package com.cfox.camera.camera.session.helper.impl;


import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.util.Range;

import com.cfox.camera.CameraConfig;
import com.cfox.camera.camera.session.helper.IBuilderHelper;
import com.cfox.camera.camera.session.helper.ICameraHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;

import java.util.HashMap;
import java.util.Map;

public class BuilderHelper implements IBuilderHelper {

    private ICameraHelper mCameraHelper;
    private Map<CaptureRequest.Key<Integer>, Integer> mConfigMap = new HashMap<>();

    private int mEvValue = 0;

    public BuilderHelper(ICameraHelper cameraHelper) {
        this.mCameraHelper = cameraHelper;
    }

    @Override
    public void clear() {
        mConfigMap.clear();
    }

    @Override
    public void configBuilder(EsRequest request) {
        setBuilder(request, null);
    }

    @Override
    public void repeatingRequestBuilder(EsRequest request, CaptureRequest.Builder builder) {
        setBuilder(request, builder);
    }

    @Override
    public void previewBuilder(CaptureRequest.Builder builder) {
        EsLog.d("previewBuilder: ");
        builder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        if (mCameraHelper.isAutoFocusSupported()) {
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
        if (mCameraHelper.isAutoFocusSupported()) {
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
        }

        if (!mCameraHelper.isLegacyLocked()) {
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
        if (mCameraHelper.isAutoFocusSupported()) {
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
        }
        if (!mCameraHelper.isLegacyLocked()) {
            builder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_CANCEL);
        }
        setExposureValue(builder, mEvValue);
    }

    private void setBuilder(EsRequest request, CaptureRequest.Builder builder) {
        CameraConfig cameraConfig = (CameraConfig) request.getObj(Es.Key.CAMERA_CONFIG);
        if (cameraConfig == null) return;
        for (Map.Entry<CaptureRequest.Key<Integer>, Integer> value : cameraConfig.getValue()) {
            EsLog.d("CaptureRequest: key:" + value.getKey()  + "   value:" + value.getValue());
            mConfigMap.put(value.getKey(), value.getValue());
            if (builder == null) return;
            if (CaptureRequest.FLASH_MODE.equals(value.getKey())) {
                if (value.getValue() == Es.FLASH_TYPE.OFF
                        || value.getValue() == Es.FLASH_TYPE.TORCH)
                    applyFlash(builder);
            } else if (CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION.equals(value.getKey())) {
                setExposureValue(builder, value.getValue());
            } else {
                builder.set(value.getKey(), value.getValue());
            }
        }
    }

    private void setExposureValue(CaptureRequest.Builder builder, int value) {
        EsLog.d("setExposureValue: value:" + value);

        if (value == mEvValue) return;

        Range<Integer> evRange = mCameraHelper.getEvRange();

        if (value >= evRange.getUpper()) {
            value = evRange.getUpper();
        } else if (value <= evRange.getLower()) {
            value = evRange.getLower();
        }

        mEvValue = value;
        builder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, value);
    }


    private void applyFlash(CaptureRequest.Builder builder) {
        int flashType = Es.FLASH_TYPE.OFF;
        if (mConfigMap.containsKey(CaptureRequest.FLASH_MODE)) {
            flashType =  mConfigMap.get(CaptureRequest.FLASH_MODE);
        }
        EsLog.d("applyFlash: flashType:" + flashType);
        switch (flashType) {
//            case Es.FLASH_TYPE.AUTO:
//                builder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
//                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
//                break;
//            case Es.FLASH_TYPE.ON:
//                builder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
//                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
//                break;
            case Es.FLASH_TYPE.OFF:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                break;
            case Es.FLASH_TYPE.TORCH:
                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                break;
        }
    }
}
