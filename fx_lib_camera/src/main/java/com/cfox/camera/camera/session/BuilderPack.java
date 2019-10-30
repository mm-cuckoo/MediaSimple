package com.cfox.camera.camera.session;

import android.hardware.camera2.CaptureRequest;
import android.util.Log;

import com.cfox.camera.CameraConfig;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;

import java.util.HashMap;
import java.util.Map;

public class BuilderPack implements IBuilderPack {
    private static final String TAG = "BuilderPack";

    private Map<CaptureRequest.Key<Integer>, Integer> mConfigMap = new HashMap<>();

    @Override
    public void clear() {
        mConfigMap.clear();
    }

    @Override
    public void configToBuilder(FxRequest request, CaptureRequest.Builder builder) {
        configToBuilder(request, builder, true);
    }

    @Override
    public void configToBuilder(FxRequest request, CaptureRequest.Builder builder, boolean isSave) {
        Log.d(TAG, "configToBuilder: ......");
        CameraConfig cameraConfig = (CameraConfig) request.getObj(FxRe.Key.CAMERA_CONFIG);
        if (cameraConfig == null) return;
        for (Map.Entry<CaptureRequest.Key<Integer>, Integer> value : cameraConfig.getValue()) {
            Log.d(TAG, "CaptureRequest: key:" + value.getKey()  + "   value:" + value.getValue());
            builder.set(value.getKey(), value.getValue());
            if (isSave) mConfigMap.put(value.getKey(), value.getValue());
        }
    }

    @Override
    public void appendPreviewBuilder(CaptureRequest.Builder builder) {
        Log.d(TAG, "appendPreviewBuilder: ......");
        for (Map.Entry<CaptureRequest.Key<Integer>, Integer> value : mConfigMap.entrySet()) {
            Log.d(TAG, "appendPreviewBuilder: key:" + value.getKey()  + "   value:" + value.getValue());
            builder.set(value.getKey(), value.getValue());
        }
    }
}
