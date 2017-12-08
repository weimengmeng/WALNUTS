package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ios.dialog.AlertDialog;
import com.njjd.adapter.MyQuestionAdapter;
import com.njjd.domain.QuestionEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/13.
 */

public class MyQuestionActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.list_ques)
    SwipeMenuListView listQues;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.img_nodata)
    ImageView imgNodata;
    private MyQuestionAdapter questionAdapter;
    private List<QuestionEntity> list = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_myquestion;
    }

    @Override
    public void initView(View view) {
        back.setText("返回");
        if(getIntent().getStringExtra("uid").equals(SPUtils.get(this,"userId","").toString())) {
            txtTitle.setText("我的创作");
        }else{
            if(getIntent().getStringExtra("type").equals("1")){
                txtTitle.setText("TA的问题");
            }else {
                txtTitle.setText("TA的文章");
            }
        }
        questionAdapter = new MyQuestionAdapter(list, this);
        listQues.setEmptyView(findViewById(R.id.empty));
        imgNodata.setImageResource(R.drawable.btn_pub_article);
        if(SPUtils.get(this,"userId","").equals(getIntent().getStringExtra("uid"))) {
            ((TextView) findViewById(R.id.txt_content)).setText("点击提问");
            findViewById(R.id.empty).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MyQuestionActivity.this,AskActivity.class);
                    intent.putExtra("type","1");
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            if(getIntent().getStringExtra("type").equals("1")){
                ((TextView) findViewById(R.id.txt_content)).setText("TA还没有提问");
            }else {
                ((TextView) findViewById(R.id.txt_content)).setText("TA还没有发表文章");
            }
        }
        listQues.setAdapter(questionAdapter);
        MyQuestionAdapter.CURRENT_PAGE = 1;
        getMyQuestion();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyQuestionAdapter.CURRENT_PAGE = 1;
                getMyQuestion();
            }
        });
        listQues.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        MyQuestionAdapter.CURRENT_PAGE++;
                        getMyQuestion();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (listQues != null && listQues.getChildCount() > 0) {
                    boolean firstItemVisible = listQues.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listQues.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refresh.setEnabled(enable);
            }
        });
        listQues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list.get(position).getIsVisiable().equals("1.0")){
                    showToast("该问题已被删除");
                    return;
                }
                Intent intent;
                if(list.get(position).getType().equals("2.0")){
                    intent = new Intent(MyQuestionActivity.this, ColumnDetailActivity.class);
                    intent.putExtra("article_id", Float.valueOf(list.get(position).getQuestionId()).intValue() + "");
                    startActivity(intent);
                    overridePendingTransition(R.anim.in, R.anim.out);
                    return;
                }
                intent = new Intent(MyQuestionActivity.this, IndexDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("question", list.get(position));
                intent.putExtra("question", bundle);
                intent.putExtra("type", "1");
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
        listQues.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog(MyQuestionActivity.this).builder()
                        .setTitle("删除提醒").setMsg("确定删除此话题吗？")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).setPositiveButton("删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        questionAdapter.notifyDataSetChanged();
                    }
                }).show();
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    private void getMyQuestion() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", getIntent().getStringExtra("uid"));
        map.put("page", MyQuestionAdapter.CURRENT_PAGE);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, false, false), map);
        HttpManager.getInstance().getUidArticle(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        refresh.setRefreshing(false);
        if (!o.toString().equals("")) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls(); //重点
            Gson gson = gsonBuilder.create();
            JSONArray array = null;
            QuestionEntity entity;
            JsonObject jsonObject = JSONUtils.getAsJsonObject(o);
            try {
                array = new JSONArray(gson.toJson(jsonObject.get("article")));
                if (MyQuestionAdapter.CURRENT_PAGE == 1) {
                    list.clear();
                }
                for (int i = 0; i < array.length(); i++) {
                    entity = new QuestionEntity(array.getJSONObject(i), "");
                    if(array.getJSONObject(i).getString("invisible").equals("1.0")&&!SPUtils.get(MyQuestionActivity.this,"userId","").equals(getIntent().getStringExtra("uid"))){

                    }else{
                        if(SPUtils.get(MyQuestionActivity.this,"userId","").equals(getIntent().getStringExtra("uid"))) {
                            entity.setIsVisiable(array.getJSONObject(i).getString("invisible"));
                            list.add(entity);
                        }else{
                            //别人的问题
                            if(getIntent().getStringExtra("type").equals("1")&&array.getJSONObject(i).getString("type").equals("1.0")){
                                entity.setIsVisiable(array.getJSONObject(i).getString("invisible"));
                                list.add(entity);
                            }else if(getIntent().getStringExtra("type").equals("2")&&array.getJSONObject(i).getString("type").equals("2.0")){
                                //别人的文章
                                entity.setIsVisiable(array.getJSONObject(i).getString("invisible"));
                                list.add(entity);
                            }
                        }
                    }
                }
                questionAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                LogUtils.d(e.toString());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
