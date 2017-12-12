package com.yanzhenjie.statusview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

/**
 * Created by albert on 2017/8/30.
 */

class InternalLayout extends RelativeLayout {

    private StatusView mStatusView;
    private NavigationView mNavigationView;
    private ViewGroup mContentLayout;

    private static final int FLAG_BOTH_NOT_IMMERSED = 0x0;
    private static final int FLAG_IMMERSED_STATUSBAR = 0x1;
    private static final int FLAG_IMMERSED_NAVIGATIONBAR = 0x2;
    private static final int FLAG_IMMERSED_STATUSBAR_AND_NAVIGATIONBAR = FLAG_IMMERSED_STATUSBAR | FLAG_IMMERSED_NAVIGATIONBAR;
    private int mImmersedFlag = FLAG_BOTH_NOT_IMMERSED;

    public InternalLayout(Context context) {
        this(context, null);
    }

    public InternalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewCompat.setFitsSystemWindows(this, true);
        ViewCompat.requestApplyInsets(this);
        ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                return insets.consumeSystemWindowInsets();
            }
        });
        inflate(context, R.layout.layout_status_view, this);
        mStatusView = (StatusView) findViewById(R.id.status_view);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mContentLayout = (ViewGroup) findViewById(R.id.content);
    }

    void setContentView(View content) {
        if(content.getParent() == null){
            mContentLayout.addView(content);
        }
    }

    void setStatusBarColor(@ColorInt int statusBarColor) {
        mStatusView.setBackgroundColor(statusBarColor);
    }

    void setStatusBarDrawable(Drawable drawable){
        ViewCompat.setBackground(mStatusView, drawable);
    }

    void setNavigationBarColor(@ColorInt int navigationBarColor){
        mNavigationView.setBackgroundColor(navigationBarColor);
    }

    void setNavigationDrawable(Drawable drawable){
        ViewCompat.setBackground(mNavigationView, drawable);
    }

    void enableImmersedStatusBar(boolean immersed) {
        if(immersed){
            mImmersedFlag |= FLAG_IMMERSED_STATUSBAR;
        }else {
            mImmersedFlag = (mImmersedFlag & ~FLAG_IMMERSED_STATUSBAR);
        }
        ViewCompat.setAlpha(mStatusView, immersed? 0 : 1);
        configContentLayout();
    }

    void enableImmersedNavigationBar(boolean immersed) {
        if(immersed){
            mImmersedFlag |= FLAG_IMMERSED_NAVIGATIONBAR;
        }else {
            mImmersedFlag &= (mImmersedFlag & ~FLAG_IMMERSED_NAVIGATIONBAR);
        }
        ViewCompat.setAlpha(mNavigationView, immersed? 0 : 1);
        configContentLayout();
    }

    private void configContentLayout() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        switch (mImmersedFlag){
            case FLAG_IMMERSED_STATUSBAR:
                params.addRule(RelativeLayout.ABOVE, R.id.navigation_view);
                bringChildToFront(mStatusView);
                break;
            case FLAG_IMMERSED_NAVIGATIONBAR:
                params.addRule(RelativeLayout.BELOW, R.id.status_view);
                break;
            case FLAG_IMMERSED_STATUSBAR_AND_NAVIGATIONBAR:
                break;
            case FLAG_BOTH_NOT_IMMERSED:
                params.addRule(RelativeLayout.BELOW, R.id.status_view);
                params.addRule(RelativeLayout.ABOVE, R.id.navigation_view);
                break;
        }
        mContentLayout.setLayoutParams(params);
    }

    public void setStatusBarAlpha(int alpha) {
        ViewCompat.setAlpha(mStatusView, 1);
        final Drawable background = mStatusView.getBackground();
        if(background != null){
            mStatusView.getBackground().mutate().setAlpha(alpha);
        }
    }

    public void setNavigationBarAlpha(int alpha) {
        ViewCompat.setAlpha(mNavigationView, 1);
        final Drawable background = mNavigationView.getBackground();
        if(background != null){
            mNavigationView.getBackground().mutate().setAlpha(alpha);
        }
    }

    void fitSystemWindowWithView(View needFitView) {
        ViewGroup.LayoutParams params = needFitView.getLayoutParams();
        if(params instanceof MarginLayoutParams){
            final MarginLayoutParams p = (MarginLayoutParams) params;
            p.setMargins(p.leftMargin, p.topMargin + mStatusView.getStatusBarHeight(), p.rightMargin, p.bottomMargin);
        }
    }
}
