package com.cfox.camera.camera.device.session;


import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface DeviceSessionManager {

    DeviceSession createSession();

    DeviceSession createSession(String sessionId);

    Observable<EsResult> closeSession();
}
