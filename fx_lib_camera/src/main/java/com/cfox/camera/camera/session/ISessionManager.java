package com.cfox.camera.camera.session;


import com.cfox.camera.camera.device.session.DeviceSession;
import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface ISessionManager  {


    DeviceSession createSession();

    DeviceSession createSession(String sessionId);

    Observable<EsResult> closeSession();
}
