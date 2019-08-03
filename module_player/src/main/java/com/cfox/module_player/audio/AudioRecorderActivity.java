package com.cfox.module_player.audio;

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

import com.cfox.rpaudio.ICallBack;
import com.cfox.rpaudio.IWaveCallBack;
import com.cfox.rpaudio.log.FxLog;
import com.cfox.rpaudio.recorder.RecorderConstants;
import com.cfox.rpaudio.recorder.wav.audio.AudioStatus;
import com.cfox.rpaudio.service.RecorderService;
import com.cfox.rpaudio.service.ServiceConstants;
import com.cfox.rpaudio.service.connection.RecorderServiceConnection;
import com.cfox.lib_common.base.BaseActivity;
import com.cfox.module_player.Constants;
import com.cfox.module_player.R;

public class AudioRecorderActivity extends BaseActivity implements View.OnClickListener,
        ServiceConstants , RecorderConstants, Constants, AudioStatus {
    private static final String TAG = "AudioRecorderActivity";

    private TextView mTvRecorderStatusView;
    private TextView mTvWaveData;
    private Button mBtnStart;
    private Button mBtnPause;
    private Button mBtnResume;
    private Button mBtnStop;

    private RecorderServiceConnection conn;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case HANDLER_KEY_RECORDER_STATUS:
                    recorderStatusChange(msg.getData());
                    break;

                case HANDLER_KEY_RECORDER_WAVE:
                    recorderWaveData(msg.getData());
                    break;

            }

        }
    };

    private void recorderWaveData(Bundle data) {
        byte[] waveByte = data.getByteArray(RECORDER_WAVE_DATE);
        FxLog.d(TAG, "waveByte length:" + waveByte.length);
    }

    private void recorderStatusChange(Bundle data) {
        int recorderStatus = data.getInt(RECORDER_STATUS);
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

        mTvRecorderStatusView.setText(status);
    }


    private ICallBack.Stub callBack = new ICallBack.Stub() {
        @Override
        public void back(final Bundle bundle) throws RemoteException {

            String key = bundle.getString(BUNDLE_KEY);

            switch (key) {
                case RECORDER_STATUS:
                    Message message = mHandler.obtainMessage(HANDLER_KEY_RECORDER_STATUS);
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                    break;
            }
        }
    };

    private IWaveCallBack.Stub mWaveCallBack = new IWaveCallBack.Stub() {
        @Override
        public void back(Bundle bundle) throws RemoteException {
            Message message = mHandler.obtainMessage(HANDLER_KEY_RECORDER_WAVE);
            message.setData(bundle);
            mHandler.sendMessage(message);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_audio_recorder);
        initBindService();
        initView();
        initEvn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseRecorder();
    }

    private void initBindService() {
        conn = new RecorderServiceConnection(mWaveCallBack);
        Intent intent = new Intent(this, RecorderService.class);
        intent.setAction(ServiceConstants.BIND_TYPE_RECORDER);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void initView() {
        mTvRecorderStatusView = (TextView) findViewById(R.id.tv_recorder_status);
        mTvWaveData = (TextView) findViewById(R.id.tv_wave_data);
        mBtnStart = (Button) findViewById(R.id.btn_start_recorder);
        mBtnPause = (Button) findViewById(R.id.btn_pause_recorder);
        mBtnResume = (Button) findViewById(R.id.btn_resume_recorder);
        mBtnStop = (Button) findViewById(R.id.btn_stop_recorder);
    }

    private void initEvn() {
        mBtnStart.setOnClickListener(this);
        mBtnPause.setOnClickListener(this);
        mBtnResume.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_start_recorder) {
            startRecorder();
        } else if (i == R.id.btn_pause_recorder) {
            pauseRecorder();
        } else if (i == R.id.btn_resume_recorder) {
            resumeRecorder();
        } else if (i == R.id.btn_stop_recorder) {
            stopRecorder();
        }
    }

    private void startRecorder() {
        String path = Environment.getExternalStorageDirectory().getPath();
//        conn.starRecorder(callBack);
        conn.starRecorder(path + "/AAAPath/","hello", callBack);
    }

    private void pauseRecorder() {
        conn.pauseRecorder(null, callBack);
    }

    private void resumeRecorder() {
        conn.resumeRecorder(null, callBack);
    }

    private void stopRecorder() {
        conn.stopRecorder(null, callBack);
    }
}
