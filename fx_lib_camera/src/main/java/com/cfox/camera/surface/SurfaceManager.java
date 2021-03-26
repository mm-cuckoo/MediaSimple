package com.cfox.camera.surface;

import android.util.Size;
import android.view.Surface;

import java.util.ArrayList;
import java.util.List;

public class SurfaceManager {

    private final SurfaceProvider mSurfaceProvider;
    private final List<Surface> mReaderSurface;
    private final List<Surface> mPreviewSurface;

    public SurfaceManager(SurfaceProvider surfaceProvider) {
        this.mSurfaceProvider = surfaceProvider;
        this.mReaderSurface = new ArrayList<>();
        this.mPreviewSurface = new ArrayList<>();
    }

    public List<Surface> getPreviewSurface() {
        mPreviewSurface.add(mSurfaceProvider.getSurface());
        return mPreviewSurface;
    }

    public Class getPreviewSurfaceClass(){
        return mSurfaceProvider.getPreviewSurfaceClass();
    }


    public List<Surface> getTotalSurface() {
        List<Surface> surfaceList = new ArrayList<>(mReaderSurface);
        surfaceList.addAll(getPreviewSurface());
        return surfaceList;
    }

    public void addPreviewSurface(Surface surface) {
        mPreviewSurface.add(surface);
    }

    public void addReaderSurface(Surface surface) {
        mReaderSurface.add(surface);
    }

    public List<Surface> getReaderSurface() {
        return mReaderSurface;
    }

    public void setAspectRatio(Size size) {
        mSurfaceProvider.setAspectRatio(size);
    }

    public boolean isAvailable() {
        return mSurfaceProvider.isAvailable();
    }

}
