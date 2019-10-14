package com.cfox.camera.camera;

import android.media.ImageReader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ImageReaderHelper implements IReaderHelper {
    private static final String TAG = "ImageReaderHelper";

    private List<ImageReader> mImageReaders;
    ImageReaderHelper() {
        mImageReaders = new ArrayList<>();
    }

    @Override
    public ImageReader createImageReader(int width, int height, int format, int maxImages) {
        Log.d(TAG, "createImageReader: width:" + width  + "  height:" + height + "  format:" + format  + "  maxImage:" + maxImages);
        ImageReader imageReader = ImageReader.newInstance(width, height, format, maxImages);
        mImageReaders.add(imageReader);
        return imageReader;
    }

    @Override
    public void closeImageReaders() {
        for (ImageReader imageReader : mImageReaders) {
            imageReader.close();
        }
        mImageReaders.clear();
    }
}
