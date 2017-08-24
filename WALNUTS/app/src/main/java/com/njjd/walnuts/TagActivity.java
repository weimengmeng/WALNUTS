package com.njjd.walnuts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.njjd.adapter.IndexQuestionAdapter2;
import com.njjd.domain.QuestionEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/28.
 */

public class TagActivity extends BaseActivity  {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.listview)
    XRecyclerView listview;
    @BindView(R.id.txt_tag)
    TextView txtTag;
    @BindView(R.id.txt_focus)
    TextView txtFocus;
    private List<QuestionEntity> list=new ArrayList<>();
    private IndexQuestionAdapter2 adapter;
    private boolean isLoading=false;
    @Override
    public int bindLayout() {
        return R.layout.activity_tag;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView(this,back);
        back.setText("");
        txtTag.setText(getIntent().getStringExtra("name"));
        adapter=new IndexQuestionAdapter2(this,list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listview.setLayoutManager(layoutManager);//这里用线性显示 类似于listview
        listview.setAdapter(adapter);
        getTagQuestion();
        setRefreshListener();
        adapter.setOnItemClickListener(new IndexQuestionAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(TagActivity.this, IndexDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("question", list.get(position));
                intent.putExtra("question", bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
    }
    private void setRefreshListener(){
        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                adapter.setCurrentPage(1);
                getTagQuestion();
            }

            @Override
            public void onLoadMore() {
                adapter.setCurrentPage(adapter.getCurrentPage() + 1);
               getTagQuestion();
                isLoading=true;
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void getTagQuestion(){
        Map<String,Object> map=new HashMap<>();
        map.put("page", adapter.getCurrentPage());
        map.put("label_id",Float.valueOf(getIntent().getStringExtra("tag_id")).intValue());
        map.put("uid",SPUtils.get(this,"userId","").toString());
        map.put("token",SPUtils.get(this,"token","").toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), map);
        HttpManager.getInstance().getQuestionList(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        JSONObject object=null;
        JSONArray array = null;
        QuestionEntity entity;
        try {
            object = new JSONObject(gson.toJson(o));
            array=object.getJSONArray("article");
            if(object.getString("label_stat").equals("1.0")){
                txtFocus.setText("已关注");
                txtFocus.setEnabled(false);
            }
            if (adapter.getCurrentPage() == 1) {
                listview.refreshComplete();
                list.clear();
            }
            if(isLoading){
                isLoading=false;
                listview.loadMoreComplete();
            }
            for (int i = 0; i < array.length(); i++) {
                entity = new QuestionEntity(array.getJSONObject(i),"");
                list.add(entity);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            LogUtils.d(e.toString());
        }
    }
    @OnClick({R.id.back,R.id.txt_focus})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.txt_focus:
                addFocus();
                break;
        }
    }
    private void addFocus(){
        Map<String,Object> map=new HashMap<>();
        map.put("label_id",getIntent().getStringExtra("tag_id"));
        map.put("token", SPUtils.get(this,"token","").toString());
        map.put("uid",SPUtils.get(this,"userId","").toString());
        map.put("select","1");
        SubjectPost postEntity=new SubjectPost(new ProgressSubscriber(focusListener,this,false,false),map);
        HttpManager.getInstance().focusLabel(postEntity);
    }
    HttpOnNextListener focusListener=new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            ToastUtils.showShortToast(TagActivity.this," 关注成功");
            txtFocus.setText("已关注");
            txtFocus.setEnabled(false);
        }
    };
}
