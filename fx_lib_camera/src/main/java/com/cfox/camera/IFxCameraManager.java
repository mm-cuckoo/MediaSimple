package com.cfox.camera;


import com.cfox.camera.controller.IDulVideoController;
import com.cfox.camera.controller.IPhotoController;
import com.cfox.camera.controller.IVideoController;

public interface IFxCameraManager {

    IPhotoController photo();
    IVideoController video();
    IDulVideoController dulVideo();
}
