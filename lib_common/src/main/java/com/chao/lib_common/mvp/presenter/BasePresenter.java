package com.chao.lib_common.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;

import com.chao.lib_common.mvp.iview.IBaseView;

import java.lang.ref.WeakReference;

public class BasePresenter<V extends IBaseView> {
    protected String mTag;
    private WeakReference<V> mViewRef;

    public void cancleRequest(){
        cancleRequest(mTag);
    }

    public void cancleRequest(String tag){

    }

    public void setCancleRequestTag(String tag) {
        this.mTag = tag;
    }

    @UiThread
    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    /**
     * Get the attached view. You should always call {@link #isViewAttached()} to check if the view
     * is
     * attached to avoid NullPointerExceptions.
     *
     * @return <code>null</code>, if view is not attached, otherwise the concrete view instance
     */
    @UiThread
    @Nullable
    public V getView() {
        return mViewRef == null ? null : mViewRef.get();
    }

    /**
     * Checks if a view is attached to this presenter. You should always call this method before
     * calling {@link #getView()} to get the view instance.
     */
    @UiThread
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @UiThread
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    protected Context getContext(){
        if (getView() == null) {
            throw new RuntimeException("View is null ,place use attachView(V view) method set");
        }

        if(getView() instanceof Fragment){
            return ((Fragment)getView()).getActivity();
        }else if(getView() instanceof Activity){
            return (Context) getView();
        }else if(getView() instanceof View){
            return ((View)getView()).getContext();
        }
        return null;
    }
}