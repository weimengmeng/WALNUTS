package com.njjd.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.njjd.adapter.ConversationAdapter;
import com.njjd.adapter.FocusPeopleAdapter;
import com.njjd.adapter.FocusQuesAdapter;
import com.njjd.adapter.InformAdapter;
import com.njjd.application.ConstantsVal;
import com.njjd.db.DBHelper;
import com.njjd.domain.InformEntity;
import com.njjd.domain.QuestionEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.ChatActivity;
import com.njjd.walnuts.IndexDetailActivity;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/10.
 */

public class MessageFragment extends BaseFragment implements HttpOnNextListener {
    @BindView(R.id.list_mes)
    SwipeMenuListView listMes;
    @BindView(R.id.list_inform)
    SwipeMenuListView listInform;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    private Context context;
    @BindView(R.id.top)
    LinearLayout top;
    private SwipeMenuCreator creator;
    Map<String, EMConversation> conversationsMap;
    private ConversationAdapter adapter;
    List<EMConversation> conversations = new ArrayList<>();
    private Receiver receiver;

    private InformAdapter adapterInform;
    private List<InformEntity> entities = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        conversations.clear();
        conversationsMap = EMClient.getInstance().chatManager().getAllConversations();
        for (Map.Entry<String, EMConversation> entry : conversationsMap.entrySet()) {
            conversations.add(entry.getValue());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsVal.MESSAGE_RECEIVE);
        context.registerReceiver(receiver, filter);
        ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), top);
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem messItem = new SwipeMenuItem(
                        context);
                messItem.setBackground(new ColorDrawable(Color.LTGRAY));
                messItem.setWidth(220);
                messItem.setTitle("置顶");
                messItem.setTitleSize(16);
                messItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(messItem);

                SwipeMenuItem focusItem = new SwipeMenuItem(
                        context);
                focusItem.setBackground(R.color.login);
                focusItem.setWidth(220);
                focusItem.setTitle("删除");
                focusItem.setTitleSize(16);
                focusItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(focusItem);
            }
        };
        listMes.setMenuCreator(creator);
        listMes.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        adapter = new ConversationAdapter(getContext(), conversations);
        listMes.setAdapter(adapter);
        listMes.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ToastUtils.showShortToast(context, "置顶");
                        break;
                    case 1:
                        ToastUtils.showShortToast(context, "删除");
                        break;
                }
                return false;
            }
        });
        listMes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name", conversations.get(position).getLastMessage().getTo());
                startActivity(intent);
            }
        });
        /**
         * tongzhi
         */
        adapterInform = new InformAdapter(context, entities);
        listInform.setAdapter(adapterInform);
        getMyInform();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(listInform.getVisibility()==View.VISIBLE) {
                    InformAdapter.CURRENT_PAGE = 1;
                    getMyInform();
                }else if(listMes.getVisibility()==View.VISIBLE){
                    onResume();
                    refresh.setRefreshing(false);
                }
            }
        });
        listInform.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        InformAdapter.CURRENT_PAGE++;
                        getMyInform();
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(listInform != null && listInform.getChildCount() > 0){
                    boolean firstItemVisible = listInform.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listInform.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refresh.setEnabled(enable);
            }});
        listInform.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent;
                    switch (entities.get(position).getType()){
//            0 系统通知 1 关注用户 2 关注问题 3回答问题 4 评论回答 5 收藏回答 6 点赞",
                        case "0.0":
                            break;
                        case "1.0":
                            intent=new Intent(context, PeopleInfoActivity.class);
                            intent.putExtra("uid",entities.get(position).getUid());
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                            break;
                        case "2.0":
                            break;
                        case "3.0":
                            intent=new Intent(context, IndexDetailActivity.class);
                            QuestionEntity entity= DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().load(entities.get(position).getArticle_id());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("question", entity);
                            intent.putExtra("question", bundle);
                            intent.putExtra("type","2");
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                            break;
                        case "4.0":
                            break;
                        case "5.0":
                            break;
                        case "6.0":
                            break;
                    }
            }
        });
    }

    private void getMyInform() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(context, "userId", "").toString());
        map.put("token", SPUtils.get(context, "token", "").toString());
        map.put("page", InformAdapter.CURRENT_PAGE);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, context, false, false), map);
        HttpManager.getInstance().getNotice(postEntity);
    }

    @Override
    public void onNext(Object o) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        refresh.setRefreshing(false);
        if (InformAdapter.CURRENT_PAGE == 1) {
            entities.clear();
        }
        try {
            JSONObject object = new JSONObject(gson.toJson(o));
            JSONArray array = object.getJSONArray("notice");
            for (int i = 0; i < array.length(); i++) {
                entities.add(new InformEntity(array.getJSONObject(i)));
            }
            adapterInform.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
    }

    @Override
    public void lazyInitData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.radio_mess, R.id.radio_inform})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radio_mess:
                listMes.setVisibility(View.VISIBLE);
                listInform.setVisibility(View.GONE);
                break;
            case R.id.radio_inform:
                listInform.setVisibility(View.VISIBLE);
                listMes.setVisibility(View.GONE);
                break;
        }
    }

    public class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            onResume();
        }
    }
}
