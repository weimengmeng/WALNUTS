package com.njjd.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.njjd.adapter.MyPagerAdapter;
import com.njjd.utils.CustomRadioButton;
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
    CustomRadioButton radioHot;
    @BindView(R.id.radio_new)
    CustomRadioButton radioNew;
    @BindView(R.id.button_group)
    RadioGroup buttonGroup;
    private Context context;
//    @BindView(R.id.top)
//    LinearLayout top;
//    @BindView(R.id.back)
//    TextView back;
//    @BindView(R.id.txt_title)
//    TextView txtTitle;
    private List<View> viewList;
    private MyPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), buttonGroup);
//        back.setVisibility(View.GONE);
//        txtTitle.setText("发现");
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
