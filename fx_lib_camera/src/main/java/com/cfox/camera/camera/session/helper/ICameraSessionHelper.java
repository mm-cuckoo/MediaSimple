package com.cfox.camera.camera.session.helper;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CaptureRequest;
import android.util.Range;
import android.util.Size;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface ICameraSessionHelper {

    Observable<FxResult> onOpenCamera(FxRequest request);
    Observable<FxResult> onCreatePreviewSession(FxRequest request) throws CameraAccessException;
    Observable<FxResult> onSendRepeatingRequest(FxRequest request);
    Observable<FxResult> onSendPreviewRepeatingRequest(FxRequest request);
    Size[] getPictureSize(FxRequest request);
    Size[] getPreviewSize(FxRequest request);
    int getSensorOrientation();
    Observable<FxResult> close();
    Range<Integer> getEvRange();

}
