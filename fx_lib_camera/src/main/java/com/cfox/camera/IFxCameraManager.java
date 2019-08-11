package com.cfox.camera;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

import com.cfox.camera.controller.FxPhotoController;
import com.cfox.camera.controller.FxVideoController;

public interface IFxCameraManager {

    void addTextureView(TextureView textureView);

    void addSurfaceTexture(SurfaceTexture surfaceTexture);

    FxPhotoController photo();
    FxVideoController video();

}
