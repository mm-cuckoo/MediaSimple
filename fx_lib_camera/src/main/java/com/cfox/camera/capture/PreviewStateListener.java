package com.cfox.camera.capture;

public interface PreviewStateListener {
    void startFocus();

    void focusSuccess();

    void focusFailed();

    void autoFocus();

    void hideFocus();
}
