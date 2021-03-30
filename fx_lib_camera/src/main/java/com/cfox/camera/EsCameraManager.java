package com.cfox.camera;


import com.cfox.camera.capture.Capture;

public interface EsCameraManager {

    Capture photoModule();

    Capture videoModule();
}
