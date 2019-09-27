package com.cfox.camera.surface;

import com.cfox.camera.utils.FxRequest;

import io.reactivex.Observable;

public interface ISurfaceHelper {
    Observable<FxRequest> isAvailable();
}
