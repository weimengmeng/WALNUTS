package com.njjd.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by mrwim on 17/7/26.
 */

public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                 int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                                MeasureSpec.AT_MOST);
                 super.onMeasure(widthMeasureSpec, expandSpec);
             }
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
////        if (getFirstVisiblePosition() == 0 &&getChildAt(0)!=null&& getChildAt(0).getTop() == 0) {//到头部了
////            getParent().requestDisallowInterceptTouchEvent(false);//放行触摸
////        } else {//没有到头部
////            getParent().requestDisallowInterceptTouchEvent(true);//拦截触摸
////        }
//        return super.onInterceptTouchEvent(ev);
//    }
}