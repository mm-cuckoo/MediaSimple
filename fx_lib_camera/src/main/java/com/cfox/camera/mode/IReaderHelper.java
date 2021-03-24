package com.cfox.camera.mode;

import android.media.ImageReader;

import com.cfox.camera.utils.EsParams;

public interface IReaderHelper {

    ImageReader createImageReader(EsParams esParams);

    ImageReader createPreviewImageReader(EsParams esParams);

    void closeImageReaders();
}
