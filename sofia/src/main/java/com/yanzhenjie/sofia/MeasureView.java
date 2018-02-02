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

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by YanZhenjie on 2017/12/13.
 */
public class MeasureView extends View {

    private static boolean isInitialize = false;
    protected static int sStatusBarHeight = 0;
    protected static int sNavigationBarHeight = 0;

    private static void measureScreenSize(Context context) {
        if (isInitialize) return;
        isInitialize = true;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
         DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1)
            display.getRealMetrics(displayMetrics);
        else display.getMetrics(displayMetrics);

        sNavigationBarHeight = displayMetrics.heightPixels - getDisplayHeight(display);

        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Field field = clazz.getField("status_bar_height");
            int x = Integer.parseInt(field.get(clazz.newInstance()).toString());
            sStatusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Throwable ignored) {
        }
    }

    private static int getDisplayHeight(Display display) {
        Point point = new Point();
        display.getSize(point);
        return point.y;
    }

    public MeasureView(Context context) {
        this(context, null, 0);
    }

    public MeasureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //这里重测量的时候如果有任何一个是0,那么一定要让重新获取导航栏和状态栏,主要是因为先横屏,后竖屏,横屏时候初始化view不可见,也没获取到系统导航栏的高度
        if (sStatusBarHeight==0||sNavigationBarHeight==0){
            isInitialize=false;
            measureScreenSize(getContext());
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public MeasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        measureScreenSize(context);
    }
}