package com.chao.lib_common.base;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

public class BaseApplication extends Application {
    private boolean routerDebug = true;
    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        if (routerDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }
}
