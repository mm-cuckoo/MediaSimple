package com.cfox.camera.model.module;

import com.cfox.camera.camera.device.IFxCameraDevice;
import com.cfox.camera.camera.session.helper.ISessionHelper;
import com.cfox.camera.camera.session.helper.IVideoSessionHelper;

public class VideoModule extends BaseModule{
    public VideoModule(IFxCameraDevice cameraDevice, IVideoSessionHelper videoSessionHelper) {
        super(cameraDevice, videoSessionHelper);
    }
}
