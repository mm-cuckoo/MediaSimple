package com.cfox.camera.utils;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CameraObserver<T> implements Observer<T> {
    private static final String TAG = "CameraObserver";
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "onError: " + e);

    }

    @Override
    public void onComplete() {

    }
}
