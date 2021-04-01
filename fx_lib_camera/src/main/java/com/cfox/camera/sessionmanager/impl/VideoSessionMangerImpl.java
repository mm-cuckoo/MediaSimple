package com.cfox.camera.sessionmanager.impl;

import android.hardware.camera2.CameraAccessException;

import com.cfox.camera.camera.session.CameraSession;
import com.cfox.camera.camera.session.CameraSessionManager;
import com.cfox.camera.sessionmanager.VideoSessionManger;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.EsParams;

import io.reactivex.Observable;

public class VideoSessionMangerImpl extends AbsSessionManager implements VideoSessionManger {



    private CameraSessionManager mCameraSessionManager;
    private CameraSession mCameraSession;

    public VideoSessionMangerImpl(CameraSessionManager sessionManager) {
        mCameraSessionManager = sessionManager;

    }

    @Override
    public void init() {

    }

    @Override
    public CameraSession getCameraSession(EsParams esParams) {
        if (mCameraSession == null) {
            mCameraSession = mCameraSessionManager.createSession();
        }
        return mCameraSession;
    }

    @Override
    public Observable<EsParams> onRepeatingRequest(EsParams esParams) {
        return null;
    }

    @Override
    public Observable<EsParams> onPreviewRepeatingRequest(EsParams esParams) throws CameraAccessException {
        return null;
    }


    @Override
    void onBeforeOpenCamera(EsParams esParams) {
        mCameraSessionManager.closeSession().subscribe(new CameraObserver<EsParams>());
    }

    @Override
    public Observable<EsParams> close(EsParams esParams) {
        return null;
    }

}
