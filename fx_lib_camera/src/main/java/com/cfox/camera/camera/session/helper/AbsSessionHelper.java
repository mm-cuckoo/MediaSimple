package com.cfox.camera.camera.session.helper;

import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public abstract class AbsSessionHelper implements ISessionHelper {
    private ICameraSession mCameraSession;


    AbsSessionHelper(ICameraSession cameraSession) {
        this.mCameraSession = cameraSession;
    }

    @Override
    public Observable<FxResult> createPreviewSession(FxRequest request) {
        return mCameraSession.onCreatePreviewSession(request);
    }

    @Override
    public Observable<FxResult> sendPreviewRepeatingRequest(FxRequest request) {
        return mCameraSession.onPreviewRepeatingRequest(request);
    }

    @Override
    public void closeSession() {
        mCameraSession.closeSession();
    }
}
