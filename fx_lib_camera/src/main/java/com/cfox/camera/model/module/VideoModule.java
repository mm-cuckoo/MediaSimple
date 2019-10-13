package com.cfox.camera.model.module;

import com.cfox.camera.camera.IFxCameraDevice;
import com.cfox.camera.camera.ISessionHelper;

public class VideoModule extends BaseModule{
    public VideoModule(IFxCameraDevice cameraDevice, ISessionHelper sessionHelper) {
        super(cameraDevice, sessionHelper);
    }
}
