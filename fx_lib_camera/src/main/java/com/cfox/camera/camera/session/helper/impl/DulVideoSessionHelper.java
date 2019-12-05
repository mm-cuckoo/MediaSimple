package com.cfox.camera.camera.session.helper.impl;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.util.Log;
import android.util.Size;

import com.cfox.camera.camera.ICameraInfo;
import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.camera.session.helper.ICameraHelper;
import com.cfox.camera.camera.session.helper.IDulVideoSessionHelper;
import com.cfox.camera.surface.ISurfaceHelper;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class DulVideoSessionHelper extends AbsCameraSessionHelper implements IDulVideoSessionHelper {
    private static final String TAG = "DulVideoSessionHelper";

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
    public void applyPreviewRepeatingBuilder(FxRequest request) throws CameraAccessException {
        String cameraId = request.getString(FxRe.Key.CAMERA_ID);
        ICameraHelper cameraHelper = getCameraHelperForId(cameraId);
        ISurfaceHelper surfaceHelper = (ISurfaceHelper) request.getObj(FxRe.Key.SURFACE_HELPER);
        CaptureRequest.Builder builder = getCameraSessionForId(cameraId).onCreateRequestBuilder(cameraHelper.createPreviewTemplate());

        builder.addTarget(surfaceHelper.getSurface());
        request.put(FxRe.Key.REQUEST_BUILDER, builder);
    }

    @Override
    public ICameraSession getCameraSession(FxRequest request) {
        String cameraId = request.getString(FxRe.Key.CAMERA_ID);
        // TODO: 19-12-5 check camera id
        return getCameraSessionForId(cameraId);
    }

    @Override
    public Observable<FxResult> onSendRepeatingRequest(FxRequest request) {
        return null;
    }

    @Override
    public Size[] getPictureSize(FxRequest request) {
        String cameraId = request.getString(FxRe.Key.CAMERA_ID);
        int imageFormat = request.getInt(FxRe.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        return getCameraHelperForId(cameraId).getPictureSize(imageFormat);
    }

    @Override
    public Size[] getPreviewSize(FxRequest request) {
        String cameraId = request.getString(FxRe.Key.CAMERA_ID);
        Log.d(TAG, "getPreviewSize: camera id:" + cameraId);
        Class klass = (Class) request.getObj(FxRe.Key.SURFACE_CLASS);
        return getCameraHelperForId(cameraId).getPreviewSize(klass);
    }

    @Override
    public int getSensorOrientation() {
        return 0;
    }

    @Override
    public Observable<FxResult> close() {
        return null;
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
