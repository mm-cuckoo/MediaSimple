package debug;



import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chao.lib_common.arouter.RouterPath;

public class AVEditorModuleActivity extends BaseDebugActivity {

    @Override
    public Fragment createFragment() {
        return (Fragment) ARouter.getInstance().build(RouterPath.MAIN_AVEDITOR_FG).navigation();
    }
}
