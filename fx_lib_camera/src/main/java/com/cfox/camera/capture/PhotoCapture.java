package com.cfox.camera.capture;


import com.cfox.camera.utils.EsRequest;

public interface PhotoCapture extends Capture {
    void onCapture(EsRequest request);
}
