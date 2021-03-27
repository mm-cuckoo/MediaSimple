package com.cfox.module_camera.reader;

import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.util.Size;

import com.cfox.camera.imagereader.ImageReaderProvider;
import com.cfox.camera.log.EsLog;

public class PreviewImageReader extends ImageReaderProvider {
    public PreviewImageReader() {
        super(TYPE.PREVIEW);
    }

    @Override
    public ImageReader createImageReader(Size previewSize, Size captureSize) {
        EsLog.d("createImageReader: previewSize width:" + previewSize.getWidth() + "  previewSize height:" + previewSize.getHeight());
        return ImageReader.newInstance(previewSize.getWidth(), previewSize.getHeight(), ImageFormat.YUV_420_888, 2);
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        EsLog.d("onImageAvailable: preview frame ....");
        Image image = reader.acquireNextImage();
        if (image == null) return;
        image.close();
    }
}
