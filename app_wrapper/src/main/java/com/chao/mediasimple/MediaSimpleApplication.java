package com.chao.mediasimple;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.chao.lib_common.base.BaseApplication;

public class MediaSimpleApplication extends BaseApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
