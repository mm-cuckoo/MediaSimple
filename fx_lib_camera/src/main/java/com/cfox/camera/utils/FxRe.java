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

    interface FLASH_TYPE {
        int OPEN        = 1;
        int CLOSE       = 2;
        int AUTO        = 3;
        int TORCH       = 4;
    }

    interface Key {
        String CAMERA_ID = "camera_id";
        String SURFACE_HELPER = "Surface_helper";
        String CAMERA_DEVICE = "camera_device";
        String OPEN_CAMERA_STATUS = "open_camera_status";
        String REQUEST_BUILDER = "request_builder";

        String PIC_SIZE = "pic_size";
        String PIC_ORIENTATION = "pic_orientation";
        String PIC_FILE_PATH = "pic_file_path";
        String IMAGE_FORMAT = "Image_format";

        String PREVIEW_SIZE = "preview_size";

        String CAMERA_CONFIG = "camera_config";
        String PREVIEW_CAPTURE = "preview_capture";
    }

    interface Value {
        String OPEN_SUCCESS = "open_success";
        String OPEN_FAIL = "open_fail";
    }
}
