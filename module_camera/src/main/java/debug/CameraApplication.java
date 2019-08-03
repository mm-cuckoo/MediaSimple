package debug;

import com.cfox.camera.FxCamera;
import com.cfox.lib_common.base.BaseApplication;

public class CameraApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FxCamera.init(this);
    }
}
