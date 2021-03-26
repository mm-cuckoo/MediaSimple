package com.cfox.camera.log;

import android.text.TextUtils;
import android.util.Log;

public class EsLog {

    private static String sTag = "";

    public static void setPrintTag(String tag) {
        sTag = "[" + tag + "]";
    }

    public static void d(String message) {
        String className = new Exception().getStackTrace()[1].getFileName();
        className = className.substring(0, className.indexOf("."));
        Log.d("[" + className + "]", printFormat(className, message));

    }

    public static void e(String message) {
        Thread thread = Thread.currentThread();
        String className = new Exception().getStackTrace()[1].getFileName();
        className = className.substring(0, className.indexOf("."));
        Log.e("[" + className + "]", printFormat(className, message));
    }

    public static void i(String message) {
        String className = new Exception().getStackTrace()[1].getFileName();
        className = className.substring(0, className.indexOf("."));
        Log.i("[" + className + "]", printFormat(className, message));
    }

    public static void w(String message) {
        String className = new Exception().getStackTrace()[1].getFileName();
        className = className.substring(0, className.indexOf("."));
        Log.w("[" + className + "]", printFormat(className, message));
    }

    private static String printFormat(String className, String message) {
        Thread thread = Thread.currentThread();
        StringBuilder buffer = new StringBuilder("  ");
        buffer.append("\n");
        buffer.append("================ Logcat ======================================================= ").append("\n");
        if (!TextUtils.isEmpty(sTag)) {
            buffer.append("== Tag :").append(sTag).append("\n");
        }
        buffer.append("== Class Name :").append(className).append("\n");
        buffer.append("== Thread Name :").append(thread.getName()).append("\n");
        buffer.append("== Message :").append(message).append("\n");
        buffer.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return buffer.toString();
    }
}
