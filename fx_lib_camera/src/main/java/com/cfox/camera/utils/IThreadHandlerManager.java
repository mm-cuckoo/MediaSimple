package com.cfox.camera.utils;

import android.os.Handler;

public interface IThreadHandlerManager {
    enum ThreadType {
        T_TYPE_MAIN,
        T_TYPE_LOAD
    }
    Handler getThreadHandler(ThreadType type);

    void release();
}
