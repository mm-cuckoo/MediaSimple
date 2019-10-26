package com.cfox.camera.camera;

import android.media.ImageReader;

import com.cfox.camera.utils.FxRequest;

public interface IReaderHelper {

    ImageReader createImageReader(FxRequest request);
    ImageReader createPreviewImageReader(FxRequest request);
    void closeImageReaders();
}
