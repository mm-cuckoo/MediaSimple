package debug;

import androidx.core.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chao.lib_common.arouter.RouterPath;


public class MineModuleActivity extends BaseDebugActivity {

    @Override
    public Fragment createFragment() {
        return (Fragment) ARouter.getInstance().build(RouterPath.MAIN_MINE_FG).navigation();
    }
}
