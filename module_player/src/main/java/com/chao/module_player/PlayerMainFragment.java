package com.chao.module_player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chao.lib_common.arouter.RouterPath;
import com.chao.lib_common.base.BaseFragment;


@Route(path = RouterPath.MAIN_PLAYER_FG)
public class PlayerMainFragment extends BaseFragment {
//
//    @BindView(R.id.btn_open_recorder_audio)
//    Button mBtnOpenRecorderAudio;
//    @BindView(R.id.btn_open_audio_player)
//    Button mBtnOpenPlayerAudio;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_main, null);
//        ButterKnife.bind(this, view);
        view.findViewById(R.id.btn_open_audio_player);
        return view;
    }
}
