package com.cfox.camera;

public interface FxCameraLifecycle {
    void onCreate();
    void onStart();
    void onRestart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
}
