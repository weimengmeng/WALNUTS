package com.njjd.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.njjd.adapter.MyPagerAdapter;
import com.njjd.utils.DepthPageTransformer;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.VpSwipeRefreshLayout;
import com.njjd.walnuts.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/10.
 */

public class FindFragment extends BaseFragment {
    @BindView(R.id.find_page)
    ViewPager findPage;
    @BindView(R.id.layout_refresh)
    VpSwipeRefreshLayout layoutRefresh;
    @BindView(R.id.radio_hot)
    RadioButton radioHot;
    @BindView(R.id.radio_new)
    RadioButton radioNew;
    @BindView(R.id.top)
    LinearLayout top;
    private Context context;
    private List<View> viewList;
    private MyPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void initAfterSetContentView(Activity activity,
                                               View titleViewGroup) {
        if (activity == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            // 透明状态栏
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // 透明导航栏
//            window.addFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (titleViewGroup == null)
                return;
            // 设置头部控件ViewGroup的PaddingTop,防止界面与状态栏重叠
            int statusBarHeight = ImmersedStatusbarUtils.getStatusBarHeight(activity);
            titleViewGroup.setPadding(0, statusBarHeight, 0,0);
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAfterSetContentView(getActivity(), top);
        viewList = new ArrayList<>();
        viewList.add(view.inflate(context, R.layout.find_jinxuan, null));
        viewList.add(view.inflate(context, R.layout.find_zhuanlan, null));
        adapter = new MyPagerAdapter(viewList);
        findPage.setAdapter(adapter);
        findPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    radioHot.setChecked(true);
                }else{
                    radioNew.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findPage.setPageTransformer(true,new DepthPageTransformer());
    }


    @OnClick({R.id.radio_hot, R.id.radio_new})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radio_hot:
                findPage.setCurrentItem(0);
                break;
            case R.id.radio_new:
                findPage.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void lazyInitData() {

    }
}
