package com.chao.module_player;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chao.lib_common.arouter.RouterPath;
import com.chao.lib_common.base.BaseFragment;

@Route(path = RouterPath.MAIN_PLAYER_FG)
public class PlayerMainFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_main, null);
    }
}
