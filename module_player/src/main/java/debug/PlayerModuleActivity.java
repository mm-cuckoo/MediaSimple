package debug;


import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chao.lib_common.arouter.RouterPath;

public class PlayerModuleActivity extends BaseDebugActivity {

    @Override
    public Fragment createFragment() {
        return (Fragment) ARouter.getInstance().build(RouterPath.MAIN_PLAYER_FG).navigation();
    }
}
