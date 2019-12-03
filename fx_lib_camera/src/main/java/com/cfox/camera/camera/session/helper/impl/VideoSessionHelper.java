package com.cfox.camera.camera.session.helper.impl;

import android.util.Size;

import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.IVideoSessionHelper;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public class VideoSessionHelper extends AbsCameraSessionHelper implements IVideoSessionHelper {



    private ISessionManager mCameraSessionManager;
    private ICameraSession mCameraSession;

    public VideoSessionHelper(ISessionManager cameraSessionManager) {
        this.mCameraSessionManager = cameraSessionManager;
        mCameraSession = cameraSessionManager.getCameraSession(1).get(0);
    }

    @Override
    public ICameraSession getCameraSession(FxRequest request) {
        return mCameraSession;
    }

    @Override
    public Observable<FxResult> sendRepeatingRequest(FxRequest request) {
        return null;
    }

    @Override
    public Observable<FxResult> sendPreviewRepeatingRequest(FxRequest request) {
        return null;
    }

    @Override
    public Size[] getPictureSize(int format) {
        return new Size[0];
    }

    @Override
    public Size[] getPreviewSize(FxRequest request) {
        return new Size[0];
    }

    @Override
    public int getSensorOrientation() {
        return 0;
    }

    @Override
    public Observable<FxResult> close() {
        return null;
    }
}
