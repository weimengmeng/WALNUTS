package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.njjd.adapter.HistoryAdapter;
import com.njjd.adapter.SearchArticleAdapter;
import com.njjd.adapter.SearchQuesAdapter;
import com.njjd.adapter.SearchUserAdapter;
import com.njjd.dao.SearchHistoryDao;
import com.njjd.db.DBHelper;
import com.njjd.domain.CommonEntity;
import com.njjd.domain.SearchArticleEntity;
import com.njjd.domain.SearchHistory;
import com.njjd.domain.SearchQuesEntity;
import com.njjd.domain.SearchUserEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.IconCenterEditText;
import com.njjd.utils.KeybordS;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;

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
    @BindView(R.id.txt_province)
    TextView txtProvince;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.lv_search)
    LinearLayout lvSearch;
    private List<SearchHistory> historyList, templist;
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
    //省份一级菜单
    private List<String> provinces;
    private List<CommonEntity> provinceEntities;
    //城市二级菜单
    private List<List<CommonEntity>> cityList;
    private List<List<String>> cityEntities;
    //行业一级菜单
    private List<String> industrys1;
    private List<CommonEntity> industryList1;
    //行业二级菜单
    private List<List<String>> industrys2;
    private List<List<CommonEntity>> industryList2;
    private OptionsPickerView<String> provincePickview, industryPickview;
    private String cityId = "0", industryId = "0";

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
                    SearchUserAdapter.CURRENTPAGE = 1;
                    SearchArticleAdapter.CURRENTPAGE = 1;
                    SearchQuesAdapter.CURRENTPAGE = 1;
                    quesEntities.clear();
                    userEntities.clear();
                    articleEntities.clear();
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
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                HttpManager.getInstance().cancle();
//                switch (searchItem) {
//                    case 1:
//                        SearchQuesAdapter.CURRENTPAGE = 1;
//                        break;
//                    case 2:
//                        SearchArticleAdapter.CURRENTPAGE = 1;
//                        break;
//                    case 3:
//                        SearchUserAdapter.CURRENTPAGE = 1;
//                        break;
//                    case 4:
////                        listLabel.setVisibility(View.GONE);
//                        break;
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (etSearch.getText().toString().equals("")) {
//                    if(historyList.size()==0){
//                        findViewById(R.id.lv_history).setVisibility(View.GONE);
//                    }else{
//                        findViewById(R.id.lv_history).setVisibility(View.VISIBLE);
//                        listHistory.setVisibility(View.VISIBLE);
//                    }
//                    switch (searchItem) {
//                        case 1:
//                            listQues.setVisibility(View.GONE);
//                            break;
//                        case 2:
//                            listArticle.setVisibility(View.GONE);
//                            break;
//                        case 3:
//                            findViewById(R.id.lv_history).setVisibility(View.GONE);
//                            listUser.setVisibility(View.VISIBLE);
//                            break;
//                        case 4:
//                            listLabel.setVisibility(View.GONE);
//                            break;
//                    }
//                    return;
//                }
//                //做搜索操作
//                etSearch.setSelection(etSearch.getText().toString().length());
//                switch (searchItem) {
//                    case 1:
//                        quesEntities.clear();
//                        quesAdapter.notifyDataSetChanged();
//                        break;
//                    case 2:
//                        articleEntities.clear();
//                        articleAdapter.notifyDataSetChanged();
//                        break;
//                    case 3:
//                        userEntities.clear();
//                        userAdapter.notifyDataSetChanged();
//                        break;
//                    case 4:
//                        listLabel.setVisibility(View.GONE);
//                        break;
//                }
//                searchByKeyWords();
//            }
//        });
        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etSearch.setText(historyList.get(position).getKeywords());
                SearchUserAdapter.CURRENTPAGE = 1;
                SearchArticleAdapter.CURRENTPAGE = 1;
                SearchQuesAdapter.CURRENTPAGE = 1;
                KeybordS.closeBoard(SearchActivity.this);
                searchByKeyWords();
                etSearch.setSelection(etSearch.getText().toString().length());
            }
        });
        quesAdapter = new SearchQuesAdapter(this, quesEntities);
        listQues.setAdapter(quesAdapter);
        articleAdapter = new SearchArticleAdapter(this, articleEntities);
        listArticle.setAdapter(articleAdapter);
        userAdapter = new SearchUserAdapter(this, userEntities,false);
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
                intent.putExtra("id", Float.valueOf(quesEntities.get(position).getArticleId()).intValue() + "");
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
        provinces = CommonUtils.getInstance().getProvincesList();
        provinces.add(0, "区域不限");
        provinceEntities = CommonUtils.getInstance().getProvinceEntities();
        provinceEntities.add(0, new CommonEntity("0", "区域不限", "000"));
        cityEntities = CommonUtils.getInstance().getCityEntities();
        List<List<String>> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        b.add("区域不限");
        a.add(b);
        cityEntities.add(0, b);
        cityList = CommonUtils.getInstance().getCityList();
        List<CommonEntity> t = new ArrayList<>();
        t.add(new CommonEntity("0", "区域不限", "000"));
        cityList.add(0, t);
        industrys1 = CommonUtils.getInstance().getIndustrys1();
        industrys1.add(0, "行业不限");
        industrys2 = CommonUtils.getInstance().getIndustrys2();
        List<String> d = new ArrayList<>();
        d.add("行业不限");
        industrys2.add(0, d);
        industryList1 = CommonUtils.getInstance().getIndustryList1();
        industryList1.add(new CommonEntity("0", "行业不限", "000"));
        industryList2 = CommonUtils.getInstance().getIndustryList2();
        List<CommonEntity> c = new ArrayList<>();
        c.add(new CommonEntity("0", "行业不限", "000"));
        industryList2.add(0, c);
        provincePickview = new OptionsPickerView.Builder(SearchActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                cityId = Float.valueOf(cityList.get(options1).get(options2).getId()).intValue() + "";
                txtProvince.setText(cityEntities.get(options1).get(options2));
                userEntities.clear();
                userAdapter.notifyDataSetChanged();
                SearchUserAdapter.CURRENTPAGE = 1;
                searchByKeyWords();
            }
        }).build();
        provincePickview.setPicker(provinces, cityEntities);
        industryPickview = new OptionsPickerView.Builder(SearchActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                txtVocation.setText(industrys2.get(options1).get(options2));
                industryId = Float.valueOf(industryList2.get(options1).get(options2).getId()).intValue() + "";
                userEntities.clear();
                SearchUserAdapter.CURRENTPAGE = 1;
                userAdapter.notifyDataSetChanged();
                searchByKeyWords();
            }
        }).build();
        industryPickview.setPicker(industrys1, industrys2);
    }

    private void searchByKeyWords() {
        if (etSearch.getText().toString().equals("")) {
            if (searchItem != 3) {
                return;
            }

        } else {
            updateHistory();
        }
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
                lvSearch.setVisibility(View.VISIBLE);
                listUser.setVisibility(View.VISIBLE);
                if (!cityId.equals("0")) {
                    map.put("city_id", Float.valueOf(cityId).intValue());
                }
                if (!industryId.equals("0")) {
                    map.put("industry_id", Float.valueOf(industryId).intValue());
                }
                map.put("page", SearchUserAdapter.CURRENTPAGE++);
                postEntity = new SubjectPost(new ProgressSubscriber(searchUser, this, true, true), map);
                HttpManager.getInstance().searchUser(postEntity);
                break;
            case 4:
                listLabel.setVisibility(View.VISIBLE);
//                map.put("page",etSearch.getText().toString());
                //搜话题
//                ToastUtils.showShortToast(this, "搜话题");
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
            if (SearchQuesAdapter.CURRENTPAGE == 1) {
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
            if (SearchArticleAdapter.CURRENTPAGE == 1) {
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

    @OnClick({R.id.back, R.id.radio1, R.id.radio2, R.id.radio3, R.id.radio4, R.id.delete_history, R.id.txt_province, R.id.txt_vocation, R.id.txt_advanced})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                KeybordS.closeBoard(this);
                finish();
                break;
            case R.id.txt_province:
                if (provincePickview != null) {
                    provincePickview.show();
                }
                KeybordS.closeBoard(this);
                break;
            case R.id.txt_advanced:
                startActivity(new Intent(this, AdvancedSearchActivity.class));
                KeybordS.closeBoard(this);
                break;
            case R.id.txt_vocation:
                if (industryPickview != null) {
                    industryPickview.show();
                }
                industryPickview.show();
                KeybordS.closeBoard(this);
                break;
            case R.id.radio1:
                searchItem = 1;
                if (listHistory.getVisibility() == View.VISIBLE) {
                    lvSearch.setVisibility(View.GONE);
                    return;
                }
                SearchQuesAdapter.CURRENTPAGE = 1;
                quesEntities.clear();
                listQues.setVisibility(View.VISIBLE);
                listArticle.setVisibility(View.GONE);
                listUser.setVisibility(View.GONE);
                lvSearch.setVisibility(View.GONE);
                listLabel.setVisibility(View.GONE);
                searchByKeyWords();
                break;
            case R.id.radio2:
                searchItem = 2;
                if (listHistory.getVisibility() == View.VISIBLE) {
                    lvSearch.setVisibility(View.GONE);
                    return;
                }
                SearchArticleAdapter.CURRENTPAGE = 1;
                articleEntities.clear();
                listQues.setVisibility(View.GONE);
                listArticle.setVisibility(View.VISIBLE);
                listUser.setVisibility(View.GONE);
                lvSearch.setVisibility(View.GONE);
                listLabel.setVisibility(View.GONE);
                searchByKeyWords();
                break;
            case R.id.radio3:
                searchItem = 3;
                userEntities.clear();
                SearchUserAdapter.CURRENTPAGE = 1;
                listQues.setVisibility(View.GONE);
                listArticle.setVisibility(View.GONE);
                lvSearch.setVisibility(View.VISIBLE);
                listUser.setVisibility(View.VISIBLE);
                listLabel.setVisibility(View.GONE);
                if(etSearch.getText().toString().equals("")){
                    return;
                }
                searchByKeyWords();
                break;
            case R.id.radio4:
                searchItem = 4;
                if (listHistory.getVisibility() == View.VISIBLE) {
                    lvSearch.setVisibility(View.GONE);
                    return;
                }
                listQues.setVisibility(View.GONE);
                listArticle.setVisibility(View.GONE);
                listUser.setVisibility(View.GONE);
                lvSearch.setVisibility(View.GONE);
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
        provinces.remove(0);
        cityEntities.remove(0);
        cityList.remove(0);
        provinceEntities.remove(0);
        industrys1.remove(0);
        industryList1.remove(0);
        industrys2.remove(0);
        industryList2.remove(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        KeybordS.closeBoard(this);
    }
}
