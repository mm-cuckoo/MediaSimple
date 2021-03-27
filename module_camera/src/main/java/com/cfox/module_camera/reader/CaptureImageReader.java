package com.cfox.module_camera.reader;

import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.util.Size;

import com.cfox.camera.imagereader.ImageReaderManagerImpl;
import com.cfox.camera.imagereader.ImageReaderProvider;
import com.cfox.camera.log.EsLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CaptureImageReader extends ImageReaderProvider {
    public CaptureImageReader() {
        super(TYPE.CAPTURE);
    }

    @Override
    public ImageReader createImageReader(Size previewSize, Size captureSize) {
        EsLog.d("createImageReader: captureSize width:" + captureSize.getWidth() + "  captureSize height:" + captureSize.getHeight());
        return ImageReader.newInstance(captureSize.getWidth(), captureSize.getHeight(), ImageFormat.JPEG, 2);
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        SimpleDateFormat format = new SimpleDateFormat("'/PIC'_yyyyMMdd_HHmmss'.jpeg'", Locale.getDefault());
        String fileName = format.format(new Date());
        String filePath = Environment.getExternalStorageDirectory().getAbsoluteFile().getPath();
        EsLog.d("createImageReader: pic file path:" + (filePath + fileName));
        new ImageSaver(reader.acquireNextImage(), new File(filePath + fileName)).run();
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
