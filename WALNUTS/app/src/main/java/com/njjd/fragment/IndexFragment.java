package com.njjd.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.njjd.adapter.IndexQuestionAdapter;
import com.njjd.adapter.MyPagerAdapter;
import com.njjd.domain.QuestionEntity;
import com.njjd.fragment.BaseFragment;
import com.njjd.utils.DepthPageTransformer;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.MyListView;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.IndexDetailActivity;
import com.njjd.walnuts.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGAMeiTuanRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by mrwim on 17/7/10.
 */

public class IndexFragment extends BaseFragment implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {
    @BindView(R.id.img_order)
    LinearLayout imgOrder;
    @BindView(R.id.layout_refresh)
    BGARefreshLayout layoutRefresh;
    @BindView(R.id.button_group)
    RadioGroup buttonGroup;
    @BindView(R.id.scrollView)
    HorizontalScrollView scrollView;
    @BindView(R.id.index_page)
    ViewPager indexPage;
    private List<View> viewList;
    private MyPagerAdapter adapter;
    private Context context;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    private View mainView;
    private LinearLayout layoutTop, layoutTime;
    private PopupWindow popupWindow;
    private LayoutInflater myinflater;
    private View currentView;
    private Banner banner;
    private MyListView list;
    private List<String> images= new ArrayList<>(Arrays.asList(
            "http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg",
            "http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg",
            "http://img.zcool.cn/community/0114a856640b6d32f87545731c076a.jpg"));
    private List<String> titles=new ArrayList<>(Arrays.asList("12趁现在","嗨购5折不要停，12.12趁现在","实打实大顶顶顶顶"));
    private List<QuestionEntity> tempList;
    private List<List<QuestionEntity>> lists=new ArrayList<>();
    private IndexQuestionAdapter questionAdapter;
    private List<IndexQuestionAdapter> adapterList=new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            layoutRefresh.endRefreshing();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        myinflater = LayoutInflater.from(context);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void lazyInitData() {
        //获取导航栏分类、广告图、问题
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), top);
        back.setVisibility(View.GONE);
        txtTitle.setText("首页");
        mainView = LayoutInflater.from(context).inflate(R.layout.layout_pop, null);
        layoutTop = ((LinearLayout) mainView.findViewById(R.id.lv_top));
        layoutTime = (LinearLayout) mainView.findViewById(R.id.lv_time);
        layoutTop.setOnClickListener(this);
        layoutTime.setOnClickListener(this);
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(mainView);
        popupWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setAnimationStyle(R.style.AnimTools);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setFocusable(true);
        popupWindow.update();
