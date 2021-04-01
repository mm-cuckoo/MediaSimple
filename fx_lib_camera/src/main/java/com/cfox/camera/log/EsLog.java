package com.cfox.camera.log;

import android.text.TextUtils;
import android.util.Log;

public class EsLog {

    private static String sTag = "";

    public static void setPrintTag(String tag) {
        sTag = tag;
    }

    public static void d(String message) {
        String className = new Exception().getStackTrace()[1].getFileName();
        className = className.substring(0, className.indexOf("."));
        Log.d(printTagFormat(className),message);
    }

    public static void e(String message) {
        String className = new Exception().getStackTrace()[1].getFileName();
        className = className.substring(0, className.indexOf("."));
        Log.e(printTagFormat(className),message);
    }

    public static void i(String message) {
        String className = new Exception().getStackTrace()[1].getFileName();
        className = className.substring(0, className.indexOf("."));
        Log.i(printTagFormat(className),message);
    }

    public static void w(String message) {
        String className = new Exception().getStackTrace()[1].getFileName();
        className = className.substring(0, className.indexOf("."));
        Log.w(printTagFormat(className),message);
    }

    private static String printTagFormat(String className) {
        Thread thread = Thread.currentThread();
        StringBuilder buffer = new StringBuilder();
        buffer.append("[").append(thread.getName()).append("]");
        buffer.append("[").append(className).append("]");
        if (!TextUtils.isEmpty(sTag)) {
            buffer.append("[").append(sTag).append("]");
        }
        return buffer.toString();
    }
}
