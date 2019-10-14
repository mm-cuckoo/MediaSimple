package com.cfox.camera.camera;

import android.media.ImageReader;

public interface IReaderHelper {

    ImageReader createImageReader(int width, int height, int format, int maxImages);
    void closeImageReaders();
}
