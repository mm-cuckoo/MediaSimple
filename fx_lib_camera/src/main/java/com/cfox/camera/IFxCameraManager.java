package com.cfox.camera;

import com.cfox.camera.controller.FxPhotoController;
import com.cfox.camera.controller.FxVideoController;

public interface IFxCameraManager {

    FxPhotoController photo();
    FxVideoController video();

}
