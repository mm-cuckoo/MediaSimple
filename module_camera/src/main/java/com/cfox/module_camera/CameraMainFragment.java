package com.cfox.module_camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cfox.camera.surface.SurfaceHelper;
import com.cfox.lib_common.arouter.RouterPath;
import com.cfox.lib_common.base.BaseFragment;

import java.util.Objects;

@Route(path = RouterPath.MAIN_CAMERA_FG)
public class CameraMainFragment extends BaseFragment {
    private static final String TAG = "CameraMainFragment";
    private TextureView mPreviewView;
    private Button mBtnOpenDevice;
    private SurfaceHelper mSurfaceHelper;
    private CameraController mCameraController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCameraController = new CameraController(Objects.requireNonNull(getContext()));
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
        mSurfaceHelper = new SurfaceHelper(mPreviewView);
        mBtnOpenDevice = view.findViewById(R.id.btn_open_device);
        mBtnOpenDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mCameraController.openPreview(mSurfaceHelper);
                openCameraDevice("1");
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void openCameraDevice(String cameraId) {
        CameraManager cameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    Log.d(TAG, "openCameraDevice  onOpened: ");
                    createSession(camera);
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    Log.d(TAG, "openCameraDevice  onDisconnected: ");

                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    Log.d(TAG, "openCameraDevice  onError: ");

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }


    private void createSession(CameraDevice device) {
    }
}
