package com.cfox.camera.model.module;


import android.util.Range;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface IModule {

    Observable<FxResult> requestPreview(FxRequest request);
    Observable<FxResult> requestStop();
    Observable<FxResult> onCameraConfig(FxRequest request);
    Observable<FxResult> requestCapture(FxRequest request);
    Range<Integer> getEvRange();

}
