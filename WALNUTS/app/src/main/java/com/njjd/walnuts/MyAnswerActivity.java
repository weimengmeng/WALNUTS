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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.njjd.adapter.MyAnswerAdapter;
import com.njjd.adapter.MySaveAdapter;
import com.njjd.domain.MyAnswerEntity;
import com.njjd.domain.SaveEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.SPUtils;

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
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        back.setText("我的");
        txtTitle.setText("我的回答");
        saveAdapter = new MyAnswerAdapter(list, this);
        listSave.setAdapter(saveAdapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MySaveAdapter.CURRENT_PAGE = 1;
                getMySave();
            }
        });
        listSave.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        MySaveAdapter.CURRENT_PAGE++;
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
                MyAnswerEntity answerEntity=list.get(position);
                SaveEntity saveEntity=new SaveEntity();
                saveEntity.setArticle_id(answerEntity.getArticle_id());
                saveEntity.setTitle(answerEntity.getTitle());
                saveEntity.setArticle_answer_num(answerEntity.getArticle_answer_num());
                saveEntity.setArticle_follow_num(answerEntity.getArticle_follow_num());
                saveEntity.setArticle_content(answerEntity.getArticle_content());
                saveEntity.setArticle_imgs(answerEntity.getArticle_imgs());
                saveEntity.setComment_id(answerEntity.getComment_id());
                saveEntity.setComment_uid_headimg(SPUtils.get(MyAnswerActivity.this,"head","").toString());
                saveEntity.setComment_uid_name(SPUtils.get(MyAnswerActivity.this,"name","").toString());
                saveEntity.setComment_uid_introduction(SPUtils.get(MyAnswerActivity.this,"message","").toString());
                saveEntity.setComment_content(answerEntity.getComment_content());
                saveEntity.setComment_point_num(answerEntity.getComment_point_num());
                saveEntity.setComment_collect_num(answerEntity.getComment_collect_num());
                saveEntity.setPoint_comment_stat(answerEntity.getPoint_comment_stat());
                saveEntity.setCollect_time(answerEntity.getAdd_time());
                Intent intent = new Intent(MyAnswerActivity.this, SaveDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("save", saveEntity);
                intent.putExtra("save", bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySaveAdapter.CURRENT_PAGE = 1;
        getMySave();
    }

    private void getMySave() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("page", MySaveAdapter.CURRENT_PAGE);
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
            MyAnswerEntity entity;
            if (MySaveAdapter.CURRENT_PAGE == 1) {
                list.clear();
            }
            for (int i = 0; i < array.size(); i++) {
                entity = new MyAnswerEntity(array.get(i).getAsJsonObject());
                list.add(entity);
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
