package com.cfox.camera.camera.session;


import com.cfox.camera.utils.EsResult;

import io.reactivex.Observable;

public interface ISessionManager  {

    void setSessionCount(int count);

    ICameraSession getSingleSession();

    ICameraSession getSessionAndKeepLive();

    Observable<EsResult> closeSession();

    Observable<EsResult> closeSessionIfNeed();
}
