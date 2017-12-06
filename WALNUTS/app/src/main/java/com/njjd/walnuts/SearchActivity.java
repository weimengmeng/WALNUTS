package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.njjd.adapter.HistoryAdapter;
import com.njjd.adapter.SearchArticleAdapter;
import com.njjd.adapter.SearchQuesAdapter;
import com.njjd.adapter.SearchUserAdapter;
import com.njjd.dao.SearchHistoryDao;
import com.njjd.db.DBHelper;
import com.njjd.domain.SearchArticleEntity;
import com.njjd.domain.SearchHistory;
import com.njjd.domain.SearchQuesEntity;
import com.njjd.domain.SearchUserEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.IconCenterEditText;
import com.njjd.utils.KeybordS;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/8/18.
 */

public class SearchActivity extends BaseActivity {
    @BindView(R.id.et_search)
    IconCenterEditText etSearch;
    @BindView(R.id.list_ques)
    ListView listQues;
    @BindView(R.id.list_article)
    ListView listArticle;
    @BindView(R.id.list_user)
    ListView listUser;
    @BindView(R.id.list_label)
    ListView listLabel;
    @BindView(R.id.list_history)
    ListView listHistory;
    private List<SearchHistory> historyList,templist;
    private HistoryAdapter adapter;
    private SearchHistory history;
    private int searchItem = 1;
    SubjectPost postEntity;
    private boolean isNew = true;
    private SearchQuesAdapter quesAdapter;
    private SearchArticleAdapter articleAdapter;
    private SearchUserAdapter userAdapter;
    private List<SearchQuesEntity> quesEntities = new ArrayList<>();
    private List<SearchArticleEntity> articleEntities = new ArrayList<>();
    private List<SearchUserEntity> userEntities = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView(View view) {
        historyList = DBHelper.getInstance().getmDaoSession().getSearchHistoryDao().queryBuilder().orderDesc(SearchHistoryDao.Properties.Date).build().list();
        adapter = new HistoryAdapter(this, historyList);
        listHistory.setAdapter(adapter);
        if (historyList.size() == 0) {
            findViewById(R.id.lv_history).setVisibility(View.GONE);
        }
        KeybordS.openKeybord(etSearch, this);
        etSearch.setOnSearchClickListener(new IconCenterEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                etSearch.onFocusChange(etSearch, false);
                if (!"".equals(etSearch.getText().toString())) {
                    searchByKeyWords();
                } else {
                    ToastUtils.showShortToast(SearchActivity.this, "请输入搜索内容");
                }
            }
        });
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etSearch.onFocusChange(etSearch, hasFocus);
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HttpManager.getInstance().cancle();
                switch (searchItem) {
                    case 1:
                        SearchQuesAdapter.CURRENTPAGE = 1;
                        break;
                    case 2:
                        SearchArticleAdapter.CURRENTPAGE = 1;
                        break;
                    case 3:
                        SearchUserAdapter.CURRENTPAGE = 1;
                        break;
                    case 4:
//                        listLabel.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSearch.getText().toString().equals("")) {
                    findViewById(R.id.lv_history).setVisibility(View.VISIBLE);
                    listHistory.setVisibility(View.VISIBLE);
                    switch (searchItem) {
                        case 1:
                            listQues.setVisibility(View.GONE);
                            break;
                        case 2:
                            listArticle.setVisibility(View.GONE);
                            break;
                        case 3:
                            listUser.setVisibility(View.GONE);
                            break;
                        case 4:
                            listLabel.setVisibility(View.GONE);
                            break;
                    }
                    return;
                }
                //做搜索操作
                etSearch.setSelection(etSearch.getText().toString().length());
                switch (searchItem) {
                    case 1:
                        quesEntities.clear();
                        quesAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        articleEntities.clear();
                        articleAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        userEntities.clear();
                        userAdapter.notifyDataSetChanged();
                        break;
                    case 4:
                        listLabel.setVisibility(View.GONE);
                        break;
                }
                searchByKeyWords();
            }
        });
        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etSearch.setText(historyList.get(position).getKeywords());
                SearchUserAdapter.CURRENTPAGE=1;
                SearchArticleAdapter.CURRENTPAGE=1;
                SearchQuesAdapter.CURRENTPAGE=1;
                KeybordS.closeBoard(SearchActivity.this);
            }
        });
        quesAdapter = new SearchQuesAdapter(this, quesEntities);
        listQues.setAdapter(quesAdapter);
        articleAdapter = new SearchArticleAdapter(this, articleEntities);
        listArticle.setAdapter(articleAdapter);
        userAdapter = new SearchUserAdapter(this, userEntities);
        listUser.setAdapter(userAdapter);
        listQues.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        SearchQuesAdapter.CURRENTPAGE++;
                        searchByKeyWords();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        listUser.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        SearchArticleAdapter.CURRENTPAGE++;
                        searchByKeyWords();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        listArticle.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        SearchUserAdapter.CURRENTPAGE++;
                        searchByKeyWords();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        listArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ColumnDetailActivity.class);
                intent.putExtra("article_id", Float.valueOf(articleEntities.get(position).getArticleId()).intValue() + "");
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
        listQues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, SearchQuesDetailActivity.class);
                intent.putExtra("id",Float.valueOf(quesEntities.get(position).getArticleId()).intValue()+"");
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, PeopleInfoActivity.class);
                intent.putExtra("uid", userEntities.get(position).getUid());
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
    }

    private void searchByKeyWords() {
        updateHistory();
        findViewById(R.id.lv_history).setVisibility(View.GONE);
        listHistory.setVisibility(View.GONE);
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("keyword", etSearch.getText().toString());
        switch (searchItem) {
            case 1:
                //搜问题
                quesAdapter.setText(etSearch.getText().toString());
                listQues.setVisibility(View.VISIBLE);
                map.put("page", SearchQuesAdapter.CURRENTPAGE++);
                postEntity = new SubjectPost(new ProgressSubscriber(searchQuestion, this, true, true), map);
                HttpManager.getInstance().searchQuest(postEntity);
                break;
            case 2:
                //搜文章
                articleAdapter.setText(etSearch.getText().toString());
                listArticle.setVisibility(View.VISIBLE);
                map.put("page", SearchArticleAdapter.CURRENTPAGE++);
                postEntity = new SubjectPost(new ProgressSubscriber(searchArticle, this, true, true), map);
                HttpManager.getInstance().searchArticle(postEntity);
                break;
            case 3:
                //搜用户
                userAdapter.setText(etSearch.getText().toString());
                listUser.setVisibility(View.VISIBLE);
                map.put("page", SearchUserAdapter.CURRENTPAGE++);
                postEntity = new SubjectPost(new ProgressSubscriber(searchUser, this, true, true), map);
                HttpManager.getInstance().searchUser(postEntity);
                break;
            case 4:
                listLabel.setVisibility(View.VISIBLE);
//                map.put("page",etSearch.getText().toString());
                //搜话题
                ToastUtils.showShortToast(this, "搜话题");
//                postEntity= new SubjectPost(new ProgressSubscriber(searchLabel, this, true, false), map);
//                HttpManager.getInstance().phoneCode(postEntity);
                break;
        }
    }

    HttpOnNextListener searchQuestion = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonArray array = JSONUtils.getAsJsonArray(o);
            SearchQuesEntity entity;
            if(SearchQuesAdapter.CURRENTPAGE==1){
                quesEntities.clear();
            }
            for (int i = 0; i < array.size(); i++) {
                entity = new SearchQuesEntity(array.get(i).getAsJsonObject());
                quesEntities.add(entity);
            }
            quesAdapter.notifyDataSetChanged();
        }
    };
    HttpOnNextListener searchArticle = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonArray array = JSONUtils.getAsJsonArray(o);
            SearchArticleEntity entity;
            if(SearchArticleAdapter.CURRENTPAGE==1){
                articleEntities.clear();
            }
            for (int i = 0; i < array.size(); i++) {
                entity = new SearchArticleEntity(array.get(i).getAsJsonObject());
                articleEntities.add(entity);
            }
            articleAdapter.notifyDataSetChanged();
        }
    };
    HttpOnNextListener searchUser = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonArray array = JSONUtils.getAsJsonArray(o);
            SearchUserEntity entity;
            if(SearchUserAdapter.CURRENTPAGE==1){
                userEntities.clear();
            }
            for (int i = 0; i < array.size(); i++) {
                entity = new SearchUserEntity(array.get(i).getAsJsonObject());
                userEntities.add(entity);
            }
            userAdapter.notifyDataSetChanged();
        }
    };
    HttpOnNextListener searchLabel = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchQuesAdapter.CURRENTPAGE = 1;
        SearchArticleAdapter.CURRENTPAGE = 1;
        SearchUserAdapter.CURRENTPAGE = 1;
    }

    private void updateHistory() {
        isNew = true;
        templist = DBHelper.getInstance().getmDaoSession().getSearchHistoryDao().queryBuilder().orderAsc(SearchHistoryDao.Properties.Id).build().list();
        for (int i = 0; i < templist.size(); i++) {
            if (templist.get(i).getKeywords().equals(etSearch.getText().toString())) {
                history = templist.get(i);
                history.setDate(new Date());
                DBHelper.getInstance().getmDaoSession().getSearchHistoryDao().update(history);
                isNew = false;
                break;
            }
        }
        if (isNew) {
            if (templist.size() > 0) {
                history = new SearchHistory(templist.get(templist.size() - 1).getId() + 1, etSearch.getText().toString(), new Date());
            } else {
                history = new SearchHistory(1, etSearch.getText().toString(), new Date());
            }
            DBHelper.getInstance().getmDaoSession().getSearchHistoryDao().insert(history);
        }
    }

    @OnClick({R.id.back, R.id.radio1, R.id.radio2, R.id.radio3, R.id.radio4, R.id.delete_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                KeybordS.closeBoard(this);
                finish();
                break;
            case R.id.radio1:
                searchItem = 1;
                if (listHistory.getVisibility() == View.VISIBLE) {
                    return;
                }
                SearchQuesAdapter.CURRENTPAGE = 1;
                quesEntities.clear();
                listQues.setVisibility(View.VISIBLE);
                listArticle.setVisibility(View.GONE);
                listUser.setVisibility(View.GONE);
                listLabel.setVisibility(View.GONE);
                searchByKeyWords();
                break;
            case R.id.radio2:
                searchItem = 2;
                if (listHistory.getVisibility() == View.VISIBLE) {
                    return;
                }
                SearchArticleAdapter.CURRENTPAGE = 1;
                articleEntities.clear();
                listQues.setVisibility(View.GONE);
                listArticle.setVisibility(View.VISIBLE);
                listUser.setVisibility(View.GONE);
                listLabel.setVisibility(View.GONE);
                searchByKeyWords();
                break;
            case R.id.radio3:
                searchItem = 3;
                if (listHistory.getVisibility() == View.VISIBLE) {
                    return;
                }
                userEntities.clear();
                SearchUserAdapter.CURRENTPAGE = 1;
                listQues.setVisibility(View.GONE);
                listArticle.setVisibility(View.GONE);
                listUser.setVisibility(View.VISIBLE);
                listLabel.setVisibility(View.GONE);
                searchByKeyWords();
                break;
            case R.id.radio4:
                searchItem = 4;
                if (listHistory.getVisibility() == View.VISIBLE) {
                    return;
                }
                listQues.setVisibility(View.GONE);
                listArticle.setVisibility(View.GONE);
                listUser.setVisibility(View.GONE);
                listLabel.setVisibility(View.VISIBLE);
                searchByKeyWords();
                break;
            case R.id.delete_history:
                DBHelper.getInstance().getmDaoSession().getSearchHistoryDao().deleteAll();
                historyList.clear();
                adapter.notifyDataSetChanged();
                findViewById(R.id.lv_history).setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeybordS.closeBoard(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
