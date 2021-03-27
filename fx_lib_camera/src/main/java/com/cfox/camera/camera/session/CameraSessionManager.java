package com.cfox.camera.camera.session;


import com.cfox.camera.utils.EsParams;

import io.reactivex.Observable;

public interface CameraSessionManager {

    CameraSession createSession();

    CameraSession createSession(String sessionId);

    Observable<EsParams> closeSession();
}
