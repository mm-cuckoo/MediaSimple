package com.cfox.camera.capture;


import com.cfox.camera.utils.EsParams;

public interface PhotoCapture extends Capture {
    void onCapture(EsParams esParams);
}
