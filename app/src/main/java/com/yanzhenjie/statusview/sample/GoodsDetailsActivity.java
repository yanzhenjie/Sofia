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
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yanzhenjie.statusview.StatusUtils;
import com.yanzhenjie.statusview.StatusView;

/**
 * <p>
 * Content intrusion status bar.
 * </p>
 * Created by YanZhenjie on 2017/8/2.
 */
public class GoodsDetailsActivity extends AppCompatActivity {

    NestedScrollView mNestedScrollView;

    StatusView mStatusView;
    Toolbar mToolbar;

    View mHeaderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        StatusUtils.setLayoutFullScreen(this);  // Layout full screen.

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mStatusView = (StatusView) findViewById(R.id.status_view);

        mNestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        mHeaderView = findViewById(R.id.header);

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int headerHeight = mHeaderView.getHeight();
                int scrollDistance = Math.min(scrollY, headerHeight);
                int alpha = (int) ((float) scrollDistance / (float) headerHeight * 255F);

                setToolbarStatusBarAlpha(alpha);
            }
        });
        setToolbarStatusBarAlpha(0);
    }

    private void setToolbarStatusBarAlpha(int alpha) {
        mToolbar.getBackground().mutate().setAlpha(alpha);
        mStatusView.getBackground().mutate().setAlpha(alpha);
    }

}