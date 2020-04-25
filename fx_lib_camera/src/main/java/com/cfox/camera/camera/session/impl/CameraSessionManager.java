package com.cfox.camera.camera.session.impl;

import android.content.Context;

import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.ISessionManager;
import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.EsResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;

public class CameraSessionManager implements ISessionManager {

    private static ISessionManager mSessionManager;
    private Context mContext;
    private ICameraSession mSingleSession;
    private List<ICameraSession> mCameraSessionList = new ArrayList<>();

    private AtomicInteger mSessionIndex = new AtomicInteger(0);
    private int mSessionCount = 1;
    private boolean mIsResetMultiSession;


    private CameraSessionManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static ISessionManager getInstance(Context context) {
        if (mSessionManager == null) {
            synchronized (CameraSessionManager.class) {
                if (mSessionManager == null) {
                    mSessionManager = new CameraSessionManager(context);
                }
            }
        }
        return mSessionManager;
    }

    @Override
    public void setSessionCount(int count) {
        EsLog.d("setSessionCount: count:" + count  + "  session size:" + mCameraSessionList.size());
        mSessionIndex.set(0);
        mSessionCount = count;
        mIsResetMultiSession = true;
    }

    private boolean hasSession() {
        return mSessionIndex.get() < mSessionCount;
    }

    @Override
    public ICameraSession getSingleSession() {
        EsLog.d("getSingleSession..");
        if (mSingleSession == null) {
            mSingleSession = createSession();
        }
        return mSingleSession;
    }

    @Override
    public ICameraSession getSessionAndKeepLive() {
        EsLog.d("getSessionAndKeepLive session index:" +  mSessionIndex.get());

        int sessionIndex = mSessionIndex.get();

        if (!hasSession()) {
            throw new RuntimeException("don`t have camera session , create size :" + mCameraSessionList.size()  + "  current session count :" + (mSessionIndex.get() + 1) );
        }

        ICameraSession cameraSession = mCameraSessionList.get(sessionIndex);

        mSessionIndex.set(mSessionIndex.get() + 1);

        return cameraSession;
    }

    @Override
    public Observable<EsResult> closeSession() {
        EsLog.d("closeSession start close all Session ");

        List<Observable<EsResult>> closeSessionList = new ArrayList<>(mCameraSessionList.size() + 1);
        EsLog.d("closeSessionIfNeed: close single session ");

        if (mSingleSession == null) {
            mSingleSession = createSession();
        }

        closeSessionList.add(mSingleSession.onClose());

        for (ICameraSession session : mCameraSessionList) {
            closeSessionList.add(session.onClose());
        }


        return Observable.concat(closeSessionList);
    }

    @Override
    public Observable<EsResult> closeSessionIfNeed() {
        List<Observable<EsResult>> closeSessionList = new ArrayList<>();
        EsLog.d("closeSessionIfNeed: close single session ");
        if (mSingleSession != null) {
            closeSessionList.add(mSingleSession.onClose());
        }

        if (mIsResetMultiSession) {
            mIsResetMultiSession = false;
            resetMultiSession(closeSessionList);
        }


        return Observable.concat(closeSessionList);
    }
    private void resetMultiSession(List<Observable<EsResult>> closeSessionList) {
        for (int i = 0 ; i < mSessionCount; i ++) {
            if (i < mCameraSessionList.size()) {
                EsLog.d("resetMultiSession: get i:" + i);
                closeSessionList.add(mCameraSessionList.get(i).onClose());
            } else {
                EsLog.d( "resetMultiSession: create i:" + i);
                mCameraSessionList.add(createSession());
            }
        }

        for (int i = mSessionCount;  i < mCameraSessionList.size() ; i ++) {
            EsLog.d("resetMultiSession: remove i：" + i);
            closeSessionList.add(mCameraSessionList.remove(i).onClose());
        }
    }

    private ICameraSession createSession() {
        return new CameraSession(mContext);
    }


}
