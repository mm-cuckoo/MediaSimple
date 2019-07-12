package com.chao.module_main.theme_one;


import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chao.lib_common.arouter.RouterPath;
import com.chao.module_main.PagerInfo;
import com.chao.module_main.R;

import java.util.List;

public class MainOneActivity extends ActivityWrapper {

    @Override
    public void initPagerViews(List<PagerInfo> views) {
        Fragment viewFg = (Fragment) ARouter.getInstance().build(RouterPath.MAIN_PLAYER_FG).navigation();
        if (viewFg != null) {
            views.add(createPagerInfo(R.string.main_tab_player, R.mipmap.main_tab_mine_select, R.mipmap.main_tab_mine_unselect, viewFg));
        }

        Fragment dbFg = (Fragment) ARouter.getInstance().build(RouterPath.MAIN_AVEDITOR_FG).navigation();
        if (dbFg != null) {
            views.add(createPagerInfo( R.string.main_tab_aveditor, R.mipmap.main_tab_mine_select, R.mipmap.main_tab_mine_unselect, dbFg));
        }

        Fragment mineFg = (Fragment) ARouter.getInstance().build(RouterPath.MAIN_MINE_FG).navigation();
        if (mineFg != null) {
            views.add(createPagerInfo(R.string.main_tab_mine, R.mipmap.main_tab_mine_select, R.mipmap.main_tab_mine_unselect, mineFg));
        }
    }
}

