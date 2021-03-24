package com.cfox.camera.sessionmanager.impl;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;

import com.cfox.camera.camera.info.CameraInfo;
import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.camera.device.session.DeviceSessionManager;
import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.sessionmanager.DulVideoSessionManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsParams;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class DulVideoSessionManagerImpl extends AbsSessionManager implements DulVideoSessionManager {

    private final Map<String, CameraInfo> mCameraInfoMap = new HashMap<>(2);
    private final Map<String, CameraInfoManager> mCameraHelperMap = new HashMap<>(2);
    private final Map<String, CaptureRequest.Builder> mPreviewBuilderMap = new HashMap<>(2);
    private final Map<String, DeviceSession> mCameraSessionMap = new HashMap<>(2);
    private final Map<String, CameraCaptureSession.CaptureCallback> mCameraSessionCallbackMap = new HashMap<>(2);
    private final DeviceSessionManager mCameraSessionManager;

    public DulVideoSessionManagerImpl(DeviceSessionManager cameraSessionManager) {
        this.mCameraSessionManager = cameraSessionManager;

    }

    @Override
    public void init() {
        EsLog.d("init .....");
        mCameraSessionManager.closeSession().subscribe();
    }

    @Override
    public Observable<EsParams> cameraStatus() {
        return null;
    }


    @Override
    void onBeforeOpenCamera(EsParams esParams) {

    }

    @Override
    Observable<EsParams> applyPreviewPlan(EsParams esParams) {
        return null;
    }

    @Override
    public DeviceSession getCameraSession(EsParams esParams) {
        String cameraId = esParams.getString(Es.Key.CAMERA_ID);
        // TODO: 19-12-5 check camera id
        return getCameraSessionForId(cameraId);
    }

    @Override
    public Observable<EsParams> onRepeatingRequest(EsParams esParams) {
        String cameraId = esParams.getString(Es.Key.CAMERA_ID);
        esParams.put(Es.Key.REQUEST_BUILDER, mPreviewBuilderMap.get(cameraId));
        return getCameraSessionForId(cameraId).onRepeatingRequest(esParams);
    }

    @Override
    public Observable<EsParams> close(EsParams esParams) {
        EsLog.d("close: dul video close camera s");

        return mCameraSessionManager.closeSession().doOnNext(new Consumer<EsParams>() {
            @Override
            public void accept(EsParams esParams) throws Exception {
                mCameraInfoMap.clear();
                mCameraHelperMap.clear();
                mPreviewBuilderMap.clear();
                mCameraSessionMap.clear();
            }
        });
    }


    private DeviceSession getCameraSessionForId(String cameraId) {
        DeviceSession deviceSession;
        if (mCameraSessionMap.containsKey(cameraId)) {
            deviceSession = mCameraSessionMap.get(cameraId);
        } else {
            deviceSession = mCameraSessionManager.createSession();
            mCameraSessionMap.put(cameraId, deviceSession);
        }
        return deviceSession;
    }

//    private CameraInfoManager getCameraHelperForId(String cameraId) {
//        CameraInfoManager cameraHelper;
//        if (mCameraHelperMap.containsKey(cameraId)) {
//            cameraHelper = mCameraHelperMap.get(cameraId);
//        } else {
//            cameraHelper = new DulVideoCameraHelper();
//            cameraHelper.initCameraInfo(getCameraInfoForId(cameraId));
//            mCameraHelperMap.put(cameraId, cameraHelper);
//        }
//
//        return cameraHelper;
//    }

//    private CameraInfo getCameraInfoForId(String cameraId) {
//        CameraInfo cameraInfo;
//        if (mCameraInfoMap.containsKey(cameraId)) {
//            cameraInfo = mCameraInfoMap.get(cameraId);
//        } else {
//            cameraInfo = getCameraInfo(cameraId);
//            mCameraInfoMap.put(cameraId, cameraInfo);
//        }
//
//        return cameraInfo;
//    }
}
