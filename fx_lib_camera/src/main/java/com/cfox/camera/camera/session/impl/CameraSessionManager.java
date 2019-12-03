package com.cfox.camera.camera.session.impl;

import android.content.Context;
import android.util.Log;

import com.cfox.camera.camera.session.ICameraSession;
import com.cfox.camera.camera.session.ISessionManager;

import java.util.ArrayList;
import java.util.List;

public class CameraSessionManager implements ISessionManager {
    private static final String TAG = "CameraSessionManager";

    private static ISessionManager mSessionManager;
    private List<ICameraSession> mCameraSessionList = new ArrayList<>();
    private Context mContext;


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
    public List<ICameraSession> getCameraSession(int count) {
        Log.d(TAG, "getCameraSession: count:" + count);

        for (int i = 0 ; i < count; i ++) {
            if (i < mCameraSessionList.size()) {
                Log.d(TAG, "getCameraSession: get i:" + i);
                mCameraSessionList.get(i).onClose();
            } else {
                Log.d(TAG, "getCameraSession: create i:" + i);
                mCameraSessionList.add(createSession());
            }
        }

        for (int i = count;  i < mCameraSessionList.size() ; i ++) {
            Log.d(TAG, "getCameraSession: remove i：" + i);
            mCameraSessionList.remove(i).onClose();
        }
        return mCameraSessionList;
    }

    private ICameraSession createSession() {
        return new CameraSession(mContext);
    }


}
