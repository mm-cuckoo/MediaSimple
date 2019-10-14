package com.cfox.camera.camera;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public abstract class AbsBaseSessionHelper implements ISessionHelper{
    private IFxCameraSession mFxCameraSession;


    public AbsBaseSessionHelper() {
        this.mFxCameraSession = FxCameraSession.getsInstance();
    }

    @Override
    public Observable<FxResult> createPreviewSession(FxRequest fxRequest) {
        return mFxCameraSession.createPreviewSession(fxRequest);
    }

    @Override
    public void closeSession() {
        mFxCameraSession.closeSession();
    }
}
