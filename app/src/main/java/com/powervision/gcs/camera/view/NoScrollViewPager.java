package com.powervision.gcs.camera.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以禁止滑动
 * Created by Sundy on 2017/6/8.
 */

public class NoScrollViewPager extends ViewPager {
    private boolean noScroll=false;
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(noScroll)
           return false;
       else
        return super.onInterceptTouchEvent(ev);
    }

}
