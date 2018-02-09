/*
 * Copyright 2017 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.sofia;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


/**
 * Created by YanZhenjie on 2017/8/30.
 *
 */

//                View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。
//                View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。
//                View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
//                View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。
public class HostLayout extends RelativeLayout implements Bar {
    public static final int FLAG_NOT_INVASION = 0x0;
    public static final int FLAG_INVASION_STATUS = 0x1;
    public static final int FLAG_INVASION_NAVIGATION = 0x2;
    public static final int FLAG_INVASION_STATUS_AND_NAVIGATION = FLAG_INVASION_STATUS | FLAG_INVASION_NAVIGATION;
    private Activity mActivity;
    private int mInvasionFlag = FLAG_NOT_INVASION,mSysInvasionFlag=FLAG_NOT_INVASION;
    private StatusView mStatusView;
    private NavigationView mNavigationView;
    private FrameLayout mContentLayout;
    AndroidUiChangedListener.UiChangedListener uiChangedListener=new AndroidUiChangedListener.UiChangedListener() {
        @Override
        public void onChanged() {
            changeFlag(mActivity.getWindow().getDecorView().getSystemUiVisibility());
            setNavigationViewStatus();
            setStatusViewStatus();
        }
    };
    ComponentCallbacks callbacks=    new ComponentCallbacks() {
        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            mNavigationView.setVisibility(VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if( newConfig.getLayoutDirection()==Configuration.ORIENTATION_LANDSCAPE){
                }else {
                }
            }else {
                if( newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE|| newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
                }else{
                }
            }
        }
        @Override
        public void onLowMemory() {
        }
    };
    Application.ActivityLifecycleCallbacks activityLifecycleCallbacks =new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }
        @Override
        public void onActivityStarted(Activity activity) {
        }
        @Override
        public void onActivityResumed(Activity activity) {
           if (mActivity != null && mActivity == activity) {
               mNavigationView.setVisibility(VISIBLE);
                changeFlag(mActivity.getWindow().getDecorView().getSystemUiVisibility());
                setNavigationViewStatus();
                setStatusViewStatus();
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }
        @Override
        public void onActivityDestroyed(Activity activity) {
            if (mActivity != null && mActivity == activity) {
                mActivity.unregisterComponentCallbacks(callbacks);
                (mActivity.getApplication()).unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
            }
        }

    };
    HostLayout(Activity activity) {
        super(activity);
        this.mActivity = activity;
        loadView();
        replaceContentView();
        AndroidUiChangedListener.assistActivity(mContentLayout);
        AndroidUiChangedListener.setUiChangedListener(uiChangedListener);
         mActivity.registerComponentCallbacks(callbacks);
        (mActivity.getApplication()).registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        Utils.invasionNavigationBar(mActivity);
        Utils.setStatusBarColor(mActivity, Color.TRANSPARENT);
        Utils.setNavigationBarColor(mActivity, Color.TRANSPARENT);
    }
    public int getmInvasionFlag() {
        return mInvasionFlag;
    }
    /**
     *  修改状态,这是我们自己通过系统当前的设置作为参考
     *  Modify state, this is our own through the system current Settings for reference.
     * @param visibility    system decorView Ui visibility
     */
    private  void changeFlag(int visibility){
        mSysInvasionFlag=FLAG_NOT_INVASION;
        if ((visibility&GONE)==GONE||
                (visibility&SYSTEM_UI_FLAG_HIDE_NAVIGATION)==SYSTEM_UI_FLAG_HIDE_NAVIGATION){
            mSysInvasionFlag=mSysInvasionFlag|FLAG_INVASION_NAVIGATION;
        }else {
            Utils.invasionNavigationBar(mActivity);
        }
        if ((visibility&GONE)==GONE||(visibility&INVISIBLE)==INVISIBLE){
            mSysInvasionFlag=mSysInvasionFlag|FLAG_INVASION_STATUS;
        }else{
            Utils.invasionStatusBar(mActivity);
        }
        if (!ScreenUtils.checkDeviceHasNavigationBar(mActivity)){
            mSysInvasionFlag=mSysInvasionFlag|FLAG_INVASION_NAVIGATION;
        }
    }
    /**
     * Vertical screen status (according to the screen width, so it is valid for mobile phone)
     * @return
     */
    private boolean isPortrait(){
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density1 = dm.density;
        int width3 = dm.widthPixels;
        int height3 = dm.heightPixels;



        WindowManager wm = (WindowManager)mActivity.getWindow().getDecorView().getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
            int srceenW = outMetrics.widthPixels;
       int screenH = outMetrics.heightPixels;

        Log.i("sho", (mActivity.getWindowManager().getDefaultDisplay().getRotation())+"fx: ");
        Log.i("sho", width3+"isPortrait: "+srceenW);
        if (screenH>srceenW){
            return true;
        }else {
            return false;
        }
    }
    /**
     * The custom navigation bar is visible.
     */
    private void setNavigationViewStatus(){
        int requestedOrientation = mActivity.getRequestedOrientation();
        //横屏或者反向横屏,以及用户想沉浸,实际系统不显示导航栏都不显示这个view
        if ((mInvasionFlag &FLAG_INVASION_NAVIGATION)==FLAG_INVASION_NAVIGATION||(mSysInvasionFlag &FLAG_INVASION_NAVIGATION)==FLAG_INVASION_NAVIGATION||ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE==requestedOrientation||ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE==requestedOrientation){
            mNavigationView.setVisibility(GONE);
        }else {
            if(isPortrait()&&!((mInvasionFlag &FLAG_INVASION_NAVIGATION)==FLAG_INVASION_NAVIGATION)){
            //这里到requestLayout的时候还是VISIBLE,但是到onMeasure的时候就又称GONE,不知道为啥
            mNavigationView.setVisibility(VISIBLE);
            if (mNavigationView.getHeight()<=0){
                requestLayout();
            }
          }else {
                //横屏,以及用户设置了沉浸
                mNavigationView.setVisibility(GONE);
            }
        }
    }

    /**
     * The status bar of the custom replacement is visible.
     */
    private void setStatusViewStatus(){
                if ((mInvasionFlag &FLAG_INVASION_STATUS)==FLAG_INVASION_STATUS||(mSysInvasionFlag &FLAG_INVASION_STATUS)==FLAG_INVASION_STATUS){
                        mStatusView.setVisibility(GONE);
                }else {
                    Utils.invasionStatusBar(mActivity);
                    mStatusView.setVisibility(VISIBLE);
                }
    }
    private void loadView() {
        inflate(mActivity, R.layout.sofia_host_layout, this);
        mStatusView = findViewById(R.id.status_view);
        mNavigationView = findViewById(R.id.navigation_view);
        mContentLayout = findViewById(R.id.content);
    }
    private void replaceContentView() {
        Window window = mActivity.getWindow();
        ViewGroup contentLayout = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        if (contentLayout.getChildCount() > 0) {
            View contentView = contentLayout.getChildAt(0);
            contentLayout.removeView(contentView);
            mContentLayout.addView(contentView);
        }
        contentLayout.addView(this);
    }
    @Override
    public Bar statusBarDarkFont() {
        Utils.setStatusBarDarkFont(mActivity, true);
        return this;
    }
    @Override
    public Bar statusBarLightFont() {
        Utils.setStatusBarDarkFont(mActivity, false);
        return this;
    }
    @Override
    public Bar statusBarBackground(int statusBarColor) {
        mStatusView.setBackgroundColor(statusBarColor);
        return this;
    }
    @Override
    public Bar statusBarBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mStatusView.setBackground(drawable);
        else mStatusView.setBackgroundDrawable(drawable);
        return this;
    }
    @Override
    public Bar statusBarBackgroundAlpha(int alpha) {
        // mStatusView.setVisibility(VISIBLE);
        // The main thing is to solve the content immersion, fixed toolbar to let toolbar and status bar, gradient, status bar, because it is the cause of immersive cause cannot display, cause the content to be leaking out.
        // 主要是解决内容沉浸后,固定toolbar让toolbar和状态栏,渐变,状态栏,因为是沉浸的原因导致的无法显示,导致内容被漏出来
        mStatusView.setVisibility(VISIBLE);
        final Drawable background = mStatusView.getBackground();
        if (background != null) background.mutate().setAlpha(alpha);
        return this;
    }
    @Override
    public Bar navigationBarBackground(int navigationBarColor) {
        mNavigationView.setBackgroundColor(navigationBarColor);
        return this;
    }
    @Override
    public Bar navigationBarBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mNavigationView.setBackground(drawable);
        else mNavigationView.setBackgroundDrawable(drawable);
        return this;
    }
    @Override
    public Bar navigationBarBackgroundAlpha(int alpha) {
        final Drawable background = mNavigationView.getBackground();
        if (background != null) background.mutate().setAlpha(alpha);
        return this;
    }
    @Override
    public Bar invasionStatusBar() {
        mInvasionFlag |= FLAG_INVASION_STATUS;
        reLayoutInvasion();
        setStatusViewStatus();
        return this;
    }
    @Override
    public Bar invasionNavigationBar() {
        mInvasionFlag |= FLAG_INVASION_NAVIGATION;
        reLayoutInvasion();
        setNavigationViewStatus();
        return this;
    }
    @Override
    public Bar setStatusAndNavigationBar(int status) {
         mInvasionFlag=status;
        reLayoutInvasion();
        setNavigationViewStatus();
        setStatusViewStatus();
        return this;
    }
    @Override
    public Bar fitsSystemWindowView(int viewId) {
        return fitsSystemWindowView(findViewById(viewId));
    }
    @Override
    public Bar fitsSystemWindowView(View fitView) {
        ViewParent fitParent = fitView.getParent();
        if (fitParent != null && !(fitParent instanceof FitWindowLayout)) {
            ViewGroup fitGroup = (ViewGroup) fitParent;
            //移除固定的
            fitGroup.removeView(fitView);
            //设置固定view的高度包裹内容
            ViewGroup.LayoutParams fitLayoutParams = fitView.getLayoutParams();
            fitLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //创建容器用来存储固定view
            FitWindowLayout fitLayout = new FitWindowLayout(mActivity);
            //添加容器到原来的地方
            fitGroup.addView(fitLayout, fitLayoutParams);
            ViewGroup.LayoutParams fitViewParams = new ViewGroup.LayoutParams(fitLayoutParams.width, fitLayoutParams.height);
            //将固定view放到容器中
            fitLayout.addView(fitView, fitViewParams);
        }
        return this;
    }

    private void reLayoutInvasion() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        switch (mInvasionFlag) {
            case FLAG_INVASION_STATUS:
                layoutParams.addRule(RelativeLayout.ABOVE, R.id.navigation_view);
                bringChildToFront(mStatusView);
                break;
            case FLAG_INVASION_NAVIGATION:
                layoutParams.addRule(RelativeLayout.BELOW, R.id.status_view);
                bringChildToFront(mNavigationView);
                break;
            case FLAG_INVASION_STATUS_AND_NAVIGATION:
                bringToFront();
                bringChildToFront(mStatusView);
                bringChildToFront(mNavigationView);
                break;
            case FLAG_NOT_INVASION:
                layoutParams.addRule(RelativeLayout.BELOW, R.id.status_view);
                layoutParams.addRule(RelativeLayout.ABOVE, R.id.navigation_view);
                break;
        }
        mContentLayout.setLayoutParams(layoutParams);

    }
}