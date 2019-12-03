package com.cfox.camera.model.module;

import com.cfox.camera.camera.session.helper.ICameraSessionHelper;
import com.cfox.camera.model.module.business.IBusiness;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public class DulVideoModule extends BaseModule {
    public DulVideoModule(ICameraSessionHelper cameraSessionHelper, IBusiness business) {
        super(cameraSessionHelper, business);
    }

    @Override
    public Observable<FxResult> requestPreview(FxRequest request) {
        return null;
    }
}
