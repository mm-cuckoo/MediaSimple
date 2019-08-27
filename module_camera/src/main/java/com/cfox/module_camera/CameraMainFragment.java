package com.cfox.module_camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cfox.camera.FxCamera;
import com.cfox.camera.FxCameraImpl;
import com.cfox.lib_common.arouter.RouterPath;
import com.cfox.lib_common.base.BaseFragment;

@Route(path = RouterPath.MAIN_CAMERA_FG)
public class CameraMainFragment extends BaseFragment {

    private TextureView mPreviewView;
    private Button mBtnOpenDevice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPreviewView = view.findViewById(R.id.preview_view);
        mBtnOpenDevice = view.findViewById(R.id.btn_open_device);
        mBtnOpenDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FxCameraImpl.getInstance().getCameraManager().photo();
            }
        });


    }
}