package com.cfox.camera.camera.session.helper.impl;

import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.VideoSessionHelper;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public class VideoSessionHelperImpl extends AbsCameraSessionHelper implements VideoSessionHelper {



    private ISessionManager mCameraSessionManager;
    private DeviceSession mDeviceSession;

    public VideoSessionHelperImpl(ISessionManager sessionManager) {
        mCameraSessionManager = sessionManager;

    }

    @Override
    public void init() {

    }

    @Override
    public DeviceSession getCameraSession(EsRequest request) {
        if (mDeviceSession == null) {
            mDeviceSession = mCameraSessionManager.createSession();
        }
        return mDeviceSession;
    }

    @Override
    public Observable<EsResult> onRepeatingRequest(EsRequest request) {
        return null;
    }

    @Override
    Observable<EsResult> beforeOpenCamera(EsRequest request) {
        mDeviceSession = null;
        return mCameraSessionManager.closeSession();
    }

    @Override
    public Observable<EsResult> close(EsRequest request) {
        return null;
    }

}
