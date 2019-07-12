package com.chao.module_mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chao.lib_common.arouter.RouterPath;
import com.chao.lib_common.base.BaseFragment;

@Route(path = RouterPath.MAIN_MINE_FG)
public class MineMainFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mine_main_fragment, null);
    }
}
