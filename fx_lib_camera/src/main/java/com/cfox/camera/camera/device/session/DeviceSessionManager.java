package com.cfox.camera.camera.device.session;


import com.cfox.camera.utils.EsParams;

import io.reactivex.Observable;

public interface DeviceSessionManager {

    DeviceSession createSession();

    DeviceSession createSession(String sessionId);

    Observable<EsParams> closeSession();
}
