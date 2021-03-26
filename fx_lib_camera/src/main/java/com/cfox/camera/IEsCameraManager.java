package com.cfox.camera;


import com.cfox.camera.capture.Capture;

public interface IEsCameraManager {

    Capture photoModule();

    Capture videoModule();
}
