package com.njjd.walnuts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.njjd.adapter.FocusPeopleAdapter;
import com.njjd.adapter.FocusQuesAdapter;
import com.njjd.adapter.FocusTagAdapter;
import com.njjd.domain.FocusEntity;
import com.njjd.domain.QuestionEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.umeng.socialize.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/8/7.
 */

public class MyFocusActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.list_user)
    SwipeMenuListView listUser;
    @BindView(R.id.list_ques)
    SwipeMenuListView listQues;
    @BindView(R.id.list_tag)
    SwipeMenuListView listTag;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;
    private List<FocusEntity> userList = new ArrayList<>();
    private List<QuestionEntity> quesList = new ArrayList<>();
    private List<FocusEntity> tagList = new ArrayList<>();
    private FocusPeopleAdapter peopleAdapter;
    private FocusTagAdapter focusTagAdapter;
    private FocusQuesAdapter quesAdapter;
    private SwipeMenuCreator creator;
    private int currentPage=1;
    @Override
    public int bindLayout() {
        return R.layout.activity_myfocus;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        back.setText("我的");
        txtTitle.setText("我的关注");
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem focusItem = new SwipeMenuItem(
                        MyFocusActivity.this);
                focusItem.setBackground(R.color.login);
                focusItem.setWidth(240);
                focusItem.setTitle("取消关注");
                focusItem.setTitleSize(16);
                focusItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(focusItem);
            }
        };
        peopleAdapter = new FocusPeopleAdapter(userList, this);
        listUser.setEmptyView(findViewById(R.id.empty));
        listUser.setAdapter(peopleAdapter);
        listUser.setMenuCreator(creator);
        listUser.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listUser.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        cancleFocusUser(position);
                        break;
                }
                return false;
            }
        });
        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MyFocusActivity.this,PeopleInfoActivity.class);
                intent.putExtra("uid",userList.get(position).getId());
                startActivity(intent);
            }
        });
        focusTagAdapter = new FocusTagAdapter(tagList, this);
        listTag.setAdapter(focusTagAdapter);
        listTag.setMenuCreator(creator);
        listTag.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listTag.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        cancleFocusTag(position);
                        break;
                }
                return false;
            }
        });
        quesAdapter = new FocusQuesAdapter(quesList, this);
        listQues.setAdapter(quesAdapter);
        listQues.setMenuCreator(creator);
        listQues.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listQues.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        cancleFocusQuestion(position);
                        break;
                }
                return false;
            }
        });
        FocusTagAdapter.CURRENT_PAGE=1;
        FocusPeopleAdapter.CURRENT_PAGE=1;
        FocusQuesAdapter.CURRENT_PAGE=1;
        getFocusUser();
        getFocusQuestion();
        getFocusTag();
        listQues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyFocusActivity.this, IndexDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("question", quesList.get(position));
                intent.putExtra("question", bundle);
                intent.putExtra("type","1");
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (currentPage){
                        case 1:
                            FocusPeopleAdapter.CURRENT_PAGE=1;
                            getFocusUser();
                            break;
                        case 2:
                            FocusQuesAdapter.CURRENT_PAGE=1;
                            getFocusQuestion();
                            break;
                        case 3:
                            FocusTagAdapter.CURRENT_PAGE=1;
                            getFocusTag();
                            break;
                    }
            }
        });
        listQues.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        FocusQuesAdapter.CURRENT_PAGE++;
                            getFocusQuestion();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(listQues != null && listQues.getChildCount() > 0){
                    boolean firstItemVisible = listQues.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listQues.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refreshLayout.setEnabled(enable);
            }});
        listUser.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        FocusPeopleAdapter.CURRENT_PAGE++;
                            getFocusUser();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(listUser != null && listUser.getChildCount() > 0){
                    boolean firstItemVisible = listUser.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listUser.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refreshLayout.setEnabled(enable);
            }});
        listTag.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        FocusTagAdapter.CURRENT_PAGE++;
                            getFocusTag();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(listTag != null && listTag.getChildCount() > 0){
                    boolean firstItemVisible = listTag.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listTag.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refreshLayout.setEnabled(enable);
            }});
        listTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyFocusActivity.this, TagActivity.class);
                intent.putExtra("tag_id", tagList.get(position).getId());
                intent.putExtra("name", tagList.get(position).getName());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.radio_one, R.id.radio_two, R.id.radio_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.radio_one:
                listUser.setVisibility(View.VISIBLE);
                listQues.setVisibility(View.GONE);
                listTag.setVisibility(View.GONE);
                listUser.setEmptyView(findViewById(R.id.empty));
                currentPage=1;
                break;
            case R.id.radio_two:
                listUser.setVisibility(View.GONE);
                listQues.setVisibility(View.VISIBLE);
                listTag.setVisibility(View.GONE);
                listQues.setEmptyView(findViewById(R.id.empty));
                currentPage=2;
                break;
            case R.id.radio_three:
                listUser.setVisibility(View.GONE);
                listQues.setVisibility(View.GONE);
                listTag.setVisibility(View.VISIBLE);
                listTag.setEmptyView(findViewById(R.id.empty));
                currentPage=3;
                break;
        }
    }

    private void getFocusUser() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("page", FocusPeopleAdapter.CURRENT_PAGE);
        map.put("select","1");
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getFocusUserListener, this, false, false), map);
        HttpManager.getInstance().getFollowUser(postEntity);
    }

    HttpOnNextListener getFocusUserListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            refreshLayout.setRefreshing(false);
            if (!o.toString().equals("")) {
                JsonObject jsonObject = JSONUtils.getAsJsonObject(o);
                JsonArray array = JSONUtils.getAsJsonArray(jsonObject.get("user"));
                JSONObject object;
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.serializeNulls(); //重点
                Gson gson = gsonBuilder.create();
                if (array != null) {
                    if(FocusPeopleAdapter.CURRENT_PAGE==1){
                        userList.clear();
                    }
                    for (int i = 0; i < array.size(); i++) {
                        try {
                            object = new JSONObject(gson.toJson(array.get(i)));
                            userList.add(new FocusEntity(object.getString("uid"), object.getString("uname"), object.getString("headimgs"),
                                    object.getString("add_time"), object.isNull("introduction")?"":object.getString("introduction")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    peopleAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void getFocusQuestion() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("page", FocusQuesAdapter.CURRENT_PAGE);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getFocusQuestionListener, this, false, false), map);
        HttpManager.getInstance().getFollowArticle(postEntity);
    }

    HttpOnNextListener getFocusQuestionListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            refreshLayout.setRefreshing(false);
            if (!o.toString().equals("")) {
                JsonObject jsonObject = JSONUtils.getAsJsonObject(o);
                JsonArray array = JSONUtils.getAsJsonArray(jsonObject.get("article"));
                if (array != null) {
                    if(FocusQuesAdapter.CURRENT_PAGE==1) {
                        quesList.clear();
                    }
                    for (int i = 0; i < array.size(); i++) {
                        quesList.add(new QuestionEntity(array.get(i).getAsJsonObject()));
                    }
                    quesAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void getFocusTag() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("page", FocusTagAdapter.CURRENT_PAGE);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getFocusTagListener, this, false, false), map);
        HttpManager.getInstance().getFollowLabel(postEntity);
    }

    HttpOnNextListener getFocusTagListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            refreshLayout.setRefreshing(false);
            if (!o.toString().equals("")) {
                JsonObject jsonObject = JSONUtils.getAsJsonObject(o);
                JsonArray array = JSONUtils.getAsJsonArray(jsonObject.get("label"));
                if (array != null) {
                    if(FocusTagAdapter.CURRENT_PAGE==1) {
                        tagList.clear();
                    }
                    JsonObject object;
                    for (int i = 0; i < array.size(); i++) {
                        object = array.get(i).getAsJsonObject();
                        tagList.add(new FocusEntity(object.get("label_id").getAsString(), object.get("label_name").getAsString(), "", "", object.get("add_time").getAsString()));
                    }
                    focusTagAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void cancleFocusUser(final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("select", "0");
        map.put("be_uid", userList.get(position).getId());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                userList.remove(position);
                peopleAdapter.notifyDataSetChanged();
            }
        }, this, false, false), map);
        HttpManager.getInstance().followUser(postEntity);

    }
    private void cancleFocusQuestion(final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("select", "0");
        map.put("article_id", Float.valueOf(quesList.get(position).getQuestionId()).intValue());
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                quesList.remove(position);
                quesAdapter.notifyDataSetChanged();
            }
        }, this, false, false), map);
        HttpManager.getInstance().focusQuestion(postEntity);
    }
    private void cancleFocusTag(final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("select", "0");
        map.put("label_id", Float.valueOf(tagList.get(position).getId()).intValue());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                tagList.remove(position);
                focusTagAdapter.notifyDataSetChanged();
            }
        }, this, false, false), map);
        HttpManager.getInstance().focusLabel(postEntity);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
