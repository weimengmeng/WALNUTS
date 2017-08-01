package com.njjd.walnuts;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

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

//    private ImageView[] tips;
    private int currentSelect=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintro);
       /* //初始化点点点控件
        ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup);
        tips = new ImageView[4];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            tips[i]=imageView;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 20;//设置点点点view的左边距
            layoutParams.rightMargin = 20;//设置点点点view的右边距
            group.addView(imageView,layoutParams);
        }*/
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
//            setImageBackground(index);//改变点点点的切换效果
            LauncherBaseFragment fragment=list.get(index);

            list.get(currentSelect).stopAnimation();//停止前一个页面的动画
            fragment.startAnimation();//开启当前页面的动画

            currentSelect=index;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}
        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };

//    /**
//     * 改变点点点的切换效果
//     * @param selectItems
//     */
//    private void setImageBackground(int selectItems) {
//        for (int i = 0; i < tips.length; i++) {
//            if (i == selectItems) {
//                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
//            } else {
//                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
//            }
//        }
//    }
}
