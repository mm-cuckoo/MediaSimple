package com.cfox.camera;


import com.cfox.camera.controller.IController;

public interface IFxCameraManager {

    IController photo();
    IController video();
}
