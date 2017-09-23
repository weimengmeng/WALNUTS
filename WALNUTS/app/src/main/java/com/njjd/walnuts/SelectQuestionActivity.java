package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njjd.adapter.SelectAnswerAdapter;
import com.njjd.domain.SelectedAnswerEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.SPUtils;

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
 * Created by mrwim on 17/9/22.
 */

public class SelectQuestionActivity extends BaseActivity {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.listview)
    ListView listview;
    private List<SelectedAnswerEntity> list=new ArrayList<>();
    private SelectAnswerAdapter adapter;
    private Intent intent;
    private SelectedAnswerEntity entity;
    @Override
    public int bindLayout() {
        return R.layout.acitivity_selectquestion;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView(this,top);
        txtTitle.setText(getIntent().getStringExtra("title"));
        adapter=new SelectAnswerAdapter(this,list);
        listview.setAdapter(adapter);
        getSelectedAnswerList();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 entity=list.get(position);
                intent=new Intent(SelectQuestionActivity.this, SelectAnswerDetailActivity.class);
                intent.putExtra("questionId",entity.getArticle_id());
                intent.putExtra("questionTitle",entity.getTitle());
                intent.putExtra("answer_id",entity.getAnswer_id());
                startActivity(intent);
            }
        });
    }
    private void getSelectedAnswerList(){
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("cate_article_id",Float.valueOf(getIntent().getStringExtra("id")).intValue());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, false, false), map);
        HttpManager.getInstance().getHotComment(postEntity);
    }

    @Override
    public void onNext(Object o) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        JSONObject object = null;
        JSONArray array = null;
        SelectedAnswerEntity answerEntity;
        try {
            object=new JSONObject(gson.toJson(o));
            array=object.getJSONArray("comment");
            for(int i=0;i<array.length()&&i<5;i++){
                object=array.getJSONObject(i);
                answerEntity=new SelectedAnswerEntity(object);
                list.add(answerEntity);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
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
}
