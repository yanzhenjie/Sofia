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
package com.yanzhenjie.sofia.sample.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yanzhenjie.sofia.Sofia;
import com.yanzhenjie.sofia.StatusView;
import com.yanzhenjie.sofia.sample.R;

/**
 * Created by YanZhenjie on 2017/12/13.
 */
public class DrawerLayoutActivity extends AppCompatActivity {

    private StatusView mStatusView;
    private Toolbar mToolbar;
    private View mHeaderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);

        mStatusView = findViewById(R.id.status_view);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        mHeaderView = findViewById(R.id.header);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int headerHeight = mHeaderView.getHeight();
                int scrollDistance = Math.min(scrollY, headerHeight);
                int statusAlpha = (int) ((float) scrollDistance / (float) headerHeight * 255F);
                setAnyBarAlpha(statusAlpha);
            }
        });

        // 这里不使用Sofia设置StatusBar颜色，用自己在布局中的，用Sofia设置会覆盖在DrawerLayout之上。
        Sofia.with(this)
                .navigationBarBackground(ContextCompat.getDrawable(this, R.color.colorNavigation))
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        setAnyBarAlpha(0);
    }

    private void setAnyBarAlpha(int alpha) {
        mStatusView.getBackground().mutate().setAlpha(alpha);
        mToolbar.getBackground().mutate().setAlpha(alpha);
    }
}