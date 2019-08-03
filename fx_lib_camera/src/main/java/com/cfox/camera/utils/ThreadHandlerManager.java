package com.cfox.camera.utils;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.HashMap;
import java.util.Map;

public class ThreadHandlerManager implements IThreadHandlerManager{

    private class ThreadWrapper {
        HandlerThread thread;
        Handler handler;
    }

    private Map<ThreadType, ThreadWrapper> mThreadWrapperMap;
    private ThreadHandlerManager() {
        mThreadWrapperMap = new HashMap<>(ThreadType.values().length);
    }

    @Override
    public Handler getThreadHandler(ThreadType type) {
        return null;
    }

    @Override
    public void release() {
        for (ThreadWrapper threadWrapper : mThreadWrapperMap.values()) {
            try {
                threadWrapper.thread.join();
                threadWrapper.thread = null;
                threadWrapper.handler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static IThreadHandlerManager getInstance() {
        return Create.threadHandlerManager;
    }

    private static class Create {
        static IThreadHandlerManager threadHandlerManager = new ThreadHandlerManager();
    }
}
