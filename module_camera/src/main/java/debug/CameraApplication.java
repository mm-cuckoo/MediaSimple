package debug;

import com.cfox.camera.FxCamera;
import com.cfox.camera.FxCameraImpl;
import com.cfox.lib_common.base.BaseApplication;

public class CameraApplication extends BaseApplication {

    FxCamera mFxCamera;
    @Override
    public void onCreate() {
        super.onCreate();
        mFxCamera = FxCameraImpl.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mFxCamera.release();
    }
}
