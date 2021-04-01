package com.cfox.camera.camera.session;

import android.content.Context;

import com.cfox.camera.camera.device.EsCameraDeviceImpl;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.CameraObserver;
import com.cfox.camera.EsParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

public class CameraSessionManagerImpl implements CameraSessionManager {

    private static CameraSessionManager mSessionManager;
    private final Context mContext;
    private final Map<String, CameraSession> mCameraSessionMap = new HashMap<>();

    private CameraSessionManagerImpl(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static CameraSessionManager getInstance(Context context) {
        if (mSessionManager == null) {
            synchronized (CameraSessionManagerImpl.class) {
                if (mSessionManager == null) {
                    mSessionManager = new CameraSessionManagerImpl(context);
                }
            }
        }
        return mSessionManager;
    }

    @Override
    public Observable<EsParams> closeSession() {
        EsLog.d("closeSession start close all Session ");

        List<Observable<EsParams>> closeSessionList = new ArrayList<>();

        for (CameraSession session : mCameraSessionMap.values()) {
            closeSessionList.add(session.onClose());
        }

        if (closeSessionList.size() == 0) {
            return Observable.create(new ObservableOnSubscribe<EsParams>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<EsParams> emitter) throws Exception {
                    EsLog.d("closeSessionIfNeed: closeSessionList.size() == 0 ");
                    emitter.onNext(new EsParams());
                }
            });
        }
        EsLog.d("closeSessionIfNeed: close single session size:" + closeSessionList.size());
        mCameraSessionMap.clear();
        return Observable.concat(closeSessionList);
    }

    @Override
    public CameraSession createSession() {
        return createSession(String.valueOf(mCameraSessionMap.size()));
    }

    @Override
    public CameraSession createSession(String sessionId) {
        EsLog.d("createSession: session id ：" + sessionId);
        CameraSession session;
        if (mCameraSessionMap.containsKey(sessionId)) {
            EsLog.e("session already has: session id ：" + sessionId);
            session = mCameraSessionMap.get(sessionId);
            session.onClose().subscribe(new CameraObserver<EsParams>());
        }  else  {
            session = new CameraSessionImpl(EsCameraDeviceImpl.getsInstance(mContext));
        }
        mCameraSessionMap.put(sessionId, session);
        return session;
    }


}
