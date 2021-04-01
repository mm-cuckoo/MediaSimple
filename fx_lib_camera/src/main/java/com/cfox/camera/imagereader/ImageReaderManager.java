package com.cfox.camera.imagereader;

import android.media.ImageReader;

import com.cfox.camera.EsParams;


public interface ImageReaderManager {

    ImageReader createImageReader(EsParams esParams, ImageReaderProvider provider);

    void closeImageReaders();
}
