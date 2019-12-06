package com.cfox.camera;


import com.cfox.camera.controller.ICameraController;
import com.cfox.camera.controller.IDulVideoController;
import com.cfox.camera.controller.IPhotoController;
import com.cfox.camera.controller.IVideoController;

public interface IEsCameraManager {

    ICameraController photoModule();
    ICameraController videoModule();
    ICameraController dulVideoModule();
}
