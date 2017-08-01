package com.njjd.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by mrwim on 17/7/19.
 */

public class MyPagerAdapter extends PagerAdapter{
    /*
     * 使用 pagerAdapter  注意几点：
     * 重写方法的时候是 含有 ViewGroup的方法 :
     * 适合用在 使用 layout 布局实现
     * instantiateItem(ViewGroup container, int position)
     * destroyItem(ViewGroup container, int position, Object object)
     *
     */
    private List<View> views;

    public MyPagerAdapter(List<View> views) {
        this.views=views;
    }

    /**
     * 返回 页卡 的数量
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return views.size();
    }

    /**
     *  判断 是 view 是否来自对象
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0==arg1;
    }

    /**
     * 实例化 一个 页卡
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 添加一个 页卡

        container.addView(views.get(position));

        return views.get(position);
    }

    /**
     * 销毁 一个 页卡
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 删除
        container.removeView(views.get(position));
    }
}
