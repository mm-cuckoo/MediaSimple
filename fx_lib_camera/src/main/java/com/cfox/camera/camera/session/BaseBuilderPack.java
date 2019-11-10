package com.cfox.camera.camera.session;

import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.util.Log;

import com.cfox.camera.CameraConfig;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseBuilderPack implements IBuilderPack  {
    private static final String TAG = "BaseBuilderPack";

    private ICameraSession mCameraSession;
    private Map<CaptureRequest.Key<Integer>, Integer> mConfigMap = new HashMap<>();

    public BaseBuilderPack(ICameraSession cameraSession) {
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
        applyFlash(builder);
        applyAFTrigger(builder);
        applyAFMode(builder);

    }

    @Override
    public void startCaptureBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
        applyFlash(builder);
    }

    @Override
    public void captureBuilder(CaptureRequest.Builder builder) {
        applyFlash(builder);
        applyAFMode(builder);
    }

    @Override
    public void previewCaptureBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);

    }

    private void setBuilder(FxRequest request, CaptureRequest.Builder builder) {
        CameraConfig cameraConfig = (CameraConfig) request.getObj(FxRe.Key.CAMERA_CONFIG);
        if (cameraConfig == null) return;
        for (Map.Entry<CaptureRequest.Key<Integer>, Integer> value : cameraConfig.getValue()) {
            Log.d(TAG, "CaptureRequest: key:" + value.getKey()  + "   value:" + value.getValue());
            mConfigMap.put(value.getKey(), value.getValue());
            if (builder == null) return;
            if (value.getKey().equals(CaptureRequest.FLASH_MODE)) {
                applyFlash(builder);
            } else {
                builder.set(value.getKey(), value.getValue());
            }
        }
    }

    private void applyAFTrigger(CaptureRequest.Builder builder) {
        int afTrigger = CaptureRequest.CONTROL_AF_TRIGGER_IDLE;
        if (mConfigMap.containsKey(CaptureRequest.CONTROL_AF_TRIGGER)) {
            afTrigger = mConfigMap.get(CaptureRequest.CONTROL_AF_TRIGGER);
        }
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, afTrigger);

    }

    private void applyAFMode(CaptureRequest.Builder builder) {
        int afMode = CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE;
        if (mConfigMap.containsKey(CaptureRequest.CONTROL_AF_MODE)) {
            afMode = mConfigMap.get(CaptureRequest.CONTROL_AF_MODE);
        }
        builder.set(CaptureRequest.CONTROL_AF_MODE, afMode);
    }

    private void applyFlash(CaptureRequest.Builder builder) {
        int flashType = FxRe.FLASH_TYPE.AUTO;
        if (mConfigMap.containsKey(CaptureRequest.FLASH_MODE)) {
            flashType =  mConfigMap.get(CaptureRequest.FLASH_MODE);
        }
        switch (flashType) {
            case FxRe.FLASH_TYPE.AUTO:
                builder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                break;
            case FxRe.FLASH_TYPE.OPEN:
                builder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                break;
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
