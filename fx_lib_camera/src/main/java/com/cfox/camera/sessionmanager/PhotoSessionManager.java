package com.cfox.camera.sessionmanager;


import com.cfox.camera.EsParams;

import io.reactivex.Observable;

public interface PhotoSessionManager extends SessionManager {

    Observable<EsParams> capture(EsParams esParams);

}
