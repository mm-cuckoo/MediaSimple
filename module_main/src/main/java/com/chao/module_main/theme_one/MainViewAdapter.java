package com.chao.module_main.theme_one;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chao.module_main.PagerInfo;

import java.util.List;


public class MainViewAdapter extends FragmentPagerAdapter {

    private List<PagerInfo> mPages;
    private Context mContext;

    public MainViewAdapter(Context context, FragmentManager fm, List<PagerInfo> pages) {
        super(fm);
        mContext = context;
        mPages = pages;
    }

    @Override
    public Fragment getItem(int position) {
        return mPages.get(position).getPager();
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getText(mPages.get(position).getTitleRes());
    }
}
