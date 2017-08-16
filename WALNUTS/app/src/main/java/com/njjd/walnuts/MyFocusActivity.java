package com.njjd.walnuts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.njjd.adapter.FocusPeopleAdapter;
import com.njjd.adapter.FocusQuesAdapter;
import com.njjd.adapter.FocusTagAdapter;
import com.njjd.domain.FocusEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

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
    private List<FocusEntity> userList=new ArrayList<>();
    private List<FocusEntity> quesList=new ArrayList<>();
    private List<FocusEntity> tagList=new ArrayList<>();
    private FocusPeopleAdapter peopleAdapter;
    private FocusTagAdapter focusTagAdapter;
    private FocusQuesAdapter quesAdapter;
    private SwipeMenuCreator creator;
    @Override
    public int bindLayout() {
        return R.layout.activity_myfocus;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView(this,topView);
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
        peopleAdapter=new FocusPeopleAdapter(userList,this);
        listUser.setAdapter(peopleAdapter);
        listUser.setMenuCreator(creator);
        listUser.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listUser.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ToastUtils.showShortToast( MyFocusActivity.this, "取消");
                        break;
                }
                return false;
            }
        });
        focusTagAdapter=new FocusTagAdapter(tagList,this);
        listTag.setAdapter(focusTagAdapter);
        listTag.setMenuCreator(creator);
        listTag.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listTag.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ToastUtils.showShortToast( MyFocusActivity.this, "取消");
                        break;
                }
                return false;
            }
        });
        quesAdapter=new FocusQuesAdapter(tagList,this);
        listQues.setAdapter(quesAdapter);
        listQues.setMenuCreator(creator);
        listQues.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listQues.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ToastUtils.showShortToast( MyFocusActivity.this, "取消");
                        break;
                }
                return false;
            }
        });
        getFocusUser();
        getFocusQuestion();
        getFocusTag();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back,R.id.radio_one, R.id.radio_two,R.id.radio_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.radio_one:
                listUser.setVisibility(View.VISIBLE);
                listQues.setVisibility(View.GONE);
                listTag.setVisibility(View.GONE);
                break;
            case R.id.radio_two:
                listUser.setVisibility(View.GONE);
                listQues.setVisibility(View.VISIBLE);
                listTag.setVisibility(View.GONE);
                break;
            case R.id.radio_three:
                listUser.setVisibility(View.GONE);
                listQues.setVisibility(View.GONE);
                listTag.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void getFocusUser(){
        Map<String,Object> map=new HashMap<>();
        map.put("uid", SPUtils.get(this,"userId","").toString());
        map.put("token",SPUtils.get(this,"token","").toString());
        SubjectPost postEntity=new SubjectPost(new ProgressSubscriber(getFocusUserListener,this,false,false),map);
        HttpManager.getInstance().getFollowUser(postEntity);
    }
    HttpOnNextListener getFocusUserListener=new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
                JsonArray array = JSONUtils.getAsJsonArray(o);
                JsonObject object;
            if(array!=null) {
                for (int i = 0; i < array.size(); i++) {
                    object = array.get(i).getAsJsonObject();
                    userList.add(new FocusEntity(object.get("be_uid").getAsString(), object.get("uname").getAsString(), object.get("headimgs").getAsString(),
                            object.get("add_time").getAsString(), object.get("introduction").getAsString()));
                }
                peopleAdapter.notifyDataSetChanged();
            }
        }
    };
    private void getFocusQuestion(){
        Map<String,Object> map=new HashMap<>();
        map.put("uid", SPUtils.get(this,"userId","").toString());
        map.put("token",SPUtils.get(this,"token","").toString());
        SubjectPost postEntity=new SubjectPost(new ProgressSubscriber(getFocusQuestionListener,this,false,false),map);
        HttpManager.getInstance().getFollowArticle(postEntity);
    }
    HttpOnNextListener getFocusQuestionListener=new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            quesAdapter.notifyDataSetChanged();
        }
    };
    private void getFocusTag(){
        Map<String,Object> map=new HashMap<>();
        map.put("uid", SPUtils.get(this,"userId","").toString());
        map.put("token",SPUtils.get(this,"token","").toString());
        SubjectPost postEntity=new SubjectPost(new ProgressSubscriber(getFocusTagListener,this,false,false),map);
        HttpManager.getInstance().getFollowLabel(postEntity);
    }
    HttpOnNextListener getFocusTagListener=new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
                JsonArray array = JSONUtils.getAsJsonArray(o);
            if(array!=null) {
                JsonObject object;
                for (int i = 0; i < array.size(); i++) {
                    object = array.get(i).getAsJsonObject();
                    tagList.add(new FocusEntity(object.get("label_id").getAsString(), object.get("label_name").getAsString(), "", "", object.get("add_time").getAsString()));
                }
                focusTagAdapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
