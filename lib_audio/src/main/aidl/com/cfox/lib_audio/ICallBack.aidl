// ICallBack.aidl
package com.cfox.lib_audio;

// Declare any non-default types here with import statements
import android.os.Bundle;

interface ICallBack {
    
    oneway void back(in Bundle bundle);
}
