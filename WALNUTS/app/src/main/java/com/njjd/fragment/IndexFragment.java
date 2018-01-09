package com.njjd.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.njjd.adapter.IndexQuestionAdapter;
import com.njjd.adapter.IndexSelectAdapter;
import com.njjd.adapter.MyPagerAdapter;
import com.njjd.adapter.SelectAdapter;
import com.njjd.application.ConstantsVal;
import com.njjd.db.DBHelper;
import com.njjd.domain.IndexNavEntity;
import com.njjd.domain.QuestionEntity;
import com.njjd.domain.SelectedAnswerEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.MyXRecyclerView;
import com.njjd.utils.NetworkUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.BaseActivity;
import com.njjd.walnuts.IndexDetailActivity;
import com.njjd.walnuts.R;
import com.njjd.walnuts.SearchActivity;
import com.njjd.walnuts.SelectAnswerDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/10.
 */

public class IndexFragment extends BaseFragment implements View.OnClickListener, HttpOnNextListener {
    @BindView(R.id.img_order)
    LinearLayout imgOrder;
    @BindView(R.id.lv_root)
    View root;
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
    private View mainView;
    private RadioButton layoutTop, layoutTime;
    private PopupWindow popupWindow;
    private LayoutInflater myinflater;
    private View currentView;
    private MyXRecyclerView list;
    private List<QuestionEntity> tempList;
    private List<List<QuestionEntity>> lists = new ArrayList<>();
    private IndexQuestionAdapter questionAdapter;
    private List<MyXRecyclerView> listViews = new ArrayList<>();
    private List<IndexQuestionAdapter> adapterList = new ArrayList<>();
    private List<IndexNavEntity> navList;
    private String tempKind = "1", ids = "";
    private String tempOrder = "time";
    private MyReceiver receiver;
    private boolean loadmoe=true;
    private List<SelectedAnswerEntity> selectedAnswerEntities = new ArrayList<>();
    private SelectedAnswerEntity entity;
    private IndexSelectAdapter indexSelectAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        myinflater = LayoutInflater.from(context);
        navList = CommonUtils.getInstance().getNavsList();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void lazyInitData() {
        //获取问题
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsVal.REFRESH);
        context.registerReceiver(receiver, filter);
        mainView = LayoutInflater.from(context).inflate(R.layout.layout_pop, null);
        layoutTop =  mainView.findViewById(R.id.rb_hot);
        layoutTop.setText("按热度排序");
        layoutTime =  mainView.findViewById(R.id.rb_time);
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
        initRefresh();
        initTop(view);
    }
    private void initTop(View view) {
        viewList = new ArrayList<>();
        for (int i = 0; i < navList.size()+1; i++) {
            RadioButton button = (RadioButton) myinflater.inflate(R.layout.item_radiobutton, null);
            if(i==0){
                button.setText("精选");
                button.setTag("0");
                button.setId(i);
                button.setOnClickListener(this);
                buttonGroup.addView(button);
                currentView = view.inflate(context, R.layout.layout_common_index, null);
                list = currentView.findViewById(R.id.list_index);
                indexSelectAdapter = new IndexSelectAdapter(context, selectedAnswerEntities);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                list.setLayoutManager(layoutManager);//这里用线性显示 类似于listview
                list.setAdapter(indexSelectAdapter);
                if (NetworkUtils.getNetworkType(context) == 0 || NetworkUtils.getNetworkType(context) == 1) {
                    ((ImageView) currentView.findViewById(R.id.empty).findViewById(R.id.img_nodata)).setImageDrawable(getResources().getDrawable(R.drawable.no_net));
                    ((TextView) currentView.findViewById(R.id.empty).findViewById(R.id.txt_content)).setText("请检查网络设置");
                }
                list.setEmptyView(currentView.findViewById(R.id.empty));
                list.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
                list.setRefreshProgressStyle(ProgressStyle.BallPulse);
                listViews.add(list);
                indexSelectAdapter.setOnItemClickListener(new IndexSelectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent;
                        entity = selectedAnswerEntities.get(position);
                        intent = new Intent(context, SelectAnswerDetailActivity.class);
                        intent.putExtra("questionId", entity.getArticle_id());
                        intent.putExtra("questionTitle", entity.getTitle());
                        intent.putExtra("answer_id", entity.getAnswer_id());
                        intent.putExtra("contents",entity.getContents());
                        startActivity(intent);
                    }
                });
            }else{
                button.setText(navList.get(i-1).getName());
                button.setTag(navList.get(i-1).getId());
                button.setId(i);
                button.setOnClickListener(this);
                buttonGroup.addView(button);
                final List<QuestionEntity> list1 =new ArrayList<>();
                lists.add(list1);
                currentView = view.inflate(context, R.layout.layout_common_index, null);
                list = currentView.findViewById(R.id.list_index);
                final IndexQuestionAdapter questionAdapter = new IndexQuestionAdapter(context, list1, navList.get(i-1).getId());
                final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                list.setLayoutManager(layoutManager);//这里用线性显示 类似于listview
                adapterList.add(questionAdapter);
                list.setAdapter(questionAdapter);
                if (NetworkUtils.getNetworkType(context) == 0 || NetworkUtils.getNetworkType(context) == 1) {
                    ((ImageView) currentView.findViewById(R.id.empty).findViewById(R.id.img_nodata)).setImageDrawable(getResources().getDrawable(R.drawable.no_net));
                    ((TextView) currentView.findViewById(R.id.empty).findViewById(R.id.txt_content)).setText("请检查网络设置");
                }
                list.setEmptyView(currentView.findViewById(R.id.empty));
                list.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
                list.setRefreshProgressStyle(ProgressStyle.BallPulse);
                listViews.add(list);
                questionAdapter.setOnItemClickListener(new IndexQuestionAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, IndexDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("question", list1.get(position));
                        intent.putExtra("question", bundle);
                        intent.putExtra("type", "1");
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                    }
                });
            }
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
        indexPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0||position==1){
                    imgOrder.setVisibility(View.INVISIBLE);
                }else{
                    imgOrder.setVisibility(View.VISIBLE);
                }
                buttonGroup.check(position);
                list = listViews.get(position);
                if(position!=0) {
                    tempList = lists.get(position-1);
                    questionAdapter = adapterList.get(position-1);
                    tempKind = navList.get(position-1).getId();
                    if(NetworkUtils.getNetworkType(context)==0||NetworkUtils.getNetworkType(context)==1||NetworkUtils.getNetworkType(context)==4){
                        ToastUtils.showShortToast(context,"网络貌似不给力哦");
                        List<QuestionEntity> entities=DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().queryRaw("where kind = ?",new String[]{tempKind});
                        for (QuestionEntity e:entities
                                ) {
                            tempList.add(e);
                        }
                        questionAdapter.notifyDataSetChanged();
                    }else {
                        if (tempList.size() == 0) {
                            getQuestion(tempKind, tempOrder);
                        }
                    }
                }else{
                    if(selectedAnswerEntities.size()==0){
                        getSelectedAnswerList();
                    }
                }
                setRefreshListener();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indexPage.setCurrentItem(1);
    }

    private void setRefreshListener() {
        list.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if(NetworkUtils.getNetworkType(context)==0||NetworkUtils.getNetworkType(context)==1){
                   BaseActivity.showToast2(root,"网络中断，请检查您的网络状态");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            list.refreshComplete();
                        }
                    },500);
                    return;
                }
                if(indexPage.getCurrentItem()==0){
                    indexSelectAdapter.setCurrentPage(1);
                    getSelectedAnswerList();
                }else{
                    questionAdapter.setCurrentPage(1);
//                CommonUtils.init(context);
                    getQuestion(tempKind, tempOrder);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.refreshComplete();
                    }
                },6000);
            }
            @Override
            public void onLoadMore() {
                if(NetworkUtils.getNetworkType(context)==0||NetworkUtils.getNetworkType(context)==1){
                    BaseActivity.showToast2(root,"网络中断，请检查您的网络状态");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            list.loadMoreComplete();
                        }
                    },500);
                    return;
                }
                if(!loadmoe){
                    BaseActivity.showToast2(root,"已加载全部数据");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            list.loadMoreComplete();
                        }
                    }, 500);
                    return;
                }
                if(indexPage.getCurrentItem()==0){
                    indexSelectAdapter.setCurrentPage(indexSelectAdapter.getCurrentPage() + 1);
                    getSelectedAnswerList();
                }else{
                    questionAdapter.setCurrentPage(questionAdapter.getCurrentPage() + 1);
                    getQuestion(tempKind, tempOrder);
                }
                list.loadMoreComplete();
            }
        });
    }

    private void getQuestion(String id, String sort) {
        if (tempList.size() == 0) {
            questionAdapter.setCurrentPage(1);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("cate_article_id", Float.valueOf(id).intValue());
        map.put("page", questionAdapter.getCurrentPage());
        if (questionAdapter.getCurrentPage() ==1 && indexPage.getCurrentItem() ==1) {
            map.put("refresh", "1");
            map.put("article_id", ids);
        } else {
            if (indexPage.getCurrentItem() == 1) {
                map.put("article_id", ids);
            }
            map.put("refresh", "0");
        }
        map.put("order", sort);
        map.put("keywords", "");
        map.put("uid", SPUtils.get(context, "userId", "").toString());
        map.put("token", SPUtils.get(context, "token", "").toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, context, false, false), map);
        HttpManager.getInstance().getQuestionList(postEntity);
    }

    @Override
    public void onNext(Object o) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        JSONObject object = null;
        JSONArray array = null;
        QuestionEntity entity;
        try {
            object = new JSONObject(gson.toJson(o));
            array = object.getJSONArray("article");
            if (questionAdapter.getCurrentPage() == 1) {
                list.refreshComplete();
                if (indexPage.getCurrentItem() == 1)
                    ids = "";
                tempList.clear();
            } else {
                list.loadMoreComplete();
            }
            if(array.length()==0){
                loadmoe=false;
                return;
            }else{
                loadmoe=true;
            }
            for (int i = 0; i < array.length(); i++) {
                entity = new QuestionEntity(array.getJSONObject(i), tempKind);
                if (indexPage.getCurrentItem() == 1 && questionAdapter.getCurrentPage() ==1) {
                    if(i==(array.length()-1)){
                        ids += Float.valueOf(entity.getQuestionId()).intValue();
                    }else {
                        ids += Float.valueOf(entity.getQuestionId()).intValue() + ",";
                    }
                }
                tempList.add(entity);
                if (DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().load(entity.getQuestionId()) != null) {
                    QuestionEntity temp = DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().load(entity.getQuestionId());
                    temp.setTitle(entity.getTitle());
                    temp.setContent(entity.getContent());
                    temp.setIsFocus(entity.getIsFocus());
                    temp.setAnswerNum(entity.getAnswerNum());
                    temp.setFocusNum(entity.getFocusNum());
                    temp.setPic(entity.getPic());
                    temp.setPhoto(entity.getPhoto());
                    temp.setPart_num(entity.getPart_num());
                    DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().insertOrReplace(temp);
                } else {
                    DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().insertOrReplace(entity);
                }
            }
            questionAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            LogUtils.d(e.toString());
        }
    }
    private void getSelectedAnswerList() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(context, "userId", "").toString());
        map.put("token", SPUtils.get(context, "token", "").toString());
        map.put("page", indexSelectAdapter.getCurrentPage());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(selectListener, context, false, false), map);
        HttpManager.getInstance().getHotComment(postEntity);
    }
    private HttpOnNextListener selectListener=new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls(); //重点
            Gson gson = gsonBuilder.create();
            JSONObject object = null;
            JSONArray array = null;
            SelectedAnswerEntity answerEntity;
            try {
                object = new JSONObject(gson.toJson(o));
                array = object.getJSONArray("comment");
                if (indexSelectAdapter.getCurrentPage() == 1) {
                    selectedAnswerEntities.clear();
                    list.refreshComplete();
                } else {
                    list.loadMoreComplete();
                }
                if (array.length() < 20) {
                    loadmoe = false;
                } else {
                    loadmoe = true;
                }
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    answerEntity = new SelectedAnswerEntity(object);
                    selectedAnswerEntities.add(answerEntity);
                }
                indexSelectAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private void initRefresh() {
    }

    @OnClick({R.id.img_order,R.id.et_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_order:
                if(indexPage.getCurrentItem()==0){
                    return;
                }
                popupWindow.showAsDropDown(imgOrder, 0, 0);
                break;
            case R.id.et_search:
                startActivity(new Intent(context, SearchActivity.class));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_hot:
                tempOrder = "hot";
                tempList.clear();
                getQuestion(tempKind, tempOrder);
                popupWindow.dismiss();
                break;
            case R.id.rb_time:
                tempOrder = "time";
                tempList.clear();
                getQuestion(tempKind, tempOrder);
                popupWindow.dismiss();
                break;
            default:
                if(v.getId()==0){
                    buttonGroup.check(0);
                    indexPage.setCurrentItem(0);
                    return;
                }
                buttonGroup.check(v.getId());
                indexPage.setCurrentItem(v.getId());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class MyReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (tempList != null && questionAdapter != null && list != null) {
                list.smoothScrollToPosition(0);
                list.setPullRefreshEnabled(true);
                list.refresh();
                if(indexPage.getCurrentItem()==0){
                    indexSelectAdapter.setCurrentPage(1);
                    getSelectedAnswerList();
                }else{
                    questionAdapter.setCurrentPage(1);
                    getQuestion(tempKind, tempOrder);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}