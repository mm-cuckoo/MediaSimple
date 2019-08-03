package com.cfox.lib_common.widegts;

import android.widget.Toast;

import com.cfox.lib_common.base.BaseApplication;

public class ToastUtils {

    private static Toast sToast;

    public static void toastShow(String content) {
        toastShow(content, Toast.LENGTH_SHORT);
    }

    public static void toastShow(String content, int duration) {
        if (sToast != null)
            sToast.cancel();
        sToast = Toast.makeText(BaseApplication.sContext, content, duration);
        sToast.setText(content);
        sToast.show();
    }
}