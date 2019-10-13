package com.cfox.camera.utils;

public interface FxRe {
    interface Camera {
        enum ID {
            FONT("1"),
            BACK("0");
            public String id;
            ID(String id) {
                this.id = id;
            }
        }
    }

    interface Key {
        String CAMERA_ID = "camera_id";
        String SURFACE_HELPER = "Surface_helper";

        String CAMERA_DEVICE = "camera_device";
        String OPEN_CAMERA_STATUS = "open_camera_status";
        String OPEN_CAMERA_ERROR = "open_camera_error";
        String PREVIEW_BUILDER = "preview_builder";
    }

    interface Value {
        String OPEN_SUCCESS = "open_success";
        String OPEN_FAIL = "open_fail";
    }
}
