package com.cfox.camera.utils;

public interface Es {
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
        int NONE        = 0;
        int TORCH       = 1;
        int OFF         = 2;
        int AUTO        = 3;
        int ON          = 4;
    }

    interface Key {

        String SURFACE_MANAGER = "surface_manager";

        String CAMERA_ID = "camera_id";
        String SURFACE_HELPER = "surface_helper";
        String CAMERA_DEVICE = "camera_device";
        String CAMERA_INFO = "camera_info";
        String OPEN_CAMERA_STATUS = "open_camera_status";
        String REQUEST_BUILDER = "request_builder";

        String PIC_SIZE = "pic_size";
        String PIC_ORIENTATION = "pic_orientation";
        String PIC_FILE_PATH = "pic_file_path";
        String IMAGE_FORMAT = "Image_format";

        String PREVIEW_SIZE = "preview_size";
        String PREVIEW_EMITTER = "preview_emitter";
        String AF_CHANGE_STATE = "af_change_state";

        String PREVIEW_CAPTURE = "preview_capture";
        String FIRST_FRAME_CALLBACK = "first_frame_callback";
        String SESSION_CALLBACK = "session_callback";
        String CAPTURE_CALLBACK = "capture_callback";
        String CAMERA_FLASH_VALUE = "camera_flash_value";
    }

    interface Value {
        String OK = "ok";
        String CAMERA_OPEN_SUCCESS = "camera_open_success";
        String CAMERA_DISCONNECTED = "camera_disconnected";
        String CAMERA_OPEN_FAIL = "camera_open_fail";
    }
}
