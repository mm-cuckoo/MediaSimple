package com.cfox.camera.log;

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
        Log.d("[" + classname + "]", sTag + "[" + thread.getName() + "]" + message);

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
