package com.cfox.module_camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cfox.camera.capture.PreviewStateListener;
import com.cfox.camera.log.EsLog;
import com.cfox.lib_common.arouter.RouterPath;
import com.cfox.lib_common.base.BaseFragment;


@Route(path = RouterPath.MAIN_CAMERA_FG)
public class CameraMainFragment extends BaseFragment implements PreviewStateListener {
    private static final String TAG = "CameraMainFragment";
    private AutoFitTextureView mPreviewView;
    private SurfaceProviderImpl mSurfaceHelperImpl;
    private EsyCameraController mCameraController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCameraController = new EsyCameraController(getActivity());
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
        view.findViewById(R.id.btn_torch_flash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.torchFlash();
            }
        });

        view.findViewById(R.id.btn_on_flash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.onFlash();
            }
        });

        view.findViewById(R.id.btn_auto_flash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.autoFlash();
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
                mSurfaceHelperImpl = new SurfaceProviderImpl(mPreviewView);
                mCameraController.backCamera(mSurfaceHelperImpl, CameraMainFragment.this);
            }
        });
        view.findViewById(R.id.btn_open_font).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSurfaceHelperImpl = new SurfaceProviderImpl(mPreviewView);
                mCameraController.fontCamera(mSurfaceHelperImpl, CameraMainFragment.this);
            }
        });

        view.findViewById(R.id.btn_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.photoModule();
                mSurfaceHelperImpl = new SurfaceProviderImpl(mPreviewView);
                mCameraController.backCamera(mSurfaceHelperImpl, CameraMainFragment.this);
            }
        });

        view.findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.videoModule();
                mSurfaceHelperImpl = new SurfaceProviderImpl(mPreviewView);
                mCameraController.backCamera(mSurfaceHelperImpl, CameraMainFragment.this);
            }
        });

        view.findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraController.capture();
            }
        });

        ((SeekBar)view.findViewById(R.id.seek_ev)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showMsg("ev :" + progress);
                mCameraController.setEv(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((SeekBar)view.findViewById(R.id.seek_focus)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showMsg("zoom :" + progress);
                mCameraController.setZoom(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mSurfaceHelperImpl = new SurfaceProviderImpl(mPreviewView);
        EsLog.d("onResume: .......");
        mCameraController.backCamera(mSurfaceHelperImpl, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EsLog.d("onPause: ........");
        if (mCameraController == null) return;
        mCameraController.stopCamera();
    }

    private void showMsg(String msg) {
        Toast.makeText(getContext(), "" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startFocus() {

    }

    @Override
    public void focusSuccess() {

    }

    @Override
    public void focusFailed() {

    }

    @Override
    public void autoFocus() {

    }

    @Override
    public void hideFocus() {

    }
}
