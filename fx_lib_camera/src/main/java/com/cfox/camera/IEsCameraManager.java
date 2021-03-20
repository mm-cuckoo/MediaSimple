package com.cfox.camera;


import com.cfox.camera.capture.BaseCapture;

public interface IEsCameraManager {

    BaseCapture photoModule();

    BaseCapture videoModule();

    BaseCapture dulVideoModule();
}
