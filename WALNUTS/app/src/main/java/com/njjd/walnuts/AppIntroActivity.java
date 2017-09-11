package com.njjd.walnuts;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.example.retrofit.welcome.adapter.BaseFragmentAdapter;
import com.example.retrofit.welcome.fragment.FirstLauncherFragment;
import com.example.retrofit.welcome.fragment.GoLauncherFragment;
import com.example.retrofit.welcome.fragment.LauncherBaseFragment;
import com.example.retrofit.welcome.fragment.SecondLauncherFragment;
import com.example.retrofit.welcome.fragment.ThirdLauncherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrwim on 17/7/17.
 */

public class AppIntroActivity extends FragmentActivity {
    private ViewPager vPager;
    private List<LauncherBaseFragment> list = new ArrayList<>();
    private BaseFragmentAdapter adapter;
    private int currentSelect=0;
    LauncherBaseFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintro);
        /**
         * 初始化三个fragment  并且添加到list中
         */
        FirstLauncherFragment first = new FirstLauncherFragment();
        SecondLauncherFragment second = new SecondLauncherFragment();
        ThirdLauncherFragment third = new ThirdLauncherFragment();
        GoLauncherFragment goLauncherFragment = new GoLauncherFragment();
        list.add(first);
        list.add(second);
        list.add(third);
        list.add(goLauncherFragment);
        vPager = (ViewPager) findViewById(R.id.viewpager_launcher);
        adapter = new BaseFragmentAdapter(getSupportFragmentManager(),list);
        vPager.setAdapter(adapter);
        vPager.setOffscreenPageLimit(2);
        vPager.setCurrentItem(0);
        vPager.setOnPageChangeListener(changeListener);
    }

    /**
     * 监听viewpager的移动
     */
    ViewPager.OnPageChangeListener changeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int index) {
            if(index!=3){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.setStatusBarColor(getResources().getColor(R.color.white));
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.setStatusBarColor(getResources().getColor(R.color.login));
                }
            }
            fragment=list.get(index);
            list.get(currentSelect).stopAnimation();//停止前一个页面的动画
            fragment.startAnimation();//开启当前页面的动画
            currentSelect=index;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}
        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };
}
