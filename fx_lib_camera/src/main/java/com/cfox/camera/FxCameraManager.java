package com.cfox.camera;


import com.cfox.camera.controller.FxPhotoController;
import com.cfox.camera.controller.FxVideoController;

public interface FxCameraManager {

    FxPhotoController photo();
    FxVideoController video();

}
