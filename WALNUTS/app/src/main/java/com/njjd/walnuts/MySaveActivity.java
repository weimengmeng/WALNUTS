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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njjd.adapter.MySaveAdapter;
import com.njjd.domain.SaveEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.LogUtils;
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
 * Created by mrwim on 17/7/13.
 */

public class MySaveActivity extends BaseActivity {
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
    private List<SaveEntity> list = new ArrayList<>();
    private MySaveAdapter saveAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_mysave;
    }

    @Override
    public void initView(View view) {
        back.setText("我的");
        txtTitle.setText("我的收藏");
        listSave.setEmptyView(findViewById(R.id.empty));
        ((TextView)findViewById(R.id.txt_content)).setText("暂无收藏");
        saveAdapter = new MySaveAdapter(list, this);
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
                Intent intent = new Intent(MySaveActivity.this, SaveDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("save", list.get(position));
                intent.putExtra("save", bundle);
                intent.putExtra("type","1");
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
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, false, false), map);
        HttpManager.getInstance().getUidSave(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        refresh.setRefreshing(false);
        if (!o.toString().equals("")) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls(); //重点
            Gson gson = gsonBuilder.create();
            try {
                JSONObject object=new JSONObject(gson.toJson(o));
                JSONArray array=object.getJSONArray("collect");
                SaveEntity entity;
                if (MySaveAdapter.CURRENT_PAGE == 1) {
                    list.clear();
                }
                for (int i = 0; i < array.length(); i++) {
                    entity = new SaveEntity(array.getJSONObject(i));
                    list.add(entity);
                }
                saveAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
