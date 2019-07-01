// IWaveCallBack.aidl
package com.chao.lib_audio;

import android.os.Bundle;

// Declare any non-default types here with import statements

interface IWaveCallBack {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
        oneway void back(in Bundle bundle);
}
