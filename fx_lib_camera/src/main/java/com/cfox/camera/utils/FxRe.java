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
        String CAPTURE_REQUEST_BUILDER = "capture_request_builder";

        String PIC_WIDTH = "pic_width";
        String PIC_HEIGHT = "pic_height";
        String IMAGE_FORMAT = "Image_format";
        String PIC_FILE_PATH = "pic_file_path";

        String PREVIEW_WIDTH = "preview_width";
        String PREVIEW_HEIGHT = "preview_height";

        String CAMERA_CONFIG = "camera_config";
    }

    interface Value {
        String OPEN_SUCCESS = "open_success";
        String OPEN_FAIL = "open_fail";
    }
}
