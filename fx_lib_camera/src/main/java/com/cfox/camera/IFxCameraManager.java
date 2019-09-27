package com.cfox.camera;


import com.cfox.camera.controller.IController;
import com.cfox.camera.surface.ISurfaceHelper;

public interface IFxCameraManager {

    IController photo();
    IController video();
    void setSurfaceHelper(ISurfaceHelper surfaceHelper);

}
