package com.cfox.camera.utils;


import com.cfox.camera.log.EsLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CameraObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        EsLog.e("onError: " + e);
        throw new RuntimeException(e);

    }

    @Override
    public void onComplete() {

    }
}
