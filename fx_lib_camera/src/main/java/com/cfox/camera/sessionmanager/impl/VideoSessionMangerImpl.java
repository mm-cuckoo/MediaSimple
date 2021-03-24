package com.cfox.camera.sessionmanager.impl;

import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.camera.device.session.DeviceSessionManager;
import com.cfox.camera.sessionmanager.VideoSessionManger;
import com.cfox.camera.utils.EsParams;

import io.reactivex.Observable;

public class VideoSessionMangerImpl extends AbsSessionManager implements VideoSessionManger {



    private DeviceSessionManager mCameraSessionManager;
    private DeviceSession mDeviceSession;

    public VideoSessionMangerImpl(DeviceSessionManager sessionManager) {
        mCameraSessionManager = sessionManager;

    }

    @Override
    public void init() {

    }

    @Override
    public Observable<EsParams> cameraStatus() {
        return null;
    }

    @Override
    public DeviceSession getCameraSession(EsParams esParams) {
        if (mDeviceSession == null) {
            mDeviceSession = mCameraSessionManager.createSession();
        }
        return mDeviceSession;
    }

    @Override
    public Observable<EsParams> onRepeatingRequest(EsParams esParams) {
        return null;
    }


    @Override
    void onBeforeOpenCamera(EsParams esParams) {
        mCameraSessionManager.closeSession().subscribe();
    }

    @Override
    Observable<EsParams> applyPreviewPlan(EsParams esParams) {
        return null;
    }

    @Override
    public Observable<EsParams> close(EsParams esParams) {
        return null;
    }

}
