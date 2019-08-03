package com.cfox.module_main;


import androidx.fragment.app.Fragment;

public class PagerInfo {
    private String mFlag;
    private int mTitleRes;
    private int mSelectImgRes;
    private int mUnSelectImgRes;
    private Fragment mPager;

    public String getFlag() {
        return mFlag;
    }

    public void setFlag(String mFlag) {
        this.mFlag = mFlag;
    }

    public int getTitleRes() {
        return mTitleRes;
    }

    public void setTitleRes(int titleRes) {
        this.mTitleRes = titleRes;
    }

    public int getSelectImgRes() {
        return mSelectImgRes;
    }

    public void setSelectImgRes(int selectImgRes) {
        this.mSelectImgRes = selectImgRes;
    }

    public int getUnSelectImgRes() {
        return mUnSelectImgRes;
    }

    public void setUnSelectImgRes(int mUnSelectImgRes) {
        this.mUnSelectImgRes = mUnSelectImgRes;
    }

    public Fragment getPager() {
        return mPager;
    }

    public void setPager(Fragment mPager) {
        this.mPager = mPager;
    }
}