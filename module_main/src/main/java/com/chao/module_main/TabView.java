package com.chao.module_main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class TabView {

    private View mTabView;
    private Context mContext;

    public TabView(Context context, int viewRes) {
        mContext = context;
        mTabView = LayoutInflater.from(context).inflate(viewRes, null);
    }

    public static TabView createTabView(Context context, int viewRes) {
        return new TabView(context,viewRes);
    }

    public void setTabTile(int tabTileRes) {
        setTabTile(mContext.getResources().getString(tabTileRes));
    }

    public void setTabTile(String tabTile) {
        TextView tabTitle = mTabView.findViewById(R.id.main_tab_title_tv);
        tabTitle.setText(tabTile);
    }


    public void setIconStatus(int selectRes, int unSelectRes) {
        setIconStatus(selectRes,unSelectRes, 0);
    }

    public void setIconStatus(int selectRes, int unSelectRes, int pressedRes) {
        Drawable selectDraw = null;
        Drawable pressedDraw = null;
        Drawable unSelectDraw = null;
        if (selectRes > 0) {
            selectDraw = ContextCompat.getDrawable(mContext,selectRes);
        }
        if (unSelectRes > 0) {
            unSelectDraw = ContextCompat.getDrawable(mContext,unSelectRes);
        }
        if (pressedRes > 0) {
            pressedDraw = ContextCompat.getDrawable(mContext, pressedRes);
        }
        setIconStatus(selectDraw, unSelectDraw, pressedDraw);
    }

    public void setIconStatus(Drawable selectDraw, Drawable unSelectDraw) {
        setIconStatus(selectDraw,unSelectDraw, null);
    }

    public void setIconStatus(Drawable selectDraw, Drawable unSelectDraw, Drawable pressedDraw) {
        ImageView icon = mTabView.findViewById(R.id.main_tab_icon_iv);
        StateListDrawable drawable = new StateListDrawable();
        if (pressedDraw != null) {
            drawable.addState(new int[]{android.R.attr.state_pressed}, pressedDraw);
        }
        if (selectDraw != null) {
            drawable.addState(new int[]{android.R.attr.state_selected}, selectDraw);
        }
        if (unSelectDraw != null) {
            drawable.addState(new int[]{-android.R.attr.state_selected}, unSelectDraw);
        }
        icon.setImageDrawable(drawable);
    }

    public View build() {
        return mTabView;
    }

    public View build(String viewTag) {
        mTabView.setTag(viewTag);
        return mTabView;
    }
}
