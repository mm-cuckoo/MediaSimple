package com.cfox.camera.capture;

import android.graphics.Rect;

import com.cfox.camera.camera.info.CameraInfoManager;
import com.cfox.camera.log.EsLog;

public class ZoomHelper {
    private CameraInfoManager mCameraInfoManager;
    private float mMaxZoomValue;
    private Rect mZoomRect;

    public ZoomHelper(CameraInfoManager manager) {
        this.mCameraInfoManager = manager;

    }

    public Rect getZoomRect(float zoomValue) {
        this.mMaxZoomValue = mCameraInfoManager.getMaxZoom() * 10;
        this.mZoomRect = mCameraInfoManager.getActiveArraySize();
        if (zoomValue > mMaxZoomValue) {
            zoomValue = mMaxZoomValue;
        }

        if (zoomValue < 1) {
            zoomValue = 1;
        }

        int minW = (int) (mZoomRect.width()/mMaxZoomValue);
        int minH = (int) (mZoomRect.height()/mMaxZoomValue);
        int difW = mZoomRect.width() - minW;
        int difH = mZoomRect.height() - minH;
        int cropW = difW /100 *(int)zoomValue;
        int cropH = difH /100 *(int)zoomValue;
        cropW -= cropW & 3;
        cropH -= cropH & 3;
        Rect zoom = new Rect(cropW, cropH, mZoomRect.width() - cropW, mZoomRect.height() - cropH);

        EsLog.d("zoom value :" + zoomValue  + " zoom rect:" + zoom);
        return zoom;
    }
}
