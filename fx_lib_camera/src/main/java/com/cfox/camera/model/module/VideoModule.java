package com.cfox.camera.model.module;

import com.cfox.camera.camera.device.IFxCameraDevice;
import com.cfox.camera.camera.session.ISessionHelper;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public class VideoModule extends BaseModule{
    public VideoModule(IFxCameraDevice cameraDevice, ISessionHelper sessionHelper) {
        super(cameraDevice, sessionHelper);
    }
}
