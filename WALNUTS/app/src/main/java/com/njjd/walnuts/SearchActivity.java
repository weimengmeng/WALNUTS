package com.njjd.walnuts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.adapter.HistoryAdapter;
import com.njjd.dao.SearchHistoryDao;
import com.njjd.db.DBHelper;
import com.njjd.domain.SearchHistory;
import com.njjd.http.HttpManager;
import com.njjd.utils.IconCenterEditText;
import com.njjd.utils.KeybordS;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import org.greenrobot.greendao.query.Query;

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
    private List<SearchHistory> historyList;
    private HistoryAdapter adapter;
    private SearchHistory history;
    private int searchItem = 1;
    SubjectPost postEntity;
    private boolean isNew = true;

    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView(View view) {
        historyList = DBHelper.getInstance().getmDaoSession().getSearchHistoryDao().queryBuilder().orderDesc(SearchHistoryDao.Properties.Date).build().list();
        adapter = new HistoryAdapter(this, historyList);
        listHistory.setAdapter(adapter);
        KeybordS.openKeybord(etSearch, this);
        etSearch.setOnSearchClickListener(new IconCenterEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                etSearch.onFocusChange(etSearch, false);
                if (!"".equals(etSearch.getText().toString())) {
                    searchByKeyWords();
                    updateHistory();
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
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSearch.getText().toString().equals("")) {
                    findViewById(R.id.lv_history).setVisibility(View.VISIBLE);
                    listHistory.setVisibility(View.VISIBLE);
                    historyList = DBHelper.getInstance().getmDaoSession().getSearchHistoryDao().queryBuilder().orderDesc(SearchHistoryDao.Properties.Date).build().list();
                    adapter.notifyDataSetChanged();
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
                searchByKeyWords();
            }
        });
        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etSearch.setText(historyList.get(position).getKeywords());
                searchByKeyWords();
            }
        });
    }

    private void searchByKeyWords() {
        findViewById(R.id.lv_history).setVisibility(View.GONE);
        listHistory.setVisibility(View.GONE);
        switch (searchItem) {
            case 1:
                listQues.setVisibility(View.VISIBLE);
                break;
            case 2:
                listArticle.setVisibility(View.VISIBLE);
                break;
            case 3:
                listUser.setVisibility(View.VISIBLE);
                break;
            case 4:
                listLabel.setVisibility(View.VISIBLE);
                break;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("keyword", etSearch.getText().toString());
        switch (searchItem) {
            case 1:
                //搜问题
                ToastUtils.showShortToast(this, "搜问题");
//                postEntity= new SubjectPost(new ProgressSubscriber(searchQuestion, this, true, false), map);
//                HttpManager.getInstance().phoneCode(postEntity);
                break;
            case 2:
                //搜文章
                ToastUtils.showShortToast(this, "搜文章");
//                postEntity= new SubjectPost(new ProgressSubscriber(searchArticle, this, true, false), map);
//                HttpManager.getInstance().phoneCode(postEntity);
                break;
            case 3:
                //搜用户
                ToastUtils.showShortToast(this, "搜用户");
//                postEntity= new SubjectPost(new ProgressSubscriber(searchUser, this, true, false), map);
//                HttpManager.getInstance().phoneCode(postEntity);
                break;
            case 4:
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

        }
    };
    HttpOnNextListener searchArticle = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {

        }
    };
    HttpOnNextListener searchUser = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {

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
    }
    private void updateHistory(){
        isNew = true;
        historyList = DBHelper.getInstance().getmDaoSession().getSearchHistoryDao().queryBuilder().orderAsc(SearchHistoryDao.Properties.Id).build().list();
        for (int i = 0; i < historyList.size(); i++) {
            if (historyList.get(i).getKeywords().equals(etSearch.getText().toString())) {
                history = historyList.get(i);
                history.setDate(new Date());
                DBHelper.getInstance().getmDaoSession().getSearchHistoryDao().update(history);
                isNew = false;
                break;
            }
        }
        if (isNew) {
            if (historyList.size() > 0) {
                history = new SearchHistory(historyList.get(historyList.size() - 1).getId() + 1, etSearch.getText().toString(), new Date());
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
                if (!"".equals(etSearch.getText().toString())) {
                   updateHistory();
                }
                finish();
                break;
            case R.id.radio1:
                searchItem = 1;
                if (listHistory.getVisibility() == View.VISIBLE) {
                    return;
                }
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
