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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yanzhenjie.sofia.Sofia;
import com.yanzhenjie.sofia.sample.R;

/**
 * Content intrusion status bar.
 * Created by YanZhenjie on 2017/8/2.
 */
public class GoodsDetailsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private View mHeaderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        mHeaderView = findViewById(R.id.header);

        boolean flag = getIntent().getBooleanExtra("flag",true);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int headerHeight = mHeaderView.getHeight();
                int scrollDistance = Math.min(scrollY, headerHeight);
                int statusAlpha = (int) ((float) scrollDistance / (float) headerHeight * 255F);
                setAnyBarAlpha(statusAlpha);
            }
        });

        Sofia.with(this)
                .statusBarBackground(ContextCompat.getColor(this, R.color.colorPrimary))
                .navigationBarBackground(ContextCompat.getDrawable(this, R.color.colorPrimary))
                .fitsSystemWindowView(mToolbar);

        setAnyBarAlpha(0);
        //区别如果内容要侵入到状态栏需要设置invasionStatusBar,否则不需要
        if (flag){
            Sofia.with(this).invasionStatusBar();
        }else{
            setTitle("内容不侵入状态栏与toolbar渐变");
        }
    }

    private void setAnyBarAlpha(int alpha) {
        mToolbar.getBackground().mutate().setAlpha(alpha);
        Sofia.with(this)
                .statusBarBackgroundAlpha(alpha);
    }

}