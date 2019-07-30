package com.chao.module_player.audio;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chao.lib_audio.ICallBack;
import com.chao.lib_audio.IWaveCallBack;
import com.chao.lib_audio.log.FxLog;
import com.chao.lib_audio.recorder.RecorderConstants;
import com.chao.lib_audio.recorder.wav.audio.AudioStatus;
import com.chao.lib_audio.service.RecorderService;
import com.chao.lib_audio.service.ServiceConstants;
import com.chao.lib_audio.service.connection.PlayerServiceConnection;
import com.chao.lib_common.base.BaseActivity;
import com.chao.module_player.Constants;
import com.chao.module_player.R;

public class AudioPlayerActivity extends BaseActivity  implements View.OnClickListener,
        ServiceConstants, RecorderConstants, Constants, AudioStatus {
    private static final String TAG = "AudioPlayerActivity";
    private TextView mTvPlayStart;
    private TextView mTvWaveData;
    private Button mBtnStartPlay;
    private Button mBtnPausePlay;
    private Button mBtnResumePlay;
    private Button mBtnStopPlay;
    private Button mBtnBack;
    private Button mBtnForward;

    private PlayerServiceConnection conn;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case HANDLER_KEY_PALY_STATUS:
                    recorderStatusChange(msg.getData());
                    break;

                case HANDLER_KEY_PLAY_WAVE:
                    recorderWaveData(msg.getData());
                    break;

            }

        }
    };

    private void recorderWaveData(Bundle data) {
        byte[] waveByte = data.getByteArray(PLAY_RECORDER_WAVE_DATE);
        FxLog.d(TAG, "waveByte length:" + waveByte.length);
    }

    private void recorderStatusChange(Bundle data) {
        int recorderStatus = data.getInt(PLAY_RECORDER_STATUS);
        String status = "Recorder Status :";
        switch (recorderStatus) {
            case STARTING:
                status += "STARTING";
                break;

            case PAUSE:
                status += "PAUSE";
                break;

            case RESUME:
                status += "RESUME";
                break;

            case STOP:
                status += "STOP";
                break;
        }
        mTvPlayStart.setText(status);
    }


    private ICallBack.Stub callBack = new ICallBack.Stub() {
        @Override
        public void back(final Bundle bundle) throws RemoteException {
            String key = bundle.getString(BUNDLE_KEY);
            switch (key) {
                case PLAY_RECORDER_STATUS:
                    Message message = mHandler.obtainMessage(HANDLER_KEY_PALY_STATUS);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                    break;
            }
        }
    };

    private IWaveCallBack.Stub mWaveCallBack = new IWaveCallBack.Stub() {
        @Override
        public void back(Bundle bundle) throws RemoteException {
            Message message = mHandler.obtainMessage(HANDLER_KEY_PLAY_WAVE);
            message.setData(bundle);
            mHandler.sendMessage(message);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_audio_player);
        initBindService();
        initView();
        initEvn();
    }

    private void initBindService() {
        conn = new PlayerServiceConnection(mWaveCallBack);
        Intent intent = new Intent(this, RecorderService.class);
        intent.setAction(ServiceConstants.BIND_TYPE_PLAY);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void initView() {
        mTvPlayStart = findViewById(R.id.tv_play_status);
        mTvWaveData = findViewById(R.id.tv_wave_data);
        mBtnStartPlay = findViewById(R.id.btn_start_play);
        mBtnPausePlay = findViewById(R.id.btn_pause_play);
        mBtnResumePlay =  findViewById(R.id.btn_resume_play);
        mBtnStopPlay =  findViewById(R.id.btn_stop_play);
        mBtnBack = findViewById(R.id.btn_back);
        mBtnForward = findViewById(R.id.btn_forward);
    }

    private void initEvn() {
        mBtnStartPlay.setOnClickListener(this);
        mBtnPausePlay.setOnClickListener(this);
        mBtnResumePlay.setOnClickListener(this);
        mBtnStopPlay.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
        mBtnForward.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.btn_start_play) {
            startPlay();
        } else if (i == R.id.btn_pause_play) {
            pausePlay();
        } else if (i == R.id.btn_resume_play) {
            resumePlay();
        } else if (i == R.id.btn_stop_play) {
            stopPlay();
        } else if (i == R.id.btn_back) {
            backPlay();
        } else if (i == R.id.btn_forward) {
            forwardPlay();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
        }
    }

    private void startPlay() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/AAAPath/hello-123.wav";

        Bundle bundle = new Bundle();
        bundle.putString(FILE_PATH, path);
        conn.startPlay(bundle, callBack);
    }

    private void pausePlay() {
        conn.pausePlay( callBack);
    }

    private void resumePlay() {
        conn.resumePlay( callBack);
    }

    private void stopPlay() {
        conn.stopPlay( callBack);
    }

    private void forwardPlay() {
        conn.forwardPlay(1024 * 50);
    }

    private void backPlay() {
        conn.backPlay(1024 * 50);
    }
}
