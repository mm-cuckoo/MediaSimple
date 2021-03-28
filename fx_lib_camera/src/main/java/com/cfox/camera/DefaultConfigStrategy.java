package com.cfox.camera;

import android.util.Size;

import com.cfox.camera.log.EsLog;

import java.util.Arrays;

public class DefaultConfigStrategy implements ConfigStrategy {
    @Override
    public Size getPreviewSize(Size size, Size[] supportSizes) {
        EsLog.d("getPreviewSize: size:" + size  + "   supportSize:" + Arrays.toString(supportSizes));
        Size resultSize = null;
        Size sizeTmp = size;
        for (Size size1 : supportSizes) {
            if (size.getWidth() == size1.getWidth()) {
                if (size.getHeight() == size1.getHeight()) {
                    resultSize = size1;
                    break;
                } else  {
                    if (Math.abs(size1.getHeight() - sizeTmp.getHeight()) < Math.abs(size.getHeight() - sizeTmp.getHeight())) {
                        sizeTmp = size1;
                    }
                }
            }
        }

        if (resultSize == null) {
            resultSize = sizeTmp;
        }

        return resultSize;
    }

    @Override
    public Size getPictureSize(Size size, Size[] supportSizes) {
        EsLog.d( "getPictureSize: size:" + size  + "   supportSize:" + Arrays.toString(supportSizes));
        Size resultSize = null;
        Size sizeTmp = size;
        for (Size size1 : supportSizes) {
            if (size.getWidth() == size1.getWidth()) {
                if (size.getHeight() == size1.getHeight()) {
                    resultSize = size1;
                    break;
                } else  {
                    if (Math.abs(size1.getHeight() - sizeTmp.getHeight()) < Math.abs(size.getHeight() - sizeTmp.getHeight())) {
                        sizeTmp = size1;
                    }
                }
            }
        }

        if (resultSize == null) {
            resultSize = sizeTmp;
        }
        EsLog.d("getPictureSize: return picture size:" + resultSize);
        return resultSize;
    }

    @Override
    public int getPictureOrientation(int cameraSensorOrientation) {
        return cameraSensorOrientation;
    }
}
