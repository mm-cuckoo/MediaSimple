package com.cfox.camera.capture;

public interface CaptureStateListener {

    void onCaptureStarted();

    void onCaptureCompleted();

    void onCaptureFailed();
}
