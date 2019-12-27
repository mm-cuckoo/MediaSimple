package com.cfox.camera.camera.session.helper.impl;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.camera.ICameraInfo;
import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.ICameraHelper;
import com.cfox.camera.camera.session.helper.IDulVideoSessionHelper;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.EsResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class DulVideoSessionHelper extends AbsCameraSessionHelper implements IDulVideoSessionHelper {

    private Map<String, ICameraInfo> mCameraInfoMap = new HashMap<>(2);
    private Map<String, ICameraHelper> mCameraHelperMap = new HashMap<>(2);
    private Map<String, CaptureRequest.Builder> mPreviewBuilderMap = new HashMap<>(2);
    private Map<String, ICameraSession> mCameraSessionMap = new HashMap<>(2);
    private ISessionManager mCameraSessionManager;

    public DulVideoSessionHelper(ISessionManager cameraSessionManager) {
        this.mCameraSessionManager = cameraSessionManager;
        cameraSessionManager.getCameraSession(2);
    }

    @Override
    public void applyPreviewRepeatingBuilder(EsRequest request) throws CameraAccessException {
        String cameraId = request.getString(Es.Key.CAMERA_ID);
        ICameraHelper cameraHelper = getCameraHelperForId(cameraId);
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(Es.Key.SURFACE_HELPER);
        CaptureRequest.Builder builder = getCameraSessionForId(cameraId).onCreateRequestBuilder(cameraHelper.createPreviewTemplate());
        mPreviewBuilderMap.put(cameraId, builder);
        builder.addTarget(surfaceHelper.getSurface());
        request.put(Es.Key.REQUEST_BUILDER, builder);
    }

    @Override
    public ICameraSession getCameraSession(EsRequest request) {
        String cameraId = request.getString(Es.Key.CAMERA_ID);
        // TODO: 19-12-5 check camera id
        return getCameraSessionForId(cameraId);
    }

    @Override
    public Observable<EsResult> onSendRepeatingRequest(EsRequest request) {
        String cameraId = request.getString(Es.Key.CAMERA_ID);
        request.put(Es.Key.REQUEST_BUILDER, mPreviewBuilderMap.get(cameraId));
        return getCameraSessionForId(cameraId).onRepeatingRequest(request);
    }

    @Override
    public Size[] getPictureSize(EsRequest request) {
        String cameraId = request.getString(Es.Key.CAMERA_ID);
        int imageFormat = request.getInt(Es.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        return getCameraHelperForId(cameraId).getPictureSize(imageFormat);
    }

    @Override
    public Size[] getPreviewSize(EsRequest request) {
        String cameraId = request.getString(Es.Key.CAMERA_ID);
        EsLog.d("getPreviewSize: camera id:" + cameraId);
        Class klass = (Class) request.getObj(Es.Key.SURFACE_CLASS);
        return getCameraHelperForId(cameraId).getPreviewSize(klass);
    }

    @Override
    public int getSensorOrientation(EsRequest request) {
        String cameraId = request.getString(Es.Key.CAMERA_ID);
        return getCameraHelperForId(cameraId).getSensorOrientation();
    }

    @Override
    public Observable<EsResult> close(EsRequest request) {
        EsLog.d("close: dul video close camera s");
        return Observable.create(new ObservableOnSubscribe<EsResult>() {
            @Override
            public void subscribe(ObservableEmitter<EsResult> emitter) throws Exception {
                for (ICameraSession cameraSession : mCameraSessionMap.values()) {
                    cameraSession.onClose();
                }
            }
        });
    }

    @Override
    public Range<Integer> getEvRange(EsRequest request) {
        String cameraId = request.getString(Es.Key.CAMERA_ID);
        return getCameraHelperForId(cameraId).getEvRange();
    }

    private ICameraSession getCameraSessionForId(String cameraId) {
        ICameraSession cameraSession;
        if (mCameraSessionMap.containsKey(cameraId)) {
            cameraSession = mCameraSessionMap.get(cameraId);
        } else {
            cameraSession = mCameraSessionManager.getCameraSession();
            mCameraSessionMap.put(cameraId, cameraSession);
        }
        return cameraSession;
    }

    private ICameraHelper getCameraHelperForId(String cameraId) {
        ICameraHelper cameraHelper;
        if (mCameraHelperMap.containsKey(cameraId)) {
            cameraHelper = mCameraHelperMap.get(cameraId);
        } else {
            cameraHelper = new DulVideoCameraHelper();
            cameraHelper.initCameraInfo(getCameraInfoForId(cameraId));
            mCameraHelperMap.put(cameraId, cameraHelper);
        }

        return cameraHelper;
    }

    private ICameraInfo getCameraInfoForId(String cameraId) {
        ICameraInfo cameraInfo;
        if (mCameraInfoMap.containsKey(cameraId)) {
            cameraInfo = mCameraInfoMap.get(cameraId);
        } else {
            cameraInfo = getCameraInfo(cameraId);
            mCameraInfoMap.put(cameraId, cameraInfo);
        }

        return cameraInfo;
    }
}
