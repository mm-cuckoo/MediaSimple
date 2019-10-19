package com.cfox.module_camera;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cfox.camera.AutoFitTextureView;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.lib_common.arouter.RouterPath;
import com.cfox.lib_common.base.BaseFragment;


@Route(path = RouterPath.MAIN_CAMERA_FG)
public class CameraMainFragment extends BaseFragment {
    private static final String TAG = "CameraMainFragment";
    private AutoFitTextureView mPreviewView;
    private SurfaceHelper mSurfaceHelper;
    private CameraController mCameraController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCameraController = new CameraController();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPreviewView = view.findViewById(R.id.preview_view);

        view.findViewById(R.id.btn_open_flash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.openFlash();
//                mCameraController.capture();
            }
        });

        view.findViewById(R.id.btn_close_flash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.closeFlash();
            }
        });

        view.findViewById(R.id.btn_open_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSurfaceHelper = new SurfaceHelper(mPreviewView);
                mCameraController.backCamera(mSurfaceHelper);
            }
        });
        view.findViewById(R.id.btn_open_font).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSurfaceHelper = new SurfaceHelper(mPreviewView);
                mCameraController.fontCamera(mSurfaceHelper);
            }
        });

        view.findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.capture();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mSurfaceHelper = new SurfaceHelper(mPreviewView);
        Log.d(TAG, "onResume: .......");
        mCameraController.backCamera(mSurfaceHelper);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ........");
        if (mCameraController == null) return;
        mCameraController.stopCamera();
    }
}
