package com.chao.lib_common.base;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.chao.lib_common.R;
import com.chao.lib_common.log.L;
import com.chao.lib_common.mvp.iview.IBaseView;
import com.chao.lib_common.mvp.presenter.BasePresenter;
import com.chao.lib_common.widegts.TextFilter;
import com.chao.lib_common.widegts.ToastUtils;

public abstract class AbsActivity<V extends IBaseView, T extends BasePresenter<V>> extends FragmentActivity {

    private static final String TAG = "AbsActivity";

    private InputMethodManager mMethodManager;

    /**
     * 点击无网络页回调方法
     */
    private ClickCallback mClickCallback;
    /**
     * 无网络显示的View
     */
    private RelativeLayout mNoNetlayout;

    private T mPresenter;


    public interface ClickCallback {
        void onClick();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityConfig();

    }

    private void initActivityConfig() {
        ActivityServer.init(this).addActivity();
        mMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mPresenter = getPresenter();
    }


    @Override
    protected void onResume() {
        //设置屏幕不旋转
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onResume();
        //设置判断网络连接，如果无网络，显示无网络View
//        if (!NetTools.isConnected()){
//            showNoNetView();
//        }
    }

    /**
     * 隐藏键盘
     */
    protected void hideInputMethod(View view){
        mMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 隐藏键盘
     */
    protected void showInputMethod(View view){
        mMethodManager.showSoftInput(view, 0);
    }

    /**
     * EditView 输入过滤，限制输入
     * @param textView
     * @param textLength 限制字节数，按 一个汉字 = 2 byte ，一个英文 = 1byte
     * @param showMsg 到达限制时，提示信息
     */
    public void textChangeFilter(TextView textView, int textLength, String showMsg){

        textView.setFilters(new InputFilter[]{new TextFilter(textLength, showMsg, new TextFilter.ITextChangeCall() {
            @Override
            public void textResult(String text) {
                ToastUtils.toastShow(text);
            }
        })});
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.cancleRequest();
        }
        //在这里暂停网络请求
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityServer.init(this).removeActivity();
        //注销网络监听
//        NetObserver.unregister();
        //在这里统一完成取消该activity 中的所有网络请求
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    public T getPresenter() {
        return null;
    }

    /**
     * 调起没有网络连接视图
     *
     * @param clickCallback ： 点击页面时回调方法
     */
    public void showNoNetView(final ClickCallback clickCallback) {
        mClickCallback = clickCallback;
        initNoNetVeiw();
        if (mNoNetlayout != null && mNoNetlayout.getVisibility() == View.GONE) {
            mNoNetlayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 调起没有网络连接视图
     */
    public void showNoNetView() {
        this.showNoNetView(mClickCallback);
    }

    /**
     * 添加没有网络连接视图中点击回调事件
     *
     * @param clickCallback 点击页面时回调方法
     */
    public void setNoNetCallbackListener(ClickCallback clickCallback) {
        this.mClickCallback = clickCallback;
    }

    public void hidenNoNetView() {
        if (mNoNetlayout != null && mNoNetlayout.getVisibility() == View.VISIBLE) {
            mNoNetlayout.setVisibility(View.GONE);
        }
    }


    /**
     * 初始化没有网络填充视图
     */
    private void initNoNetVeiw() {
        mNoNetlayout = (RelativeLayout) findViewById(R.id.no_net);
        if (mNoNetlayout != null) {
            View btnReload = mNoNetlayout.findViewById(R.id.btn_reload_data);
            btnReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickCallback != null){
                        mClickCallback.onClick();
                    }
                }
            });
        } else {
            L.e(TAG, "########没有添加无网络占位布局");
        }
    }
}