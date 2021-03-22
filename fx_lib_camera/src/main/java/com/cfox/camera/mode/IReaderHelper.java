package com.cfox.camera.mode;

import android.media.ImageReader;

import com.cfox.camera.utils.EsRequest;

public interface IReaderHelper {

    ImageReader createImageReader(EsRequest request);

    ImageReader createPreviewImageReader(EsRequest request);

    void closeImageReaders();
}
