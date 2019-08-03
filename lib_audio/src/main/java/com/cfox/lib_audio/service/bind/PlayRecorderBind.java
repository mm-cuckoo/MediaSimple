package com.cfox.lib_audio.service.bind;

import android.os.Bundle;
import android.os.RemoteException;

import com.cfox.lib_audio.ICallBack;
import com.cfox.lib_audio.IPlayerRecorderService;
import com.cfox.lib_audio.IWaveCallBack;
import com.cfox.lib_audio.recorder.RecorderPlayer;
import com.cfox.lib_audio.service.ServiceConstants;

public class PlayRecorderBind extends IPlayerRecorderService.Stub implements ServiceConstants {
    private RecorderPlayer mRecorderPlayer;

    public PlayRecorderBind(RecorderPlayer recorderPlayer) {
        this.mRecorderPlayer = recorderPlayer;
    }
    @Override
    public void startPlay(Bundle bundle, ICallBack callback) throws RemoteException {
        String filePath = bundle.getString(FILE_PATH);
        mRecorderPlayer.setCallBack(callback).startPlay(filePath);
    }

    @Override
    public void setWaveListener(IWaveCallBack callback) throws RemoteException {
        mRecorderPlayer.setWaveCallBack(callback);
    }

    @Override
    public void pausePlay(Bundle bundle, ICallBack callback) throws RemoteException {
        mRecorderPlayer.setCallBack(callback).pausePlay();
    }

    @Override
    public void resumePlay(Bundle bundle, ICallBack callback) throws RemoteException {
        mRecorderPlayer.setCallBack(callback).resumePlay();
    }

    @Override
    public void stopPlay(Bundle bundle, ICallBack callback) throws RemoteException {
        mRecorderPlayer.setCallBack(callback).stopPlay();
    }

    @Override
    public void back(long lenth) throws RemoteException {
        mRecorderPlayer.back(lenth);
    }

    @Override
    public void forward(long length) throws RemoteException {
        mRecorderPlayer.forward(length);
    }

    @Override
    public void getRecorderStatus(ICallBack callback) throws RemoteException {
        mRecorderPlayer.setCallBack(callback).getRecorderStatus();
    }
}
