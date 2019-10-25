package com.cfox.camera.model.module;

import com.cfox.camera.camera.device.IFxCameraDevice;
import com.cfox.camera.camera.session.helper.IVideoSessionHelper;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public class VideoModule extends BaseModule{
    public VideoModule(IFxCameraDevice cameraDevice, IVideoSessionHelper videoSessionHelper) {
        super(cameraDevice, videoSessionHelper);
    }

    @Override
    public Observable<FxResult> onStartPreview(FxRequest request) {
        return startPreview(request);
    }
}
