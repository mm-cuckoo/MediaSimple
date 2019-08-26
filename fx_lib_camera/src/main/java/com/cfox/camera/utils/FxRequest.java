package com.cfox.camera.utils;

public interface FxRequest {
    interface Camera {
        enum ID {
            FONT("0"),
            BACK("1");
            private String id;
            ID(String id) {
                this.id = id;
            }
        }
    }

    interface Key {
        String CAMERA_ID = "camera_id";
    }

    void put(String key , Object value);
    void put(String key , int value);
    void put(String key , float value);

    Object getObj(String key);
    String getString(String key, String def);
    String getString(String key);
    int getInt(String key, int def);
    int getInt(String key);
    float getFloat(String key, float def);
    float getFloat(String key);

}
