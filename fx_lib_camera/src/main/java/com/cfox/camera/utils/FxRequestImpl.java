package com.cfox.camera.utils;

import java.util.HashMap;
import java.util.Map;

public class FxRequestImpl implements FxRequest{

    private Map<String, Object> mMapObject = new HashMap<>();

    @Override
    public void put(String key, Object value) {
        mMapObject.put(key,value);
    }

    @Override
    public void put(String key, int value) {
        mMapObject.put(key, value);
    }

    @Override
    public void put(String key, float value) {
        mMapObject.put(key, value);
    }

    @Override
    public Object getObj(String key) {
        if (mMapObject.containsKey(key)) {
            return mMapObject.get(key);
        }
        return null;
    }

    @Override
    public String getString(String key, String def) {
        if (mMapObject.containsKey(key)) {
            return (String) mMapObject.get(key);
        }
        return def;
    }

    @Override
    public String getString(String key) {
        return getString(key, null);
    }

    @Override
    public int getInt(String key, int def) {
        if (mMapObject.containsKey(key)) {
            return (int) mMapObject.get(key);
        }
        return def;
    }

    @Override
    public int getInt(String key) {
        return getInt(key, 0);
    }

    @Override
    public float getFloat(String key, float def) {
        if (mMapObject.containsKey(key)) {
            return (float) mMapObject.get(key);
        }
        return def;
    }

    @Override
    public float getFloat(String key) {
        return getInt(key, 0);
    }
}
