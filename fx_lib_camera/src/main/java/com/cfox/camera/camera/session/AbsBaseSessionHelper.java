package com.cfox.camera.camera.session;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public abstract class AbsBaseSessionHelper implements ISessionHelper {
    private IFxCameraSession mFxCameraSession;


    AbsBaseSessionHelper(IFxCameraSession cameraSession) {
        this.mFxCameraSession = cameraSession;
    }

    @Override
    public Observable<FxResult> createPreviewSession(FxRequest fxRequest) {
        return mFxCameraSession.createPreviewSession(fxRequest);
    }

    @Override
    public Observable<FxResult> sendRepeatingRequest(FxRequest request) {
        return mFxCameraSession.sendRepeatingRequest(request);
    }

    @Override
    public void closeSession() {
        mFxCameraSession.closeSession();
    }
}
