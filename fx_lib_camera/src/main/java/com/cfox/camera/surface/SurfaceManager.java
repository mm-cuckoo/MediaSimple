package com.cfox.camera.surface;

import android.util.Size;
import android.view.Surface;

import com.cfox.camera.log.EsLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 进行 Surface 管理。
 *
 * 对camera 输出的surface 进行管理，包括预览 surface 和 capture surface
 */
public class SurfaceManager {

    private final SurfaceProvider mSurfaceProvider;
    private final List<Surface> mCaptureSurface;
    private final List<Surface> mPreviewSurface;

    /**
     * 这个 surfaceProvider 是提供预览的 surfaceProvider
     * @param surfaceProvider
     */
    public SurfaceManager(SurfaceProvider surfaceProvider) {
        this.mSurfaceProvider = surfaceProvider;
        this.mCaptureSurface = new ArrayList<>();
        this.mPreviewSurface = new ArrayList<>();
    }

    /**
     * 只获取预览surface list
     * @return
     */
    public List<Surface> getPreviewSurface() {
        mPreviewSurface.add(mSurfaceProvider.getSurface());
        return mPreviewSurface;
    }

    public Class getPreviewSurfaceClass(){
        return mSurfaceProvider.getPreviewSurfaceClass();
    }


    /**
     * 获取所有surface list , 包括预览 surface 和 capture surface
     * @return
     */
    public List<Surface> getTotalSurface() {
        List<Surface> surfaceList = new ArrayList<>(mCaptureSurface);
        surfaceList.addAll(getPreviewSurface());
        if (surfaceList.size() == 0) {
            EsLog.e("getTotalSurface surface list size is 0");
        }
        return surfaceList;
    }

    public void addPreviewSurface(Surface surface) {
        mPreviewSurface.add(surface);
    }

    public void addCaptureSurface(Surface surface) {
        mCaptureSurface.add(surface);
    }

    public List<Surface> getReaderSurface() {
        return mCaptureSurface;
    }

    public void setAspectRatio(Size size) {
        mSurfaceProvider.setAspectRatio(size);
    }

    public boolean isAvailable() {
        return mSurfaceProvider.isAvailable();
    }

}
