package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.njjd.adapter.MyAnswerAdapter;
import com.njjd.domain.MyAnswerEntity;
import com.njjd.domain.SaveEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/13.
 */

public class MyAnswerActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.list_save)
    SwipeMenuListView listSave;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    private List<MyAnswerEntity> list = new ArrayList<>();
    private MyAnswerAdapter saveAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_mysave;
    }

    @Override
    public void initView(View view) {
        back.setText("返回");
        if(getIntent().getStringExtra("uid").equals(SPUtils.get(this,"userId","").toString())) {
            txtTitle.setText("我的回答");
        }else{
            txtTitle.setText("TA的回答");
        }
        saveAdapter = new MyAnswerAdapter(list, this);
        findViewById(R.id.radiogroup).setVisibility(View.GONE);
        listSave.setEmptyView(findViewById(R.id.empty));
        if(SPUtils.get(this,"userId","").equals(getIntent().getStringExtra("uid"))) {
            ((TextView)findViewById(R.id.txt_content)).setText("快去回答问题吧");
        }else{
            ((TextView) findViewById(R.id.txt_content)).setText("TA还没有回答");
        }

        listSave.setAdapter(saveAdapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyAnswerAdapter.CURRENT_PAGE = 1;
                getMySave();
            }
        });
        listSave.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        MyAnswerAdapter.CURRENT_PAGE++;
                        getMySave();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (listSave != null && listSave.getChildCount() > 0) {
                    boolean firstItemVisible = listSave.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listSave.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refresh.setEnabled(enable);
            }
        });
        listSave.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list.get(position).getType().equals("2.0")){
                    Intent intent=new Intent(MyAnswerActivity.this, ColumnDetailActivity.class);
                    intent.putExtra("article_id",Float.valueOf(list.get(position).getArticle_id()).intValue()+"");
                    startActivity(intent);
                }else {
                    if(list.get(position).getStat().equals("0.0")){
                        showToast("该评论已被删除");
                        return;
                    }
                    MyAnswerEntity answerEntity = list.get(position);
                    SaveEntity saveEntity = new SaveEntity();
                    saveEntity.setArticle_id(answerEntity.getArticle_id());
                    saveEntity.setTitle(answerEntity.getTitle());
                    saveEntity.setArticle_answer_num(answerEntity.getArticle_answer_num());
                    saveEntity.setArticle_follow_num(answerEntity.getArticle_follow_num());
                    saveEntity.setArticle_content(answerEntity.getArticle_content());
                    saveEntity.setArticle_imgs(answerEntity.getArticle_imgs());
                    saveEntity.setComment_id(answerEntity.getComment_id());
                    saveEntity.setComment_uid_headimg(SPUtils.get(MyAnswerActivity.this, "head", "").toString());
                    saveEntity.setComment_uid_name(SPUtils.get(MyAnswerActivity.this, "name", "").toString());
                    saveEntity.setComment_uid_introduction(SPUtils.get(MyAnswerActivity.this, "message", "").toString());
                    saveEntity.setComment_content(answerEntity.getComment_content());
                    saveEntity.setComment_point_num(answerEntity.getComment_point_num());
                    saveEntity.setComment_collect_num(answerEntity.getComment_collect_num());
                    saveEntity.setPoint_comment_stat(answerEntity.getPoint_comment_stat());
                    saveEntity.setCollect_time(answerEntity.getAdd_time());
                    Intent intent = new Intent(MyAnswerActivity.this, SaveDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("save", saveEntity);
                    intent.putExtra("save", bundle);
                    intent.putExtra("type", "0");
                    startActivity(intent);
                    overridePendingTransition(R.anim.in, R.anim.out);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyAnswerAdapter.CURRENT_PAGE = 1;
        getMySave();
    }

    private void getMySave() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", getIntent().getStringExtra("uid"));
//        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("page", MyAnswerAdapter.CURRENT_PAGE);
//        map.put("ouid", getIntent().getStringExtra("uid"));
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, false, false), map);
        HttpManager.getInstance().getUidComment(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        refresh.setRefreshing(false);
        if (!o.toString().equals("")) {
            JsonObject object = JSONUtils.getAsJsonObject(o);
            JsonArray array = object.get("collect").getAsJsonArray();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls(); //重点
            Gson gson = gsonBuilder.create();
            MyAnswerEntity entity;
            JSONObject object1=null;
            if (MyAnswerAdapter.CURRENT_PAGE == 1) {
                list.clear();
            }
            for (int i = 0; i < array.size(); i++) {
                try {
                    object1=new JSONObject(gson.toJson(array.get(i)));
                    entity = new MyAnswerEntity(object1);
                    if(!entity.getArticle_id().equals("")){
                        if(entity.getStat().equals("1.0")&&!SPUtils.get(MyAnswerActivity.this,"userId","").equals(getIntent().getStringExtra("uid"))){

                        }else{
                            list.add(entity);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            saveAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
