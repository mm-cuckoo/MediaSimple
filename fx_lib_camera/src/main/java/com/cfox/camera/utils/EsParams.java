package com.cfox.camera.utils;

import com.cfox.camera.BuildConfig;

import java.util.HashMap;
import java.util.Map;

public class EsParams {
    private final Map<String, Object> mMapObject = new HashMap<>();

    public void put(String key, Object value) {
        mMapObject.put(key,value);
    }

    public void put(String key, int value) {
        mMapObject.put(key, value);
    }

    public void put(String key, float value) {
        mMapObject.put(key, value);
    }

    public Object getObj(String key) {
        if (mMapObject.containsKey(key)) {
            return mMapObject.get(key);
        }
        return null;
    }

    public String getString(String key, String def) {
        if (mMapObject.containsKey(key)) {
            return (String) mMapObject.get(key);
        }
        return def;
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public int getInt(String key, int def) {
        if (mMapObject.containsKey(key)) {
            return (int) mMapObject.get(key);
        }
        return def;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public float getFloat(String key, float def) {
        if (mMapObject.containsKey(key)) {
            return (float) mMapObject.get(key);
        }
        return def;
    }

    public float getFloat(String key) {
        return getInt(key, 0);
    }

    @Override
    public String toString() {
        String result = super.toString();
        if (BuildConfig.DEBUG) {
            StringBuilder buffer = new StringBuilder("\n");
            buffer.append("================ Params ======================================================= ");
            buffer.append("\n");
            buffer.append("============================================================================================");
            buffer.append("\n");
            for (Map.Entry<String, Object> entry : mMapObject.entrySet()) {
                buffer.append("== ").append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }
            buffer.append("============================================================================================");
            result = buffer.toString();
        }
        return result;
    }
}
