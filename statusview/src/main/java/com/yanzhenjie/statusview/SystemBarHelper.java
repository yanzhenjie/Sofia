package com.yanzhenjie.statusview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


/**
 * Created by albert on 2017/8/30.
 */

public class SystemBarHelper {

    private InternalLayout mInternalLayout;

    /**
     * 必须在Activity的setContentView之后调用
     */
    public void setFullToStatusBar(Activity activity, @ColorInt int statusBarColor){
        StatusUtils.setFullToStatusBar(activity);
        if (mInternalLayout == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initialize(activity);
        }
        if(mInternalLayout != null){
            mInternalLayout.setStatusBarColor(statusBarColor);
        }
    }

    /**
     *
     * 必须在Activity的setContentView之后调用
     */
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

    public void setFullToNavigationBar(Activity activity, @ColorInt int navigationBarColor) {
        StatusUtils.setFullToNavigationBar(activity);
        if (mInternalLayout == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initialize(activity);
        }
        if(mInternalLayout != null){
            mInternalLayout.setNavigationBarColor(navigationBarColor);
        }
    }
}
