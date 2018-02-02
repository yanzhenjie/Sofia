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
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
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
    /**
     *  修改状态,这是我们自己通过系统当前的设置作为参考
     */
    private  void changeFlag(int visibility){
        //清空
        mSysInvasionFlag=FLAG_NOT_INVASION;
        //导航栏隐藏了
        if ((visibility&GONE)==GONE||
                (visibility&SYSTEM_UI_FLAG_HIDE_NAVIGATION)==SYSTEM_UI_FLAG_HIDE_NAVIGATION){
            mSysInvasionFlag=mSysInvasionFlag|FLAG_INVASION_NAVIGATION;
        }else {
            Utils.invasionNavigationBar(mActivity);
        }
        //状态栏隐藏了
        if ((visibility&GONE)==GONE||(visibility&INVISIBLE)==INVISIBLE){
            mSysInvasionFlag=mSysInvasionFlag|FLAG_INVASION_STATUS;
        }else{

            Utils.invasionStatusBar(mActivity);
        }
        //如果导航栏是可隐藏,并且已经隐藏了
        if (!ScreenUtils.checkDeviceHasNavigationBar(mActivity)){
            mSysInvasionFlag=mSysInvasionFlag|FLAG_INVASION_NAVIGATION;
        }
    }
    AndroidUiChangedListener.UiChangedListener uiChangedListener=new AndroidUiChangedListener.UiChangedListener() {
        @Override
        public void onChanged() {
            changeFlag(mActivity.getWindow().getDecorView().getSystemUiVisibility());
            setNavigationViewStatus();
            setStatusViewStatus();
        }
    };

    public int getmInvasionFlag() {
        return mInvasionFlag;
    }

    HostLayout(Activity activity) {
        super(activity);
        this.mActivity = activity;
        loadView();
        replaceContentView();
        AndroidUiChangedListener.assistActivity(mContentLayout);
        //监听页面改变,如果一旦发生改变就会重新更新下通知栏和导航栏的状态
        // (这里主要是因为有些系统的导航栏可以隐藏,但是不通过SystemUiVisibility的问题)
        AndroidUiChangedListener.setUiChangedListener(uiChangedListener);
       //设置系统 的Ui可见度的时候监听
     //顶替系统的导航栏
      Utils.invasionNavigationBar(mActivity);
      Utils.setStatusBarColor(mActivity, Color.TRANSPARENT);
      Utils.setNavigationBarColor(mActivity, Color.TRANSPARENT);
    }
    /**
     * 是否竖屏状态(根据屏幕宽高,因此针对手机有效)
     * @return
     */
    private boolean isPortrait(){
        WindowManager wm = (WindowManager)mActivity .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int srceenW = outMetrics.widthPixels;
      int screenH = outMetrics.heightPixels;
        if (screenH>srceenW){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 自定义的状态栏是否可见
     */
    private void setNavigationViewStatus(){
        int requestedOrientation = mActivity.getRequestedOrientation();
        //横屏或者反向横屏,以及用户想沉浸,实际系统不显示导航栏都不显示这个view
        if ((mInvasionFlag &FLAG_INVASION_NAVIGATION)==FLAG_INVASION_NAVIGATION||(mSysInvasionFlag &FLAG_INVASION_NAVIGATION)==FLAG_INVASION_NAVIGATION||ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE==requestedOrientation||ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE==requestedOrientation){
            mNavigationView.setVisibility(GONE);
        }else {
            //不清楚屏幕反向有系统决定的时候,以及其他情况
            //是竖屏并且使用者未设置沉浸
            if(isPortrait()&&!((mInvasionFlag &FLAG_INVASION_NAVIGATION)==FLAG_INVASION_NAVIGATION)){
                //这里判断下导航栏高度的情况,如果横屏进入Activity后,在旋转为竖屏的时候,因为横屏的时候mNavigationViewgone了,因此高度测量的时候就没有高度,高度是0,
                //如果高度不是0,直接显示出就行,否则需要设置高度,让view重新测量的方法没找到,因此使用了直接设置,不过设置的高度,也是系统的导航栏高度
                //请求重测量
                if (mNavigationView.getHeight()<=0){
                    requestLayout();
                }
                Utils.invasionNavigationBar(mActivity);
                mNavigationView.setVisibility(VISIBLE);
            }else {
                //横屏,以及用户设置了沉浸
                mNavigationView.setVisibility(GONE);
            }
        }
    }
    /**
     * 自定义替换的状态栏是否可见
     *
     */
    private void setStatusViewStatus(){
                  //使用者想进入沉浸
                if ((mInvasionFlag &FLAG_INVASION_STATUS)==FLAG_INVASION_STATUS||(mSysInvasionFlag &FLAG_INVASION_STATUS)==FLAG_INVASION_STATUS){
                        mStatusView.setVisibility(GONE);
                }else {
                    Utils.invasionStatusBar(mActivity);
                    mStatusView.setVisibility(VISIBLE);
                }
    }
    /**
     * 加载自定义的容器
     */
    private void loadView() {
        inflate(mActivity, R.layout.sofia_host_layout, this);
        mStatusView = findViewById(R.id.status_view);
        mNavigationView = findViewById(R.id.navigation_view);
        mContentLayout = findViewById(R.id.content);
    }
    /**
     * 将本来应该设置给ContentView的,给自己的contentView,然后将自己的content添加到系统的content
     * (其实就是嵌套一层)
     */
    private void replaceContentView() {
        Window window = mActivity.getWindow();
        //获取系统的的父容器
        ViewGroup contentLayout = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        //容器内有view
        if (contentLayout.getChildCount() > 0) {
            //容器内第一个view(可能是状态栏)
            View contentView = contentLayout.getChildAt(0);
            //移除状态栏
            contentLayout.removeView(contentView);
            //添加到自定义的容器
            mContentLayout.addView(contentView);
        }
        //添加本view到系统的view中
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
        // mStatusView.setVisibility(VISIBLE);主要是解决内容沉浸后,固定toolbar让toolbar和状态栏,渐变,状态栏,因为是沉浸的原因导致的无法显示,导致内容被漏出来
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
            case FLAG_INVASION_STATUS://在状态栏上边
                layoutParams.addRule(RelativeLayout.ABOVE, R.id.navigation_view);
                bringChildToFront(mStatusView);
                break;
            case FLAG_INVASION_NAVIGATION://在状态栏下边
                layoutParams.addRule(RelativeLayout.BELOW, R.id.status_view);
                bringChildToFront(mNavigationView);
                break;
            case FLAG_INVASION_STATUS_AND_NAVIGATION://
                bringToFront();
                bringChildToFront(mStatusView);
                bringChildToFront(mNavigationView);
                break;
            case FLAG_NOT_INVASION://在状态栏下,在导航栏之上
                layoutParams.addRule(RelativeLayout.BELOW, R.id.status_view);
                layoutParams.addRule(RelativeLayout.ABOVE, R.id.navigation_view);
                break;
        }
        mContentLayout.setLayoutParams(layoutParams);

    }
}