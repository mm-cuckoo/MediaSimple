package com.cfox.camera;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

import com.cfox.camera.controller.FxPhotoController;
import com.cfox.camera.controller.FxVideoController;

public class FxCameraManagerImpl implements FxCameraManager {
    @Override
    public void addTextureView(TextureView textureView) {

    }

    @Override
    public void addSurfaceTexture(SurfaceTexture surfaceTexture) {

    }
    @Override
    public FxPhotoController photo() {
        return null;
    }

    @Override
    public FxVideoController video() {
        return null;
    }
}
