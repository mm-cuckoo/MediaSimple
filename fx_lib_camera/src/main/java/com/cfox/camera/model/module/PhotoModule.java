package com.cfox.camera.model.module;

import com.cfox.camera.camera.device.IFxCameraDevice;
import com.cfox.camera.camera.session.ISessionHelper;

public class PhotoModule extends BaseModule {
    public PhotoModule(IFxCameraDevice cameraDevice, ISessionHelper sessionHelper) {
        super(cameraDevice, sessionHelper);
    }
}
