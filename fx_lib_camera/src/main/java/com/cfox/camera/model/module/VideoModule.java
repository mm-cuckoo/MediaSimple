package com.cfox.camera.model.module;

import com.cfox.camera.camera.session.helper.IVideoSessionHelper;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public class VideoModule extends BaseModule {
    public VideoModule(IVideoSessionHelper videoSessionHelper, IBusiness business) {
        super(videoSessionHelper, business);
    }

    @Override
    public Observable<EsResult> requestPreview(EsRequest request) {
        return startPreview(request);
    }
}
