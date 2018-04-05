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
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by YanZhenjie on 2017/8/30.
 */
class HostLayout extends RelativeLayout implements Bar {

    private static final int FLAG_NOT_INVASION = 0x0;
    private static final int FLAG_INVASION_STATUS = 0x1;
    private static final int FLAG_INVASION_NAVIGATION = 0x2;
    private static final int FLAG_INVASION_STATUS_AND_NAVIGATION = FLAG_INVASION_STATUS | FLAG_INVASION_NAVIGATION;

    private Activity mActivity;
    private int mInvasionFlag = FLAG_NOT_INVASION;

    private StatusView mStatusView;
    private NavigationView mNavigationView;
    private FrameLayout mContentLayout;

    HostLayout(Activity activity) {
        super(activity);
        this.mActivity = activity;

        loadView();
        replaceContentView();

        Utils.invasionStatusBar(mActivity);
        Utils.invasionNavigationBar(mActivity);
        Utils.setStatusBarColor(mActivity, Color.TRANSPARENT);
        Utils.setNavigationBarColor(mActivity, Color.TRANSPARENT);
    }

    @Override
    public final WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int paddingSize = insets.getSystemWindowInsetBottom();
            int barSize = mNavigationView.getDefaultBarSize();
            paddingSize = paddingSize == barSize ? 0 : paddingSize;
            mContentLayout.setPaddingRelative(0, 0, 0, paddingSize);
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) mContentLayout.getLayoutParams();
            if (paddingSize > 0 && !mNavigationView.isLandscape()) {
                layoutParams.bottomMargin = -barSize;
            } else {
                layoutParams.bottomMargin = 0;
            }
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, 0));
        } else {
            return insets;
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
            ViewGroup.LayoutParams contentParams = contentView.getLayoutParams();
            mContentLayout.addView(contentView, contentParams.width, contentParams.height);
        }
        contentLayout.addView(this, -1, -1);
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
    public Bar statusBarBackground(int color) {
        mStatusView.setBackgroundColor(color);
        return this;
    }

    @Override
    public Bar statusBarBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mStatusView.setBackground(drawable);
        } else {
            mStatusView.setBackgroundDrawable(drawable);
        }
        return this;
    }

    @Override
    public Bar statusBarBackgroundAlpha(int alpha) {
        final Drawable background = mStatusView.getBackground();
        if (background != null) background.mutate().setAlpha(alpha);
        return this;
    }

    @Override
    public Bar navigationBarBackground(int color) {
        mNavigationView.setBackgroundColor(color);
        return this;
    }

    @Override
    public Bar navigationBarBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNavigationView.setBackground(drawable);
        } else {
            mNavigationView.setBackgroundDrawable(drawable);
        }
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
        layoutInvasion();
        return this;
    }

    @Override
    public Bar invasionNavigationBar() {
        mInvasionFlag |= FLAG_INVASION_NAVIGATION;
        layoutInvasion();
        return this;
    }

    /**
     * @deprecated use {@link #fitsStatusBarView(int)} instead.
     */
    @Deprecated
    @Override
    public Bar fitsSystemWindowView(int viewId) {
        return fitsStatusBarView(findViewById(viewId));
    }

    /**
     * @deprecated use {@link #fitsStatusBarView(View)} instead.
     */
    @Deprecated
    @Override
    public Bar fitsSystemWindowView(View view) {
        return fitsStatusBarView(view);
    }

    @Override
    public Bar fitsStatusBarView(int viewId) {
        return fitsStatusBarView(findViewById(viewId));
    }

    @Override
    public Bar fitsStatusBarView(View view) {
        ViewParent fitParent = view.getParent();
        if (fitParent != null && !(fitParent instanceof FitWindowLayout)) {
            FitWindowLayout fitLayout = new FitWindowLayout(mActivity);
            ViewGroup fitGroup = (ViewGroup) fitParent;
            fitGroup.removeView(view);
            fitGroup.addView(fitLayout);

            StatusView statusView = new StatusView(mActivity);
            fitLayout.addView(statusView);

            ViewGroup.LayoutParams fitViewParams = view.getLayoutParams();
            fitLayout.addView(view, fitViewParams.width, fitViewParams.height);
        }
        return this;
    }

    @Override
    public Bar fitsNavigationBarView(int viewId) {
        return fitsNavigationBarView(findViewById(viewId));
    }

    @Override
    public Bar fitsNavigationBarView(View view) {
        ViewParent fitParent = view.getParent();
        if (fitParent != null && !(fitParent instanceof FitWindowLayout)) {
            FitWindowLayout fitLayout = new FitWindowLayout(mActivity);
            ViewGroup fitGroup = (ViewGroup) fitParent;
            fitGroup.removeView(view);
            fitGroup.addView(fitLayout);

            ViewGroup.LayoutParams fitViewParams = view.getLayoutParams();
            fitLayout.addView(view, fitViewParams.width, fitViewParams.height);

            NavigationView navigationView = new NavigationView(mActivity) {
                @Override
                protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                    if (isLandscape()) {
                        this.setMeasuredDimension(0, 0);
                    } else {
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    }
                }
            };
            fitLayout.addView(navigationView);
        }
        return this;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (mNavigationView.isLandscape()) {
            LayoutParams navigationParams = (LayoutParams) mNavigationView.getLayoutParams();
            navigationParams.addRule(ALIGN_PARENT_RIGHT);

            LayoutParams statusParams = (LayoutParams) mStatusView.getLayoutParams();
            statusParams.addRule(ALIGN_PARENT_TOP);
            statusParams.addRule(LEFT_OF, R.id.navigation_view);
        } else {
            LayoutParams statusParams = (LayoutParams) mStatusView.getLayoutParams();
            statusParams.addRule(ALIGN_PARENT_TOP);

            LayoutParams navigationParams = (LayoutParams) mNavigationView.getLayoutParams();
            navigationParams.addRule(ALIGN_PARENT_BOTTOM);
        }
        layoutInvasion();
    }

    private void layoutInvasion() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (mNavigationView.isLandscape()) {
            switch (mInvasionFlag) {
                case FLAG_INVASION_STATUS:
                    layoutParams.addRule(LEFT_OF, R.id.navigation_view);
                    bringChildToFront(mStatusView);
                    break;
                case FLAG_INVASION_NAVIGATION:
                    layoutParams.addRule(BELOW, R.id.status_view);
                    layoutParams.addRule(LEFT_OF, R.id.navigation_view);
                    bringChildToFront(mNavigationView);
                    break;
                case FLAG_INVASION_STATUS_AND_NAVIGATION:
                    layoutParams.addRule(LEFT_OF, R.id.navigation_view);
                    bringChildToFront(mStatusView);
                    bringChildToFront(mNavigationView);
                    break;
                case FLAG_NOT_INVASION:
                    layoutParams.addRule(BELOW, R.id.status_view);
                    layoutParams.addRule(LEFT_OF, R.id.navigation_view);
                    break;
            }
        } else {
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
                    bringChildToFront(mStatusView);
                    bringChildToFront(mNavigationView);
                    break;
                case FLAG_NOT_INVASION:
                    layoutParams.addRule(RelativeLayout.BELOW, R.id.status_view);
                    layoutParams.addRule(RelativeLayout.ABOVE, R.id.navigation_view);
                    break;
            }
        }
        mContentLayout.setLayoutParams(layoutParams);
    }
}