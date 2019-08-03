package com.cfox.module_main.theme_one;


import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cfox.lib_common.arouter.RouterPath;
import com.cfox.module_main.PagerInfo;
import com.cfox.module_main.R;

import java.util.List;

public class MainOneActivity extends ActivityWrapper {

    @Override
    public void initPagerViews(List<PagerInfo> views) {
        Fragment cameraFg = (Fragment) ARouter.getInstance().build(RouterPath.MAIN_CAMERA_FG).navigation();
        if (cameraFg != null) {
            views.add(createPagerInfo(R.string.main_tab_camera, R.mipmap.main_tab_mine_select, R.mipmap.main_tab_mine_unselect, cameraFg));
        }

        Fragment playFg = (Fragment) ARouter.getInstance().build(RouterPath.MAIN_PLAYER_FG).navigation();
        if (playFg != null) {
            views.add(createPagerInfo(R.string.main_tab_player, R.mipmap.main_tab_mine_select, R.mipmap.main_tab_mine_unselect, playFg));
        }

        Fragment avEditorFg = (Fragment) ARouter.getInstance().build(RouterPath.MAIN_AVEDITOR_FG).navigation();
        if (avEditorFg != null) {
            views.add(createPagerInfo( R.string.main_tab_aveditor, R.mipmap.main_tab_mine_select, R.mipmap.main_tab_mine_unselect, avEditorFg));
        }

        Fragment mineFg = (Fragment) ARouter.getInstance().build(RouterPath.MAIN_MINE_FG).navigation();
        if (mineFg != null) {
            views.add(createPagerInfo(R.string.main_tab_mine, R.mipmap.main_tab_mine_select, R.mipmap.main_tab_mine_unselect, mineFg));
        }
    }
}

