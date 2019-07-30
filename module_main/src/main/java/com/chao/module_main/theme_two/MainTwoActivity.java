package com.chao.module_main.theme_two;



import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chao.lib_common.arouter.RouterPath;
import com.chao.module_main.PagerInfo;
import com.chao.module_main.R;

import java.util.List;

public class MainTwoActivity extends ActivityWrapper{

    private static final String TAB_FLAG_CAMERA     = "camera";
    private static final String TAB_FLAG_PLAYER     = "player";
    private static final String TAB_FLAG_AVEDITOR   = "aveditor";
    private static final String TAB_FLAG_MINE       = "mine";


    @Override
    public void initPagerInfo(List<PagerInfo> views) {
        views.add(createPagerInfo(TAB_FLAG_CAMERA,R.string.main_tab_camera, R.mipmap.main_tab_mine_select, R.mipmap.main_tab_mine_unselect));
        views.add(createPagerInfo(TAB_FLAG_PLAYER,R.string.main_tab_player, R.mipmap.main_tab_mine_select, R.mipmap.main_tab_mine_unselect));
        views.add(createPagerInfo(TAB_FLAG_AVEDITOR,R.string.main_tab_aveditor, R.mipmap.main_tab_mine_select, R.mipmap.main_tab_mine_unselect));
        views.add(createPagerInfo(TAB_FLAG_MINE,R.string.main_tab_mine, R.mipmap.main_tab_mine_select, R.mipmap.main_tab_mine_unselect));
    }

    @Override
    public Fragment createView(String flag) {
        Fragment fragmentView = null;
        switch (flag) {
            case TAB_FLAG_CAMERA:
                fragmentView = (Fragment) ARouter.getInstance().build(RouterPath.MAIN_CAMERA_FG).navigation();
                break;
            case TAB_FLAG_PLAYER:
                fragmentView = (Fragment) ARouter.getInstance().build(RouterPath.MAIN_PLAYER_FG).navigation();
                break;
            case TAB_FLAG_AVEDITOR:
                fragmentView = (Fragment) ARouter.getInstance().build(RouterPath.MAIN_AVEDITOR_FG).navigation();
                break;
            case TAB_FLAG_MINE:
                fragmentView = (Fragment) ARouter.getInstance().build(RouterPath.MAIN_MINE_FG).navigation();
                break;
            default:
        }

        return fragmentView;
    }
}
