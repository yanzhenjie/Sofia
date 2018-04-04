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

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by YanZhenjie on 2017/12/13.
 */
public interface Bar {

    Bar statusBarDarkFont();

    Bar statusBarLightFont();

    Bar statusBarBackground(int statusBarColor);

    Bar statusBarBackground(Drawable drawable);

    Bar statusBarBackgroundAlpha(int alpha);

    Bar navigationBarBackground(int navigationBarColor);

    Bar navigationBarBackground(Drawable drawable);

    Bar navigationBarBackgroundAlpha(int alpha);

    Bar invasionStatusBar();

    Bar invasionNavigationBar();

    /**
     * @deprecated use {@link #fitsStatusBarView(int)} instead.
     */
    @Deprecated
    Bar fitsSystemWindowView(int viewId);

    /**
     * @deprecated use {@link #fitsStatusBarView(View)} instead.
     */
    @Deprecated
    Bar fitsSystemWindowView(View view);

    Bar fitsStatusBarView(int viewId);

    Bar fitsStatusBarView(View view);

    Bar fitsNavigationBarView(int viewId);

    Bar fitsNavigationBarView(View view);
}