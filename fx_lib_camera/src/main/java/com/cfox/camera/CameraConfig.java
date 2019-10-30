package com.cfox.camera;

import android.hardware.camera2.CaptureRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CameraConfig {
    private Map<CaptureRequest.Key<Integer>, Integer> mConfigMap;

    private CameraConfig() {
        mConfigMap = new HashMap<>();
    }

    public static CameraConfig getInstance() {
        return new CameraConfig();
    }

    public void push(CaptureRequest.Key<Integer> key , int value) {
        mConfigMap.put(key, value);
    }

    public Set<Map.Entry<CaptureRequest.Key<Integer>, Integer>> getValue() {
        return mConfigMap.entrySet();
    }

    public Integer getValue(CaptureRequest.Key<Integer> key) {
        return mConfigMap.get(key);
    }
}
