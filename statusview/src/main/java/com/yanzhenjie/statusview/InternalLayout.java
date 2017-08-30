package com.yanzhenjie.statusview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by albert on 2017/8/30.
 */

public class InternalLayout extends RelativeLayout {

    private StatusView mStatusView;
    private NavigationView mNavigationView;
    private ViewGroup mContentLayout;

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

    void setNavigationBarColor(Drawable drawable){
        ViewCompat.setBackground(mNavigationView, drawable);
    }
}