//        getNav();
        initRefresh();
        initTop(view);
    }

    private void initTop(View view) {
        viewList = new ArrayList<>();
        for (int i = 0; i <4; i++) {
            RadioButton button = (RadioButton) myinflater.inflate(R.layout.item_radiobutton, null);
            button.setText("精选");
            button.setTag(i + "");
            button.setId(i);
            button.setOnClickListener(this);
            if (i == 0) {
                button.setChecked(true);
            }
            buttonGroup.addView(button);
            final List<QuestionEntity> list1=new ArrayList<>();
            lists.add(list1);
            currentView=view.inflate(context, R.layout.layout_common_index, null);
            list= (MyListView) currentView.findViewById(R.id.list_index);
            banner=(Banner) currentView.findViewById(R.id.banner);
            if(i==0) {
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                banner.setIndicatorGravity(BannerConfig.RIGHT);
                banner.setBannerTitles(titles);
                banner.isAutoPlay(true);
                banner.setDelayTime(3000);
                banner.setImages(images).setImageLoader(GlideImageLoder.getInstance()).start();
                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        ToastUtils.showShortToast(context, "你点击了：" + position);
                    }
                });
            }else{
                banner.setVisibility(View.GONE);
            }
            list1.add(new QuestionEntity("1","我的销售领导和我的销售风格不一致，怎么办？","我在一家私营企业，主要是做微信商城搭建、公司网站建设的，一般都是先打电话约对方老板，然后上门去拜访。\n" +
                    "可能是过去的职业习惯，我喜欢先去把每个要电话约访的企业资料先收集好，再去打电话，我感觉这样更有效率和针对性，但是我的领导喜欢在数量上做文章，希望我每天尽可能的多打电话，朋友们你们觉得我应该怎么办？","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg","","30","20",0,"2017-7-26","电销,外访"));
            final IndexQuestionAdapter questionAdapter=new IndexQuestionAdapter(context,list1);
            adapterList.add(questionAdapter);
            list.setAdapter(questionAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(context, IndexDetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("question",list1.get(position));
                    intent.putExtra("question",bundle);
                    startActivity(intent);
                }
            });
            viewList.add(currentView);
        }
        Display d = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        d.getMetrics(dm);
        final int screenHalf = d.getWidth() / 2;//屏幕宽度的一半
        buttonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int scrollX = scrollView.getScrollX();
                RadioButton rb = (RadioButton) group.getChildAt(checkedId);
                int left = rb.getLeft();
                int leftScreen = left - scrollX;
                scrollView.smoothScrollBy((leftScreen - screenHalf), 0);
            }
        });
        adapter = new MyPagerAdapter(viewList);
        indexPage.setAdapter(adapter);
        indexPage.setPageTransformer(true,new DepthPageTransformer());
        indexPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                buttonGroup.check(position);
                currentView=viewList.get(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indexPage.setCurrentItem(0);
    }
    private void setpage(int page){
        //currentView=viewList.get(page);
//        list= (MyListView) currentView.findViewById(R.id.list_index);
//        banner=(Banner) currentView.findViewById(R.id.banner);
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
//        banner.setIndicatorGravity(BannerConfig.RIGHT);
//        banner.setBannerTitles(titles);
//        banner.isAutoPlay(true)    ;
//        banner.setDelayTime(3000);
//        banner.setImages(images).setImageLoader(GlideImageLoder.getInstance()).start();
//        banner.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(int position) {
//                ToastUtils.showShortToast(context, "你点击了：" + position);
//            }
//        });
//        tempList=lists.get(page);
//        tempList.clear();
//        tempList.add(new QuestionEntity("1",page+"喝酒不要开车1","树大根深过如果如果如果特惠树大根深过如果如果如果特惠树大根深过如果如果如果特惠树大根深过如果如果如果特惠树大根深过如果如果如果特惠树大根深过如果如果如果特惠树大根深过如果如果如果特惠树大根深过如果如果如果特惠树大根深过如果如果如果特惠树大根深过如果如果如果特惠","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg","","30","20",0,"2017-7-26"));
//        tempList.add(new QuestionEntity("2","喝酒不要开车2喝酒不要开车2喝酒不要开车2喝酒不要开车2喝酒不要开车2喝酒不要","树大根深过如果如果如果特惠","","hhah","80","30",0,"2017-7-26"));
//        tempList.add(new QuestionEntity("3","喝酒不要开车3","树大根深过如果如果如果特惠树大根深过如果如果如果特惠树大根深过如果如果如果特惠","http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg,http://img.zcool.cn/community/0114a856640b6d32f87545731c076a.jpg","","10","70",0,"2017-7-26"));
//        questionAdapter=new IndexQuestionAdapter(context,tempList);
//        list.setAdapter(questionAdapter);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent(context, IndexDetailActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("question",tempList.get(position));
//                intent.putExtra("question",bundle);
//                startActivity(intent);
//            }
//        });
    }
    private void initRefresh() {
        layoutRefresh.setDelegate(this);
//        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGAMeiTuanRefreshViewHolder refreshViewHolder = new MyRefresh(context, false);
        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("正在加载");
//        // 设置整个加载更多控件的背景颜色资源 id
//        refreshViewHolder.setLoadMoreBackgroundColorRes(loadMoreBackgroundColorRes);
//        // 设置整个加载更多控件的背景 drawable 资源 id
//        refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
//        // 设置下拉刷新控件的背景 drawable 资源 id
        refreshViewHolder.setRefreshViewBackgroundDrawableRes(R.drawable.app_icon1);
        refreshViewHolder.setPullDownImageResource(R.drawable.bga_refresh_mt_pull_down);
        refreshViewHolder.setRefreshingAnimResId(R.drawable.bga_refresh_mt_refreshing);
        refreshViewHolder.setChangeToReleaseRefreshAnimResId(R.drawable.bga_refresh_mt_change_to_release_refresh);
        // 设置下拉刷新和上拉加载更多的风格
        layoutRefresh.setRefreshViewHolder(refreshViewHolder);
    }


    @OnClick(R.id.img_order)
    public void onViewClicked() {
        popupWindow.showAsDropDown(imgOrder, 0, 0);
    }

    class MyRefresh extends BGAMeiTuanRefreshViewHolder {
        /**
         * @param context
         * @param isLoadingMoreEnabled 上拉加载更多是否可用
         */
        public MyRefresh(Context context, boolean isLoadingMoreEnabled) {
            super(context, isLoadingMoreEnabled);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lv_top:
                ToastUtils.showShortToast(context, "热度排序");
                break;
            case R.id.lv_time:
                ToastUtils.showShortToast(context, "时间排序");
                break;
            default:
                buttonGroup.check(v.getId());
                indexPage.setCurrentItem(v.getId());
                break;
        }
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        LogUtils.d("刷新啦");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(banner!=null){
            banner.stopAutoPlay();
        }
    }
}
