package com.cfox.camera;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.util.Pair;
import android.util.Size;

import com.cfox.camera.imagereader.ImageReaderProvider;
import com.cfox.camera.surface.SurfaceManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EsParams {

    private final Map<Key<?>, Object> mMapObject = new HashMap<>();

    public <T> void put(Key<T> key, T value) {
        mMapObject.put(key,value);
    }


    public <T> T get(Key<T> key) {
        if (mMapObject.containsKey(key) ) {
            return (T) mMapObject.get(key);
        }
        return null;
    }

    public <T> T get(Key<T> key, T def) {
        if (mMapObject.containsKey(key) ) {
            return (T) mMapObject.get(key);
        }
        return def;
    }

    @Override
    public String toString() {
        String result = super.toString();
        if (BuildConfig.DEBUG) {
            StringBuilder buffer = new StringBuilder("\n");
            buffer.append("================ Params ======================================================= ");
            buffer.append("\n");
            for (Map.Entry<Key<?>, Object> entry : mMapObject.entrySet()) {
                buffer.append("== ").append(entry.getKey().NAME).append(":").append(entry.getValue()).append("\n");
            }
            buffer.deleteCharAt(buffer.length() -1);
            result = buffer.toString();
        }
        return result;
    }
    public interface Value {
        interface CAMERA_ID {
            String FONT = "1";
            String BACK = "0";
        }

        interface FLASH_STATE {
//            int TORCH       = 1;
            int OFF         = 2;
            int AUTO        = 3;
            int ON          = 4;
        }
        String OK = "ok";
        String CAMERA_OPEN_SUCCESS = "camera_open_success";
        String CAMERA_DISCONNECTED = "camera_disconnected";
        String CAMERA_OPEN_FAIL = "camera_open_fail";

        interface CAPTURE_STATE {
            int CAPTURE_START = 1;
            int CAPTURE_COMPLETED = 2;
            int CAPTURE_FAIL = 3;
        }
    }


    public final static class Key<T> {


        public static final EsParams.Key<SurfaceManager> SURFACE_MANAGER = new EsParams.Key<>("surface_manager");
        public static final EsParams.Key<List<ImageReaderProvider>> IMAGE_READER_PROVIDERS = new EsParams.Key<>("image_reader_providers");
        public static final EsParams.Key<String> CAMERA_ID = new EsParams.Key<>("camera_id");
        public static final EsParams.Key<CameraDevice> CAMERA_DEVICE = new EsParams.Key<>("camera_device");
        public static final EsParams.Key<String> OPEN_CAMERA_STATE = new EsParams.Key<>("open_camera_state");
        public static final EsParams.Key<CaptureRequest.Builder> REQUEST_BUILDER = new EsParams.Key<>("request_builder");
        public static final EsParams.Key<Size> PIC_SIZE = new EsParams.Key<>("pic_size");
        public static final EsParams.Key<Integer> PIC_ORIENTATION = new EsParams.Key<>("pic_orientation");
        public static final EsParams.Key<Size> PREVIEW_SIZE = new EsParams.Key<>("preview_size");
        public static final EsParams.Key<Integer> AF_STATE = new EsParams.Key<>("af_state");
        public static final EsParams.Key<Integer> CAPTURE_STATE = new EsParams.Key<>("capture_state");
        public static final EsParams.Key<String> PREVIEW_FIRST_FRAME = new EsParams.Key<>("preview_first_frame");
        public static final EsParams.Key<CameraCaptureSession.CaptureCallback> CAPTURE_CALLBACK = new EsParams.Key<>("capture_callback");

        public static final EsParams.Key<Float> ZOOM_VALUE = new EsParams.Key<>("zoom_value");
        public static final EsParams.Key<Integer> FLASH_STATE = new EsParams.Key<>("camera_flash_value");
        public static final EsParams.Key<Integer> EV_SIZE = new EsParams.Key<>("ev_size");
        public static final EsParams.Key<Pair<Float, Float>> AF_TRIGGER = new EsParams.Key<>("af_trigger");// Pair<X, Y>
        public static final EsParams.Key<Boolean> RESET_FOCUS = new EsParams.Key<>("reset_focus");

        public final String NAME;
        public Key(String name) {
            this.NAME = name;
        }
    }
}
