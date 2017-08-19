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
package com.yanzhenjie.statusview.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

/**
 * <p>
 * Gradient status bar.
 * </p>
 * Created by YanZhenjie on 2017/8/2.
 */
public class CommonActivity extends AppCompatActivity {

    NestedScrollView mNestedScrollView;

    StatusView mStatusView;
    Toolbar mToolbar;

    View mHeaderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        StatusUtils.setFullToStatusBar(this);  // StatusBar.
        StatusUtils.setFullToNavigationBar(this); // NavigationBar.

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mStatusView = (StatusView) findViewById(R.id.status_view);

        mNestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        mHeaderView = findViewById(R.id.header);

        final int startColor = ContextCompat.getColor(this, R.color.colorPrimary);
        final int endColor = ContextCompat.getColor(this, R.color.colorAccent);

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int headerHeight = mHeaderView.getHeight();
                int scrollDistance = Math.min(scrollY, headerHeight);
                float fraction = (float) scrollDistance / (float) headerHeight;

                setToolbarStatusBarAlpha(evaluate(fraction, startColor, endColor));
            }
        });
    }

    private void setToolbarStatusBarAlpha(int color) {
        DrawableCompat.setTint(mToolbar.getBackground().mutate(), color);
        DrawableCompat.setTint(mStatusView.getBackground().mutate(), color);
    }

    public int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }
}
