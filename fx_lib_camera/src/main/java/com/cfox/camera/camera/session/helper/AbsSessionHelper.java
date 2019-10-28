package com.cfox.camera.camera.session.helper;

import android.hardware.camera2.CaptureRequest;
import android.util.Log;

import com.cfox.camera.CameraConfig;
import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import java.util.Map;

import io.reactivex.Observable;

public abstract class AbsSessionHelper implements ISessionHelper {
    private static final String TAG = "AbsSessionHelper";
    private ICameraSession mCameraSession;


    AbsSessionHelper(ICameraSession cameraSession) {
        this.mCameraSession = cameraSession;
    }

    @Override
    public Observable<FxResult> createPreviewSession(FxRequest request) {
        return mCameraSession.onCreatePreviewSession(request);
    }

    @Override
    public Observable<FxResult> sendPreviewRepeatingRequest(FxRequest request) {
        return mCameraSession.onPreviewRepeatingRequest(request);
    }

    @Override
    public void closeSession() {
        mCameraSession.closeSession();
    }
    void configToBuilder(FxRequest request, CaptureRequest.Builder builder) {
        CameraConfig cameraConfig = (CameraConfig) request.getObj(FxRe.Key.CAMERA_CONFIG);
        if (cameraConfig == null) return;
        for (Map.Entry<CaptureRequest.Key<Integer>, Integer> value : cameraConfig.getValue()) {
            Log.d(TAG, "CaptureRequest: key:" + value.getKey()  + "   value:" + value.getValue());
            builder.set(value.getKey(), value.getValue());
        }
    }
}
