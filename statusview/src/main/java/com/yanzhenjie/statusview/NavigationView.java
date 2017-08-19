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
package com.yanzhenjie.statusview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by YanZhenjie on 2017/8/1.
 */
public class NavigationView extends View {

    private static boolean isInitialize = false;
    private static int NavigationBarHeight = 0;
    private static int screenWidth = 0;

    private static void initialize(View view) {
        if (isInitialize) return;
        isInitialize = true;

        Context context = view.getContext();
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class<?> displayClass = display.getClass();
            Method method = displayClass.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            NavigationBarHeight = displayMetrics.heightPixels - getDisplayHeight(display);
        } catch (Exception ignored) {
        }
    }

    private static int getDisplayHeight(Display display) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point point = new Point();
            display.getSize(point);
            return point.y;
        }
        return display.getHeight();
    }

    private static View getParentView(View view) {
        ViewParent viewParent = view.getParent();
        if (viewParent != null) {
            return getParentView((View) viewParent);
        }
        return view;
    }

    public NavigationView(Context context) {
        this(context, null, 0);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusView);
        int fitsViewId = typedArray.getInt(R.styleable.StatusView_fitsView, -1);
        typedArray.recycle();

        if (fitsViewId != -1) {
            View parent = getParentView(this);
            View fitsView = parent.findViewById(fitsViewId);

            if (fitsView != null &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP &&
                    Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                fitsView.setFitsSystemWindows(true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setMeasuredDimension(screenWidth, NavigationBarHeight);
        } else {
            setMeasuredDimension(0, 0);
            setVisibility(View.GONE);
        }
    }
}
