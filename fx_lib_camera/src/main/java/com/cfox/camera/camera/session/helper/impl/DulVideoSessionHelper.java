package com.cfox.camera.camera.session.helper.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;
import android.util.Size;

import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.IDulVideoSessionHelper;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import java.util.List;

import io.reactivex.Observable;

public class DulVideoSessionHelper extends AbsCameraSessionHelper implements IDulVideoSessionHelper {



    private ISessionManager mCameraSessionManager;
    private ICameraSession mCameraSession1;
    private ICameraSession mCameraSession2;

    public DulVideoSessionHelper(ISessionManager cameraSessionManager) {
        this.mCameraSessionManager = cameraSessionManager;
        List<ICameraSession> cameraSessionList = cameraSessionManager.getCameraSession(2);
        mCameraSession1 = cameraSessionList.get(0);
        mCameraSession2 = cameraSessionList.get(1);
    }

    @Override
    public ICameraSession getCameraSession(FxRequest request) {
        return null;
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
