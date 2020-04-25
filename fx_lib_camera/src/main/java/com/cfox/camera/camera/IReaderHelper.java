package com.cfox.camera.camera;

import android.media.ImageReader;

import com.cfox.camera.utils.EsRequest;

public interface IReaderHelper {

    ImageReader createImageReader(EsRequest request);

    ImageReader createPreviewImageReader(EsRequest request);

    void closeImageReaders();
}
