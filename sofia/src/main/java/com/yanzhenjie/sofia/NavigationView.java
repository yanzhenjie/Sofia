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
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by YanZhenjie on 2017/8/1.
 */
public class NavigationView extends MeasureView {

    public NavigationView(Context context) {
        this(context, null, 0);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //当重新测量的的时候,一定要让父类先重新测量,因为sNavigationBarHeight有可能是0
         super.onMeasure(widthMeasureSpec,heightMeasureSpec);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), sNavigationBarHeight);
        } else {
            setMeasuredDimension(0, 0);
            setVisibility(View.GONE);
        }
    }
}