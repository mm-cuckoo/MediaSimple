package com.cfox.camera.camera.session.helper.impl;

import android.graphics.ImageFormat;
import android.util.Size;

import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.IVideoSessionHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public class VideoSessionHelper extends AbsCameraSessionHelper implements IVideoSessionHelper {



    private ISessionManager mCameraSessionManager;
    private ICameraSession mCameraSession;

    public VideoSessionHelper(ISessionManager sessionManager) {
        sessionManager.getCameraSession(1);
        mCameraSession = sessionManager.getCameraSession();
    }

    @Override
    public ICameraSession getCameraSession(FxRequest request) {
        return mCameraSession;
    }

    @Override
    public Observable<FxResult> onSendRepeatingRequest(FxRequest request) {
        return null;
    }

    @Override
    public Observable<FxResult> onSendPreviewRepeatingRequest(FxRequest request) {
        return null;
    }

    @Override
    public Size[] getPictureSize(FxRequest request) {
        int imageFormat = request.getInt(FxRe.Key.IMAGE_FORMAT, ImageFormat.JPEG);
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
