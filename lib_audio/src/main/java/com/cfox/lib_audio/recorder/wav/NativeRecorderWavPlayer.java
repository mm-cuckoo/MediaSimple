package com.cfox.lib_audio.recorder.wav;


import com.cfox.lib_audio.log.FxLog;
import com.cfox.lib_audio.recorder.wav.audio.AudioPlayer;
import com.cfox.lib_audio.recorder.wav.audio.AudioStatus;
import com.cfox.lib_audio.recorder.wav.info.PlayerAudioInfo;
import com.cfox.lib_audio.recorder.wav.wavfile.WavFileReader;

import java.io.IOException;

public class NativeRecorderWavPlayer implements AudioPlayer.PlayStatusListener {

    private static final String TAG = "NativeRecorderWavPlayer";
    private AudioPlayer mAudioPlayer;
    private WavFileReader mWavFileReader;
    private IAudioStatusListener mStatusListener;
    private IAudioWaveDataListener mWaveDataListener;
    private long mFilePointer;

    public void setWaveDataListener(IAudioWaveDataListener mWaveDataListener) {
        this.mWaveDataListener = mWaveDataListener;
    }

    public void setStatusListener(IAudioStatusListener mStatusListener) {
        this.mStatusListener = mStatusListener;
    }

    public NativeRecorderWavPlayer() {
        mAudioPlayer = new AudioPlayer();
        mWavFileReader = new WavFileReader();
        mAudioPlayer.setStatusChangeListener(this);
    }


    public void startPlay(String filePath) throws IOException {
        FxLog.d(TAG, "Audio File path:" + filePath);
        if (!mWavFileReader.openFile(filePath)){
            FxLog.d(TAG, "Audio File read fail");
            return;
        }
        PlayerAudioInfo audioInfo = PlayerAudioInfo.parse(mWavFileReader.getWavFileHeader());
        if (mAudioPlayer.startPlay(audioInfo)) {
            new Thread(AudioPlayRunnable).start();
        }
    }

    public void pausePlay() throws IOException {
        mFilePointer = mWavFileReader.getFilePointer();
        mAudioPlayer.pausePlay();
    }

    public void resumePlay() throws IOException {
        if (mAudioPlayer.resumePlay()) {
            mWavFileReader.seek(mFilePointer);
            new Thread(AudioPlayRunnable).start();
        }
    }

    public void stopPlay() {
        mAudioPlayer.stopPlay();
    }

    public void forward(long length) throws IOException {
        mWavFileReader.forward(length);
    }

    public void back(long length) throws IOException {
        mWavFileReader.back(length);
    }

    public int getRecorderStatus() {
        return mAudioPlayer.getAudioStatus();
    }

    private Runnable AudioPlayRunnable = new Runnable() {
        @Override
        public void run() {
            byte[] buffer = new byte[getBufferSize() * 2];
            while (mAudioPlayer.isPalying() && mWavFileReader.readData(buffer, 0, buffer.length) > 0) {
                int audioStatus = mAudioPlayer.getAudioStatus();
                if (audioStatus == AudioStatus.PAUSE || audioStatus == AudioStatus.STOP) {
                    break;
                }

                mAudioPlayer.playData(buffer, 0, buffer.length);
                if (mWaveDataListener != null) {
                    mWaveDataListener.waveData(buffer);
                }
            }
            playRelease();
        }
    };

    private void playRelease() {
        if (mAudioPlayer.getAudioStatus() == AudioStatus.PAUSE) {
            return;
        }
        mWavFileReader.release();
        mAudioPlayer.release();
    }

    private int getBufferSize() {
        return mAudioPlayer.getAudioBufferSize();
    }

    @Override
    public void statusChange(int status) {
        if (mStatusListener != null) {
            mStatusListener.statusChange(status);
        }
    }
}
