package com.cfox.camera.model.module;

import com.cfox.camera.camera.session.helper.IVideoSessionHelper;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public class VideoModule extends BaseModule{
    public VideoModule(IVideoSessionHelper videoSessionHelper, IBusiness business) {
        super(videoSessionHelper, business);
    }

    @Override
    public Observable<FxResult> onStartPreview(FxRequest request) {
        return startPreview(request);
    }
}
