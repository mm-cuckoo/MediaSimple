package com.cfox.camera.capture;


public interface PhotoCapture extends Capture {
    void onCapture(CaptureStateListener listener);
}
