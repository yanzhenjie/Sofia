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
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yanzhenjie.statusview.NavigationView;
import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;
import com.yanzhenjie.statusview.SystemBarHelper;

/**
 * <p>
 * Content intrusion status bar.
 * </p>
 * Created by YanZhenjie on 2017/8/2.
 */
public class GoodsDetailsActivity extends AppCompatActivity {

    NestedScrollView mNestedScrollView;
    Toolbar mToolbar;
    View mHeaderView;
    SystemBarHelper mSystemBarHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);

        mSystemBarHelper = new SystemBarHelper.Builder()
                .enableFullToStatusBar()
                .enableFullToNavigationBar()
                .enableImmersedStatusBar(true)
                .fitSystemWindow(R.id.toolbar)
                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .navigationBarDrawable(ContextCompat.getDrawable(this, R.drawable.navigation))
                .into(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mNestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        mHeaderView = findViewById(R.id.header);

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int headerHeight = mHeaderView.getHeight();
                int scrollDistance = Math.min(scrollY, headerHeight);
                int statusAlpha = (int) ((float) scrollDistance / (float) headerHeight * 255F);
                setAnyBarAlpha(statusAlpha);
            }
        });
        setAnyBarAlpha(0);
    }

    private void setAnyBarAlpha(int alpha) {
        mSystemBarHelper.enableImmersedNavigationBar(alpha > 0);
        mToolbar.getBackground().mutate().setAlpha(alpha);
        mSystemBarHelper.setStatusBarAlpha(alpha);
        mSystemBarHelper.setNavigationBarAlpha(255 - alpha);
    }

}