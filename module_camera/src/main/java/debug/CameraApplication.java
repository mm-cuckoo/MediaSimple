package debug;

import com.cfox.camera.EsCamera;
import com.cfox.camera.log.EsLog;
import com.cfox.lib_common.base.BaseApplication;

public class CameraApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        EsLog.setPrintTag("Es:");
        EsCamera.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EsCamera.release();
    }
}
