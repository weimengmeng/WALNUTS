package com.njjd.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.njjd.adapter.SelectAdapter;
import com.njjd.application.ConstantsVal;
import com.njjd.domain.ColumnArticleEntity;
import com.njjd.domain.ColumnEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.MyXRecyclerView;
import com.njjd.utils.NetworkUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.BaseActivity;
import com.njjd.walnuts.ColumnDetailActivity;
import com.njjd.walnuts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mrwim on 17/9/14.
 */

public class ColumnFragment extends BaseFragment implements HttpOnNextListener {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.list_find)
    MyXRecyclerView listFind;
    @BindView(R.id.lv_root)
    View root;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.top)
    LinearLayout top;
    private Context context;
    private SelectAdapter adapter;
    private List<ColumnEntity> columnEntities = new ArrayList<>();
    private List<ColumnArticleEntity> columnArticleEntities = new ArrayList<>();
    private MyReceiver receiver;
    private boolean loadmoe = true;
    ColumnEntity columnArticleEntity;
    private RecyclerView.RecycledViewPool viewPool;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_select, container, false);
        ButterKnife.bind(this, view);
        ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), top);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsVal.REFRESH_FIND);
        context.registerReceiver(receiver, filter);
        back.setVisibility(View.GONE);
        txtTitle.setText("专栏");
        viewPool= new RecyclerView.RecycledViewPool();
        adapter = new SelectAdapter(context, columnArticleEntities, columnEntities,viewPool);
        listFind.setRecycledViewPool(viewPool);
        listFind.setLayoutManager(new LinearLayoutManager(context));
        listFind.setAdapter(adapter);
        if (NetworkUtils.getNetworkType(context) == 0 || NetworkUtils.getNetworkType(context) == 1) {
            ((ImageView) view.findViewById(R.id.img_nodata)).setImageDrawable(getResources().getDrawable(R.drawable.no_net));
            ((TextView) view.findViewById(R.id.txt_content)).setText("请检查网络设置");
        }
        listFind.setEmptyView(view.findViewById(R.id.empty));
        listFind.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        listFind.setRefreshProgressStyle(ProgressStyle.BallPulse);
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(context, ColumnDetailActivity.class);
                intent.putExtra("article_id",Float.valueOf(columnArticleEntities.get(position).getArticle_id()).intValue()+"");
                startActivity(intent);
            }
        });
        getSelectArticle();
        getIndexColumn();
        listFind.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if(NetworkUtils.getNetworkType(context)==0||NetworkUtils.getNetworkType(context)==1){
                    ToastUtils.showShortToast(context,"网络中断，请检查您的网络状态");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listFind.refreshComplete();
                        }
                    },500);
                    return;
                }
                adapter.setCurrentPage(1);
                getIndexColumn();
                getSelectArticle();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        listFind.refreshComplete();
                    }
                }, 6000);
            }

            @Override
            public void onLoadMore() {
                if(NetworkUtils.getNetworkType(context)==0||NetworkUtils.getNetworkType(context)==1){
                    ToastUtils.showShortToast(context,"网络中断，请检查您的网络状态s");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listFind.loadMoreComplete();
                        }
                    },500);
                    return;
                }
                if (!loadmoe) {
                    ToastUtils.showShortToast(context, "已加载全部数据啦");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listFind.loadMoreComplete();
                        }
                    }, 500);
                    return;
                }
                adapter.setCurrentPage(adapter.getCurrentPage() + 1);
                getAllArticle();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    private void getSelectArticle() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(context, "userId", "").toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, context, false, false), map);
        HttpManager.getInstance().getColumnArticle(postEntity);
    }
    private void getAllArticle() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(context, "userId", "").toString());
        map.put("page", adapter.getCurrentPage()==1?1:adapter.getCurrentPage()-1);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, context, false, false), map);
        HttpManager.getInstance().getAllArticle(postEntity);
    }
    private void getIndexColumn() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(context, "userId", "").toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getColumnListener, context, false, false), map);
        HttpManager.getInstance().getIndexColumn(postEntity);
    }

    HttpOnNextListener getColumnListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            columnEntities.clear();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls(); //重点
            Gson gson = gsonBuilder.create();
            try {
                JSONObject object = new JSONObject(gson.toJson(o));
                JSONArray array = object.getJSONArray("column");
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    columnArticleEntity = new ColumnEntity(object);
                    columnEntities.add(columnArticleEntity);
                }
                adapter.setColumnEntities(columnEntities);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onNext(Object o) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        try {
            JSONObject object = new JSONObject(gson.toJson(o));
            JSONArray array = object.getJSONArray("column");
            ColumnArticleEntity entity;
            if(adapter.getCurrentPage()==1){
                columnArticleEntities.clear();
                listFind.refreshComplete();
            }else{
                listFind.loadMoreComplete();
            }
            if(array.length()==0){
                BaseActivity.showToast2(root,"已加载全部数据");
                return;
            }
            for (int i = 0; i < array.length(); i++) {
                entity = new ColumnArticleEntity(array.getJSONObject(i));
                if(adapter.getCurrentPage()==1){
                    columnArticleEntities.add(entity);
                }else{
                    if(array.getJSONObject(i).getString("is_select").equals("0.0")){
                        columnArticleEntities.add(entity);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            listFind.smoothScrollToPosition(0);
            listFind.setPullRefreshEnabled(true);
            adapter.setCurrentPage(1);
            listFind.refresh();
        }
    }

    @Override
    public void lazyInitData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        context.unregisterReceiver(receiver);
    }
}
