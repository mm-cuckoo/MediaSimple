package com.cfox.camera.utils;

import java.util.HashMap;
import java.util.Map;

public class FxRequest {

    private Map<String, Object> mMapObject = new HashMap<>();

    public void put(String key, Object value) {
        mMapObject.put(key,value);
    }

    public void put(String key, int value) {
        mMapObject.put(key, value);
    }

    public void put(String key, float value) {
        mMapObject.put(key, value);
    }

    public void  put(String key ,boolean value) {
        mMapObject.put(key, value);
    }

    public Object getObj(String key) {
        return getObj(key, null);
    }

    public Object getObj(String key, Object def) {
        return getValue(key, def);
    }

    public String getString(String key, String def) {
        return (String) getValue(key, def);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public int getInt(String key, int def) {
        return (int) getValue(key,def);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public float getFloat(String key, float def) {
        return (float) getValue(key, def);
    }

    public float getFloat(String key) {
        return getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        return (boolean) getValue(key, def);
    }

    private Object getValue(String key, Object def) {
        if (mMapObject.containsKey(key)) {
            return mMapObject.get(key);
        }
        return def;
    }

    @Override
    public String toString() {
        return "FxRequest{" +
                "mMapObject=" + mMapObject +
                '}';
    }
}
