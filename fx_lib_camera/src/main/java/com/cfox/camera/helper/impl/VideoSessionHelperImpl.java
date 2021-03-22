package com.cfox.camera.helper.impl;

import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.camera.device.session.DeviceSessionManager;
import com.cfox.camera.helper.VideoSessionHelper;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public class VideoSessionHelperImpl extends AbsCameraSessionHelper implements VideoSessionHelper {



    private DeviceSessionManager mCameraSessionManager;
    private DeviceSession mDeviceSession;

    public VideoSessionHelperImpl(DeviceSessionManager sessionManager) {
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
