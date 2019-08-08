package com.cfox.camera.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;

public class ThreadHandlerManager {
    public enum Tag {
        T_TYPE_LOAD("load");
        protected final String tag;
        Tag(String tag) {
            this.tag = tag;
        }
    }

    private Map<Tag, MyHandlerThread> mHandlerThreadMap;
    private ThreadHandlerManager() {
        mHandlerThreadMap = new HashMap<>(Tag.values().length);
        for (Tag tag : Tag.values()) {
            mHandlerThreadMap.put(tag, new MyHandlerThread(tag));
        }
    }

    public MyHandlerThread obtain(Tag tag) {
        if (mHandlerThreadMap.containsKey(tag)) {
            return mHandlerThreadMap.get(tag);
        }

        return null;
    }

    public void release() {
        for (MyHandlerThread handlerThread : mHandlerThreadMap.values()) {
            handlerThread.stop();
        }
    }

    public static ThreadHandlerManager getInstance() {
        return Create.threadHandlerManager;
    }

    private static class Create {
        static ThreadHandlerManager threadHandlerManager = new ThreadHandlerManager();
    }

    public class MyHandlerThread {
        HandlerThread mHandlerThread;
        Handler mHandler;

        MyHandlerThread(Tag tag) {
            mHandlerThread = new HandlerThread(tag.tag);
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper());
        }

        public Looper getLooper() {
            return mHandler.getLooper();
        }

        public Thread getThread() {
            return mHandler.getLooper().getThread();
        }

        public Handler getHandler() { return mHandler; }

        private void stop() {
            mHandlerThread.quitSafely();

            try {
                mHandlerThread.join();
                mHandlerThread = null;
                mHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
