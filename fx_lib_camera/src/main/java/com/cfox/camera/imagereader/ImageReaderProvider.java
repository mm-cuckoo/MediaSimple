package com.cfox.camera.imagereader;

import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.util.Size;

import java.nio.ByteBuffer;


public abstract class ImageReaderProvider implements ImageReader.OnImageAvailableListener {

    private final TYPE mType;

    public enum TYPE {
        CAPTURE,
        PREVIEW
    }

    public ImageReaderProvider(TYPE type) {
        this.mType = type;
    }

    public TYPE getType() {
        return mType;
    }

    public ImageReader onCreateImageReader(Size previewSize, Size captureSize, Handler handler) {
        ImageReader imageReader = createImageReader(previewSize, captureSize);
        imageReader.setOnImageAvailableListener(this, handler);
        return imageReader;
    }

    public byte[] getByteFromReader(ImageReader reader) {
        Image image = reader.acquireLatestImage();
        byte[] bytes = getByteFromImage(image);
        image.close();
        return bytes;
    }

    public byte[] getByteFromImage(Image image) {
        int totalSize = 0;
        for (Image.Plane plane : image.getPlanes()) {
            totalSize += plane.getBuffer().remaining();
        }
        ByteBuffer totalBuffer = ByteBuffer.allocate(totalSize);
        for (Image.Plane plane : image.getPlanes()) {
            totalBuffer.put(plane.getBuffer());
        }
        return totalBuffer.array();
    }


    public abstract ImageReader createImageReader(Size previewSize, Size captureSize);

    public abstract void onImageAvailable(ImageReader reader);
}
