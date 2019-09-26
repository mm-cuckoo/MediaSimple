package com.cfox.camera.controller;

import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.FxResult;

import io.reactivex.Observable;

public interface Controller {
    Observable<FxResult> startPreview(FxRequest request);
    Observable<FxResult> openCamera(FxRequest request);
}
