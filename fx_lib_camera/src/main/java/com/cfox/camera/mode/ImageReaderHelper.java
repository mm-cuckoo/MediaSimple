package com.cfox.camera.mode;

import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.util.Size;

import com.cfox.camera.log.EsLog;
import com.cfox.camera.utils.Es;
import com.cfox.camera.utils.EsRequest;
import com.cfox.camera.utils.WorkerHandlerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImageReaderHelper implements IReaderHelper {

    private List<ImageReader> mImageReaders;
    private Handler mImageReaderHandler;
    public ImageReaderHelper() {
        mImageReaders = new ArrayList<>();
        mImageReaderHandler = WorkerHandlerManager.getHandler(WorkerHandlerManager.Tag.T_TYPE_IMAGE_READER);
    }

    @Override
    public ImageReader createImageReader(EsRequest request) {
        Size picSize = (Size) request.getObj(Es.Key.PIC_SIZE);
        int imageFormat = request.getInt(Es.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        final String filePath = request.getString(Es.Key.PIC_FILE_PATH);
        EsLog.d("createImageReader: pic width:" + picSize.getWidth() + "  pic height:" + picSize.getHeight()  + "   format:" + imageFormat);
        ImageReader imageReader = ImageReader.newInstance(picSize.getWidth(), picSize.getHeight(), imageFormat, 2);
        mImageReaders.add(imageReader);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                SimpleDateFormat format = new SimpleDateFormat("'/PIC'_yyyyMMdd_HHmmss'.jpeg'", Locale.getDefault());
                String fileName = format.format(new Date());
                EsLog.d("createImageReader: pic file path:" + (filePath + fileName));
                mImageReaderHandler.post(new ImageSaver(reader.acquireNextImage(), new File(filePath + fileName)));
            }
        }, mImageReaderHandler);
        return imageReader;
    }

    @Override
    public ImageReader createPreviewImageReader(EsRequest request) {
        Size picSize = (Size) request.getObj(Es.Key.PREVIEW_SIZE);
        int imageFormat = request.getInt(Es.Key.IMAGE_FORMAT, ImageFormat.JPEG);
        EsLog.d("createImageReader: pic width:" + picSize.getWidth() + "  pic height:" + picSize.getHeight()  + "   format:" + imageFormat);
        ImageReader imageReader = ImageReader.newInstance(picSize.getWidth(), picSize.getHeight(), ImageFormat.YUV_420_888, 2);
        mImageReaders.add(imageReader);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
//                EsLog.d("onImageAvailable: preview frame ....");
                Image image = reader.acquireNextImage();
                if (image == null) return;
                image.close();
            }
        }, mImageReaderHandler);
        return imageReader;
    }

    byte[] getByteFromReader(ImageReader reader) {
        Image image = reader.acquireLatestImage();
        int totalSize = 0;
        for (Image.Plane plane : image.getPlanes()) {
            totalSize += plane.getBuffer().remaining();
        }
        ByteBuffer totalBuffer = ByteBuffer.allocate(totalSize);
        for (Image.Plane plane : image.getPlanes()) {
            totalBuffer.put(plane.getBuffer());
        }
        image.close();
        return totalBuffer.array();
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
