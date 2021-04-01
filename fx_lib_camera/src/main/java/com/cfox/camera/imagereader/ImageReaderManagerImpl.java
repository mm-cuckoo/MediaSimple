package com.cfox.camera.imagereader;

import android.media.ImageReader;
import android.os.Handler;
import android.util.Size;

import com.cfox.camera.EsParams;
import com.cfox.camera.utils.WorkerHandlerManager;

import java.util.ArrayList;
import java.util.List;

public class ImageReaderManagerImpl implements ImageReaderManager {

    private final List<ImageReader> mImageReaders;
    private final Handler mImageReaderHandler;
    public ImageReaderManagerImpl() {
        mImageReaders = new ArrayList<>();
        mImageReaderHandler = WorkerHandlerManager.getHandler(WorkerHandlerManager.Tag.T_TYPE_IMAGE_READER);
    }

    @Override
    public ImageReader createImageReader(EsParams esParams, ImageReaderProvider provider) {
        Size picSize = esParams.get(EsParams.Key.PIC_SIZE);
        Size previewSize = esParams.get(EsParams.Key.PREVIEW_SIZE);
        ImageReader imageReader = provider.onCreateImageReader(previewSize, picSize, mImageReaderHandler);
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
