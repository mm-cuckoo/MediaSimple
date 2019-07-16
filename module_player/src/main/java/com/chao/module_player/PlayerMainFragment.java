package com.chao.module_player;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chao.lib_common.arouter.RouterPath;
import com.chao.lib_common.base.BaseFragment;
import com.chao.module_player.audio.AudioPlayerActivity;
import com.chao.module_player.audio.AudioRecorderActivity;


@Route(path = RouterPath.MAIN_PLAYER_FG)
public class PlayerMainFragment extends BaseFragment implements View.OnClickListener{
    Button mBtnOpenRecorderAudio;
    Button mBtnOpenPlayerAudio;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_main, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        mBtnOpenRecorderAudio = view.findViewById(R.id.btn_open_audio_player);
        mBtnOpenPlayerAudio = view.findViewById(R.id.btn_open_audio_recorder);
        mBtnOpenRecorderAudio.setOnClickListener(this);
        mBtnOpenPlayerAudio.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_open_audio_player:
                intent = new Intent(getContext(), AudioPlayerActivity.class);
                break;
            case R.id.btn_open_audio_recorder:
                intent = new Intent(getContext(), AudioRecorderActivity.class);
                break;
        }

        if (intent == null) return;
        startActivity(intent);
    }
}
