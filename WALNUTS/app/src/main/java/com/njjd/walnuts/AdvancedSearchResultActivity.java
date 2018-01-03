package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.njjd.adapter.SearchUserAdapter;
import com.njjd.domain.SearchUserEntity;
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
 * Created by mrwim on 17/12/25.
 */

public class AdvancedSearchResultActivity extends BaseActivity {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.list_user)
    ListView listUser;
    @BindView(R.id.txt_result)
    TextView txtResult;
    private SearchUserAdapter userAdapter;
    private List<SearchUserEntity> userEntities = new ArrayList<>();
    @Override
    public int bindLayout() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initView(View view) {
        SearchUserAdapter.CURRENTPAGE=1;
        txtTitle.setText("搜索结果");
        listUser.setEmptyView(txtResult);
        userAdapter = new SearchUserAdapter(this, userEntities,true);
        listUser.setAdapter(userAdapter);
        search();
        listUser.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        SearchUserAdapter.CURRENTPAGE++;
                        search();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AdvancedSearchResultActivity.this, PeopleInfoActivity.class);
                intent.putExtra("uid", userEntities.get(position).getUid());
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
    }
    private void search(){
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("keyword", getIntent().getStringExtra("keyword"));
        map.put("product",getIntent().getStringExtra("product"));
        if(!"0".equals(getIntent().getStringExtra("city_id"))&&!"0.0".equals(getIntent().getStringExtra("city_id")))
            map.put("city_id", Float.valueOf(getIntent().getStringExtra("city_id")).intValue());
        if(!"0".equals(getIntent().getStringExtra("industry_id"))&&!"0.0".equals(getIntent().getStringExtra("industry_id")))
            map.put("industry_id", Float.valueOf(getIntent().getStringExtra("industry_id")).intValue());
        map.put("company", getIntent().getStringExtra("company"));
        map.put("position", getIntent().getStringExtra("position"));
        map.put("page", SearchUserAdapter.CURRENTPAGE);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, true), map);
        HttpManager.getInstance().searchUserAdvanced(postEntity);
    }

    @Override
    public void onNext(Object o) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        JSONArray array = null;
        SearchUserEntity entity;
        if (SearchUserAdapter.CURRENTPAGE == 1) {
            userEntities.clear();
        }
        try {
            array = new JSONArray(gson.toJson(o));
            for (int i = 0; i < array.length(); i++) {
                entity = new SearchUserEntity(array.getJSONObject(i));
                userEntities.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userAdapter.notifyDataSetChanged();
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
