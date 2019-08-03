package debug;


import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cfox.lib_common.arouter.RouterPath;

public class CameraModuleActivity extends BaseDebugActivity{
    @Override
    public Fragment createFragment() {
        return (Fragment) ARouter.getInstance().build(RouterPath.MAIN_CAMERA_FG).navigation();
    }
}
