package com.cfox.camera.utils;

public interface FxReq {
    interface Camera {
        enum ID {
            FONT("0"),
            BACK("1");
            public String id;
            ID(String id) {
                this.id = id;
            }
        }
    }

    interface Key {
        String CAMERA_ID = "camera_id";
        String CAMERA_DEVICE = "camera_device";
    }
}
