package com.cfox.camera.camera.session.helper.impl;

import android.graphics.ImageFormat;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.IVideoSessionHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public class VideoSessionHelper extends AbsCameraSessionHelper implements IVideoSessionHelper {



    private ISessionManager mCameraSessionManager;
    private ICameraSession mCameraSession;

    public VideoSessionHelper(ISessionManager sessionManager) {
        sessionManager.getCameraSession(1);
        mCameraSession = sessionManager.getCameraSession();
    }

    @Override
    public ICameraSession getCameraSession(EsRequest request) {
        return mCameraSession;
    }

    @Override
    public Observable<EsResult> onSendRepeatingRequest(EsRequest request) {
        return null;
    }

    @Override
    public Observable<EsResult> onSendPreviewRepeatingRequest(EsRequest request) {
        return null;
    }

    @Override
    public Size[] getPictureSize(EsRequest request) {
        int imageFormat = request.getInt(Es.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        return new Size[0];
    }

    @Override
    public Size[] getPreviewSize(EsRequest request) {
        return new Size[0];
    }

    @Override
    public int getSensorOrientation(EsRequest request) {
        return 0;
    }

    @Override
    public Observable<EsResult> close(EsRequest request) {
        return null;
    }

    @Override
    public Range<Integer> getEvRange(EsRequest request) {
        return null;
    }
}
