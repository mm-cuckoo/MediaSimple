package com.cfox.camera.surface;

import android.util.Size;
import android.view.Surface;

import com.cfox.camera.utils.EsResult;

import java.util.List;

import io.reactivex.Observable;

public interface ISurfaceHelper {
    Surface getSurface();

    Observable<EsResult> isAvailable();

    List<Surface> getAllSurfaces();

    List<Surface> getCaptureSurfaces();

    void addCaptureSurface(Surface surface);

    Class getPreviewSurfaceClass();

    void setAspectRatio(Size size);
}
