package com.cfox.camera.log;

import android.util.Log;

public class EsLog {

    public static void d(String message) {
        Thread thread = Thread.currentThread();
        String classname = new Exception().getStackTrace()[1].getFileName();
        classname = classname.substring(0, classname.indexOf("."));
        Log.d("[" + classname + "]", "[" + thread.getName() + "]" + message);

    }

    public static void e(String message) {

    }

    public static void i(String message) {

    }
}
