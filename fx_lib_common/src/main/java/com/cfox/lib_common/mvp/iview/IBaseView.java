package com.cfox.lib_common.mvp.iview;

public interface IBaseView{
    /**
     * 网络错误
     * @param state
     */
    void netError(int state);

    /**
     * 显示正在加载的对话框
     */
    void showLoadingDialog();

    /**
     * 消失加载对话框
     */
    void dismissLoadingDialog();

    /**
     * 显示提示信息
     * @param msg
     */
    void showMsg(String msg);

}