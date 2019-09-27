package com.cfox.camera;

import android.view.TextureView;

import com.cfox.camera.surface.SurfaceHelper;

import java.util.List;

public interface IFxSurfaceManager {
    IFxSurfaceManager init();
    IFxSurfaceManager addTextureView(TextureView textureView);
    IFxSurfaceManager addTextureView(TextureView textureView, String tag);
    boolean hasSurface();
    List<SurfaceHelper> getSurfaceHelpers();
    SurfaceHelper getSurfaceHelper(String tag);
}
