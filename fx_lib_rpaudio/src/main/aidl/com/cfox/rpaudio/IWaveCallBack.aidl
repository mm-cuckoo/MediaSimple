// IWaveCallBack.aidl
package com.cfox.rpaudio;

import android.os.Bundle;

// Declare any non-default types here with import statements

interface IWaveCallBack {
    /**
     * Demonstrates some b0asic types that you can use as parameters
     * and return values in AIDL.
     */
        oneway void back(in Bundle bundle);
}
