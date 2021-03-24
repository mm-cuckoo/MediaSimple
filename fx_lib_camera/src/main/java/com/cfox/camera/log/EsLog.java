package com.cfox.camera.log;

import android.text.TextUtils;
import android.util.Log;

public class EsLog {

    private static String sTag = "";

    public static void setPrintTag(String tag) {
        sTag = "[" + tag + "]";
    }

    public static void d(String message) {
        Thread thread = Thread.currentThread();
        String classname = new Exception().getStackTrace()[1].getFileName();
        classname = classname.substring(0, classname.indexOf("."));
        StringBuilder buffer = new StringBuilder("  ");
        buffer.append("\n");
        buffer.append("================ Logcat ======================================================= ").append("\n");
        if (!TextUtils.isEmpty(sTag)) {
            buffer.append("== Tag :").append(sTag).append("\n");
        }
        buffer.append("== Class Name :").append(classname).append("\n");
        buffer.append("== Thread Name :").append(thread.getName()).append("\n");
        buffer.append("== Message :").append(message).append("\n");
        buffer.append("============================================================================================");

        Log.d("[" + classname + "]", buffer.toString());

    }

    public static void e(String message) {
        Thread thread = Thread.currentThread();
        String classname = new Exception().getStackTrace()[1].getFileName();
        classname = classname.substring(0, classname.indexOf("."));
        Log.e("[" + classname + "]", sTag + "[" + thread.getName() + "]" + message);
    }

    public static void i(String message) {
        Thread thread = Thread.currentThread();
        String classname = new Exception().getStackTrace()[1].getFileName();
        classname = classname.substring(0, classname.indexOf("."));
        Log.i("[" + classname + "]", sTag + "[" + thread.getName() + "]" + message);
    }

    public static void w(String message) {
        Thread thread = Thread.currentThread();
        String classname = new Exception().getStackTrace()[1].getFileName();
        classname = classname.substring(0, classname.indexOf("."));
        Log.w("[" + classname + "]", "[" + thread.getName() + "]" + message);
    }
}
