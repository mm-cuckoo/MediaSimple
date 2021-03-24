package com.cfox.camera.surface;

import android.util.Size;
import android.view.Surface;

import java.util.ArrayList;
import java.util.List;

public class SurfaceManager {

    private final SurfaceProvider mSurfaceProvider;
    private final List<Surface> mReaderSurface;

    public SurfaceManager(SurfaceProvider surfaceProvider) {
        this.mSurfaceProvider = surfaceProvider;
        this.mReaderSurface = new ArrayList<>();
    }

    public Surface getPreviewSurface() {
        return mSurfaceProvider.getSurface();
    }

    public Class getPreviewSurfaceClass(){
        return mSurfaceProvider.getPreviewSurfaceClass();
    }


    public List<Surface> getTotalSurface() {
        List<Surface> surfaceList = new ArrayList<>(mReaderSurface);
        surfaceList.add(mSurfaceProvider.getSurface());
        return surfaceList;
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
