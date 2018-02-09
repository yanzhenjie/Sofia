package com.yanzhenjie.sofia;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;


/**
 * Created by lizhanqi on 2018/2/2.
 * 适配底部虚拟导航栏
 */

public class AndroidUiChangedListener {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    /**
     * 关联要监听的视图
     *Listenerview
     * @param viewObserving
     */
    public static void assistActivity(View viewObserving) {
        new AndroidUiChangedListener(viewObserving);
    }

    private View mViewObserved;//被监听的视图
    private int usableHeightPrevious;//视图变化前的可用高度
    private ViewGroup.LayoutParams frameLayoutParams;

    private AndroidUiChangedListener(View viewObserving) {
        mViewObserved = viewObserving;
        //给View添加全局的布局监听器
        mViewObserved.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                resetLayoutByUsableHeight(computeUsableHeight());
            }
        });
        frameLayoutParams = mViewObserved.getLayoutParams();
    }
  static   UiChangedListener uiChangedListener;
    public  interface  UiChangedListener{
        void   onChanged();
    }
    public static void setUiChangedListener(UiChangedListener uiChangedListener) {
        AndroidUiChangedListener.uiChangedListener = uiChangedListener;
    }

    private void resetLayoutByUsableHeight(int usableHeightNow) {
        //比较布局变化前后的View的可用高度
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致
            //将当前的View的可用高度设置成View的实际高度
            frameLayoutParams.height = usableHeightNow;
            mViewObserved.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
            if (uiChangedListener!=null){
                uiChangedListener.onChanged();
            }
        }
    }

    /**
     * 计算视图可视高度
     *
     * @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        mViewObserved.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }
}
