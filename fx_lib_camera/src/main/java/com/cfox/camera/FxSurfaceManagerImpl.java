package com.cfox.camera;

import android.view.TextureView;

import com.cfox.camera.surface.SurfaceHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FxSurfaceManagerImpl implements FxSurfaceManager {
    private Map<String, SurfaceHelper> mSurfaceHelper = new HashMap<>();
    @Override
    public FxSurfaceManager init() {
        mSurfaceHelper.clear();
        return this;
    }

    @Override
    public FxSurfaceManager addTextureView(TextureView textureView) {
        mSurfaceHelper.put(String.valueOf(mSurfaceHelper.size()), new SurfaceHelper(textureView));
        return this;
    }

    @Override
    public FxSurfaceManager addTextureView(TextureView textureView, String tag) {
        mSurfaceHelper.put(tag, new SurfaceHelper(textureView));
        return this;
    }

    @Override
    public boolean hasSurface() {
        return mSurfaceHelper.size() > 0;
    }

    @Override
    public List<SurfaceHelper> getSurfaceHelpers() {
        return null;
    }

    @Override
    public SurfaceHelper getSurfaceHelper(String tag) {
        return mSurfaceHelper.get(tag);
    }
}
