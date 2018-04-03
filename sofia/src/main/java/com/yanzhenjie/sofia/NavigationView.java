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

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by YanZhenjie on 2017/8/1.
 */
public class NavigationView extends View {

    private static final String MINI_CONSTANT = "navigationbar_is_min";

    private ContentResolver mResolver;
    private WindowManager mWindowManager;
    private int mNavigationBarSize;

    public NavigationView(Context context) {
        this(context, null, 0);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mResolver = getContext().getContentResolver();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        registerListener();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            measureNavigation();
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mNavigationBarSize);
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    /**
     * Get navigation bar height.
     */
    public int getNavigationBarSize() {
        return mNavigationBarSize;
    }

    /**
     * Listen to the navigation bar.
     */
    private void registerListener() {
        Uri uri = Settings.System.getUriFor(MINI_CONSTANT);
        mResolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                requestLayout();
            }
        });
    }

    /**
     * Measure the size of the navigation bar.
     */
    private void measureNavigation() {
        Display display = mWindowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(displayMetrics);
        } else {
            display.getMetrics(displayMetrics);
        }
        mNavigationBarSize = displayMetrics.heightPixels - getDisplayHeight(display);
    }

    private static int getDisplayHeight(Display display) {
        Point point = new Point();
        display.getSize(point);
        return point.y;
    }
}