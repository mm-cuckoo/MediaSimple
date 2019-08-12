package com.cfox.camera;

public class FxCameraLifecycleImpl implements FxCameraLifecycle {

    private FxCameraLifecycle mLifecycle;

    FxCameraLifecycleImpl(FxCameraLifecycle lifecycle) {
        this.mLifecycle = lifecycle;
    }

    @Override
    public void onCreate() {
        mLifecycle.onCreate();
    }

    @Override
    public void onStart() {
        mLifecycle.onStart();
    }

    @Override
    public void onRestart() {
        mLifecycle.onRestart();
    }

    @Override
    public void onResume() {
        mLifecycle.onResume();
    }

    @Override
    public void onPause() {
        mLifecycle.onPause();
    }

    @Override
    public void onStop() {
        mLifecycle.onStop();
    }

    @Override
    public void onDestroy() {
        mLifecycle.onDestroy();
    }
}
