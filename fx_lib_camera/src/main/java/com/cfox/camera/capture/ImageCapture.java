package com.cfox.camera.capture;


import com.cfox.camera.utils.EsRequest;

public interface ImageCapture extends BaseCapture {
    void onCapture(EsRequest request);
}
