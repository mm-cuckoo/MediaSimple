package com.cfox.module_camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cfox.lib_common.arouter.RouterPath;
import com.cfox.lib_common.base.BaseFragment;

@Route(path = RouterPath.MAIN_CAMERA_FG)
public class CameraMainFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera_main, container, false);
    }
}
