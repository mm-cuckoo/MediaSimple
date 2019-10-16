package com.cfox.camera.camera;

import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.util.Log;

import com.cfox.camera.utils.FxRe;
import com.cfox.camera.utils.FxRequest;
import com.cfox.camera.utils.ThreadHandlerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ImageReaderHelper implements IReaderHelper {
    private static final String TAG = "ImageReaderHelper";

    private List<ImageReader> mImageReaders;
    private Handler mImageReaderHandler;
    ImageReaderHelper() {
        mImageReaders = new ArrayList<>();
        mImageReaderHandler = ThreadHandlerManager.getInstance().obtain(ThreadHandlerManager.Tag.T_TYPE_IMAGE_READER).getHandler();
    }

    @Override
    public ImageReader createImageReader(FxRequest request) {
        int picWidth = request.getInt(FxRe.Key.PIC_WIDTH);
        int picHeight = request.getInt(FxRe.Key.PIC_HEIGHT);
        int imageFormat = request.getInt(FxRe.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        final String filePath = request.getString(FxRe.Key.PIC_FILE_PATH);
        Log.d(TAG, "createImageReader: pic width:" + picWidth + "  pic height:" + picHeight  + "   format:" + imageFormat);
        Log.d(TAG, "createImageReader: pic file path:" + filePath);
        ImageReader imageReader = ImageReader.newInstance(picWidth, picHeight, imageFormat, 2);
        mImageReaders.add(imageReader);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                mImageReaderHandler.post(new ImageSaver(reader.acquireNextImage(), new File(filePath)));
            }
        }, mImageReaderHandler);
        return imageReader;
    }

    @Override
    public void closeImageReaders() {
        for (ImageReader imageReader : mImageReaders) {
            imageReader.close();
        }
        mImageReaders.clear();
    }

    private static class ImageSaver implements Runnable {

        private final Image mImage;
        private final File mFile;

        ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
