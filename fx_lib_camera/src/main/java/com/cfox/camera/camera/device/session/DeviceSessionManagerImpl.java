package com.cfox.camera.camera.device.session;

import android.content.Context;

import com.cfox.camera.camera.device.EsCameraDeviceImpl;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.EsResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

public class DeviceSessionManagerImpl implements DeviceSessionManager {

    private static DeviceSessionManager mSessionManager;
    private final Context mContext;
    private final Map<String, DeviceSession> mCameraSessionMap = new HashMap<>();

    private DeviceSessionManagerImpl(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static DeviceSessionManager getInstance(Context context) {
        if (mSessionManager == null) {
            synchronized (DeviceSessionManagerImpl.class) {
                if (mSessionManager == null) {
                    mSessionManager = new DeviceSessionManagerImpl(context);
                }
            }
        }
        return mSessionManager;
    }

    @Override
    public Observable<EsResult> closeSession() {
        EsLog.d("closeSession start close all Session ");

        List<Observable<EsResult>> closeSessionList = new ArrayList<>();

        for (DeviceSession session : mCameraSessionMap.values()) {
            closeSessionList.add(session.onClose());
        }

        if (closeSessionList.size() == 0) {
            return Observable.create(new ObservableOnSubscribe<EsResult>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<EsResult> emitter) throws Exception {
                    EsLog.d("closeSessionIfNeed: closeSessionList.size() == 0 ");
                    emitter.onNext(new EsResult());
                }
            });
        }
        EsLog.d("closeSessionIfNeed: close single session size:" + closeSessionList.size());
        mCameraSessionMap.clear();
        return Observable.concat(closeSessionList);
    }

    @Override
    public DeviceSession createSession() {
        return createSession(String.valueOf(mCameraSessionMap.size()));
    }

    @Override
    public DeviceSession createSession(String sessionId) {
        EsLog.d("createSession: session id ：" + sessionId);
        DeviceSession session = null;
        if (mCameraSessionMap.containsKey(sessionId)) {
            EsLog.e("session already has: session id ：" + sessionId);
            session = mCameraSessionMap.get(sessionId);
            session.onClose().subscribe();
        }  else  {
            session = new DeviceSessionImpl(EsCameraDeviceImpl.getsInstance(mContext));
        }
        mCameraSessionMap.put(sessionId, session);
        return session;
    }


}
