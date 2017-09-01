package com.yanzhenjie.statusview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


/**
 * Created by albert on 2017/8/30.
 */

public class SystemBarHelper {

    private static final boolean UP_LOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    private Builder mBuilder;

    private SystemBarHelper(Builder builder){
        mBuilder = builder;
    }

    public void setStatusBarColor(@ColorInt int statusBarColor) {
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.setStatusBarColor(statusBarColor);
        }else {
            StatusUtils.setStatusBarColor(mBuilder.mActivity, statusBarColor);
        }
    }

    public void setStatusBarDrawable(Drawable drawable){
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.setStatusBarDrawable(drawable);
        }
    }

    public void setNavigationBarDrawable(Drawable drawable){
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.setNavigationDrawable(drawable);
        }
    }

    public void setNavigationBarColor(@ColorInt int navigationBarColor) {
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.setNavigationBarColor(navigationBarColor);
        }else {
            StatusUtils.setNavigationBarColor(mBuilder.mActivity, navigationBarColor);
        }
    }

    public void enableImmersedStatusBar(boolean immersed){
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.enableImmersedStatusBar(immersed);
        }
    }

    public void enableImmersedNavigationBar(boolean immersed){
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.enableImmersedNavigationBar(immersed);
        }
    }

    public void setStatusBarAlpha(int alpha) {
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.setStatusBarAlpha(alpha);
        }
    }

    public void setNavigationBarAlpha(int alpha) {
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.setNavigationBarAlpha(alpha);
        }
    }

    public void setStatusBarDarkFont(boolean darkFont){
        StatusUtils.setStatusBarDarkFont(mBuilder.mActivity, darkFont);
    }

    public static class Builder{

        private boolean mIsImmersedStatusBar;
        private boolean mIsImmersedNavigationBar;
        private boolean mIsFullToStatusBar;
        private boolean mIsFullToNavigationBar;
        @ColorInt private int mStatusBarColor;
        @ColorInt private int mNavigationBarColor;
        private Drawable mStatusBarDrawable;
        private Drawable mNavigationBarDrawable;
        private Activity mActivity;
        private boolean mIsEnableStatusBarDarkFont;
        private InternalLayout mInternalLayout;

        private boolean mIsSetStatusBarColor;
        private boolean mIsSetNavigationBarColor;
        private int mFixSystemWindowViewId;

        public Builder(){}

        public Builder enableStatusBarDarkFont(){
            mIsEnableStatusBarDarkFont = true;
            return this;
        }

        public Builder fitSystemWindow(@IdRes int viewId) {
            mFixSystemWindowViewId = viewId;
            return this;
        }

        public Builder navigationBarColor(@ColorInt int navigationBarColor){
            mIsSetNavigationBarColor = true;
            mNavigationBarColor = navigationBarColor;
            return this;
        }

        public Builder navigationBarDrawable(Drawable drawable){
            mNavigationBarDrawable = drawable;
            return this;
        }

        public Builder statusBarDrawable(Drawable drawable){
            mStatusBarDrawable = drawable;
            return this;
        }

        public Builder statusBarColor(@ColorInt int statusBarColor){
            mIsSetStatusBarColor = true;
            mStatusBarColor = statusBarColor;
            return this;
        }

        public Builder enableFullToStatusBar(){
            mIsFullToStatusBar = true;
            return this;
        }

        public Builder enableFullToNavigationBar(){
            mIsFullToNavigationBar = true;
            return this;
        }

        public Builder enableImmersedStatusBar(boolean enable){
            mIsImmersedStatusBar = enable;
            return this;
        }

        public Builder enableImmersedNavigationBar(boolean enable){
            mIsImmersedNavigationBar = enable;
            return this;
        }

        /**
         * 必须在Activity的setContentView之后调用
         */
        public SystemBarHelper into(Activity activity){
            mActivity = activity;
            if (mIsEnableStatusBarDarkFont) {
                StatusUtils.setStatusBarDarkFont(activity, true);
            }
            if(mIsFullToStatusBar){
                StatusUtils.setFullToStatusBar(activity);
            }
            if(mIsFullToNavigationBar){
                StatusUtils.setFullToNavigationBar(activity);
            }
            if((mIsFullToStatusBar || mIsFullToNavigationBar) && UP_LOLLIPOP && mInternalLayout == null){
                initialize(activity);
            }
            SystemBarHelper helper = new SystemBarHelper(this);
            helper.enableImmersedStatusBar(mIsImmersedStatusBar);
            helper.enableImmersedNavigationBar(mIsImmersedNavigationBar);
            helper.setStatusBarColor(mIsSetStatusBarColor?
                        mStatusBarColor : getDefaultStatusBarColor(activity));
            helper.setNavigationBarColor(mIsSetNavigationBarColor? mNavigationBarColor : Color.BLACK);
            if(mNavigationBarDrawable != null){
                helper.setNavigationBarDrawable(mNavigationBarDrawable);
            }
            if(mStatusBarDrawable != null){
                helper.setStatusBarDrawable(mStatusBarDrawable);
            }

            if(mIsImmersedStatusBar && mIsFullToStatusBar && mInternalLayout != null){
                View needFitView= mInternalLayout.findViewById(mFixSystemWindowViewId);
                if(needFitView != null){
                    mInternalLayout.fitSystemWindowWithView(needFitView);
                }
            }
            return helper;
        }

        private void initialize(Activity activity) {
            Window window = activity.getWindow();
            View androidContent = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            if(androidContent instanceof ViewGroup){
                final ViewGroup realContent = ((ViewGroup) androidContent);
                View content = realContent.getChildAt(0);
                if(content != null){
                    realContent.removeView(content);
                    InternalLayout layout = new InternalLayout(activity);
                    layout.setContentView(content);
                    realContent.addView(layout);
                    mInternalLayout = layout;
                }
            }
        }

        private static final int[] COLOR_PRIMARY_DARK_ATTRS = {
                android.support.v7.appcompat.R.attr.colorPrimaryDark
        };

        private int getDefaultStatusBarColor(Context context){
            TypedArray a = context.obtainStyledAttributes(COLOR_PRIMARY_DARK_ATTRS);
            int color = a.getColor(0, Color.BLACK);
            a.recycle();
            return color;
        }
    }
}
