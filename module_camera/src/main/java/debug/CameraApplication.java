package debug;

import android.util.Log;
import android.util.Size;

import com.cfox.camera.FxCamera;
import com.cfox.camera.IConfig;
import com.cfox.lib_common.base.BaseApplication;

public class CameraApplication extends BaseApplication {
    private static final String TAG = "CameraApplication";

    FxCamera mFxCamera;
    @Override
    public void onCreate() {
        super.onCreate();
        mFxCamera = FxCamera.init(this);
//        mFxCamera.setConfig(new IConfig() {
//            @Override
//            public Size getPreviewSize(Size size, Size[] supportSizes) {
//                Log.d(TAG, "getPreviewSize: ......"+ size);
//                return size;
//            }
//
//            @Override
//            public Size getPictureSize(Size size, Size[] supportSizes) {
//                Log.d(TAG, "getPictureSize: ........." + size);
//                return size;
//            }
//        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mFxCamera.release();
    }
}
