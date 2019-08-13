package com.cfox.camera;

import android.view.TextureView;

import com.cfox.camera.surface.SurfaceHelper;

import java.util.List;

public interface FxSurfaceManager {
    FxSurfaceManager init();
    FxSurfaceManager addTextureView(TextureView textureView);
    FxSurfaceManager addTextureView(TextureView textureView, String tag);
    boolean hasSurface();
    List<SurfaceHelper> getSurfaceHelpers();
    SurfaceHelper getSurfaceHelper(String tag);
}
