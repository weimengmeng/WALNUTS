package com.njjd.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.njjd.adapter.ConversationAdapter;
import com.njjd.adapter.InformAdapter;
import com.njjd.application.ConstantsVal;
import com.njjd.db.DBHelper;
import com.njjd.domain.InformEntity;
import com.njjd.domain.MyConversation;
import com.njjd.domain.QuestionEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.walnuts.ChatActivity;
import com.njjd.walnuts.IndexDetailActivity;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;
import com.umeng.socialize.utils.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    XRecyclerView listMes;
    @BindView(R.id.list_inform)
    XRecyclerView listInform;
    @BindView(R.id.radio_inform)
    RadioButton radioInform;
    private Context context;
    @BindView(R.id.top)
    LinearLayout top;
    Map<String, EMConversation> conversationsMap;
    private ConversationAdapter adapter;
    List<MyConversation> conversations = new ArrayList<>();
    private Receiver receiver;
    private InformReceive informReceive;
    private InformAdapter adapterInform;
    private List<InformEntity> entities = new ArrayList<>();
    List<EMConversation> messageList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        conversations.clear();
        messageList = loadConversationsWithRecentChat();
        conversationsMap = EMClient.getInstance().chatManager().getAllConversations();
        for (int i = 0; i < messageList.size(); i++) {
            MyConversation conversation = new MyConversation(messageList.get(i));
            if(messageList.get(i).getLatestMessageFromOthers()!=null)
            conversation.setOpenId(messageList.get(i).getLatestMessageFromOthers().getFrom());
            else
                conversation.setOpenId(messageList.get(i).getLastMessage().getTo());
            conversations.add(conversation);
        }
//        for (Map.Entry<String, EMConversation> entry : conversationsMap.entrySet()) {
//            conversations.add(entry.getValue());
//        }
        adapter.notifyDataSetChanged();
        if (messageList.size() > 0) {
            getUserInfoByOpenId();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsVal.MESSAGE_RECEIVE);
        context=getContext();
        context.registerReceiver(receiver, filter);
        informReceive = new InformReceive();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(ConstantsVal.NEW_INFORM);
        context.registerReceiver(informReceive, filter1);
        ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), top);
//        listMes.setMenuCreator(creator);
//        listMes.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        adapter = new ConversationAdapter(getContext(), conversations);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        listMes.setLayoutManager(layoutManager);//这里用线性显示 类似于listview
        listMes.setAdapter(adapter);
        listMes.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                onResume();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        listMes.refreshComplete();
                    }
                }, 1500);
            }

            @Override
            public void onLoadMore() {
            }
        });
        adapter.setOnItemClickListener(new ConversationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("openId", conversations.get(position).getOpenId());
                intent.putExtra("name", conversations.get(position).getName());
                intent.putExtra("avatar",conversations.get(position).getAvatar());
                startActivity(intent);
            }
        });
//        listMes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(context, ChatActivity.class);
//                intent.putExtra("openId", conversations.get(position).getOpenId());
//                startActivity(intent);
//            }
//        });
        /**
         * tongzhi
         */
        adapterInform = new InformAdapter(context, entities);
        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        listInform.setLayoutManager(layoutManager1);//这里用线性显示 类似于listview
        listInform.setAdapter(adapterInform);
        listInform.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getMyInform();
            }

            @Override
            public void onLoadMore() {
                InformAdapter.CURRENT_PAGE++;
                getMyInform();
            }
        });
        getMyInform();
        adapterInform.setOnItemClickListener(new InformAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent;
                QuestionEntity entity = null;
                Bundle bundle = null;
                switch (entities.get(position).getType()) {
//            0 系统通知 1 关注用户 2 关注问题 3回答问题 4 评论回答 5 收藏回答 6 点赞",
                    case "0.0":
                        break;
                    case "1.0":
                    case "2.0":
                    case "5.0":
                    case "6.0":
                        intent = new Intent(context, PeopleInfoActivity.class);
                        intent.putExtra("uid", entities.get(position).getUid());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                        break;
                    case "3.0":
                        intent = new Intent(context, IndexDetailActivity.class);
                        bundle = new Bundle();
                        entity = DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().load(entities.get(position).getArticle_id());
                        intent.putExtra("comment_id", String.valueOf(Float.valueOf(entities.get(position).getComment_id())));
                        bundle.putSerializable("question", entity);
                        intent.putExtra("question", bundle);
                        intent.putExtra("type", "2");
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                        break;
                    case "4.0":
                        intent = new Intent(context, IndexDetailActivity.class);
                        bundle = new Bundle();
                        try {
                            entity = DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().load(entities.get(position).getContent().getString("article_id"));
                            intent.putExtra("comment_id", String.valueOf(Float.valueOf(entities.get(position).getContent().getString("answer_id"))));
                            bundle.putSerializable("question", entity);
                            intent.putExtra("question", bundle);
                            intent.putExtra("type", "2");
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }

    private void getUserInfoByOpenId() {
        String ids = "";
        for (MyConversation conversation : conversations)
            ids = ids + conversation.getOpenId() + ",";
        Map<String, Object> map = new HashMap<>();
        map.put("uids", ids);
        map.put("uid", SPUtils.get(context, "userId", ""));
        map.put("token", SPUtils.get(context, "token", ""));
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.serializeNulls(); //重点
                Gson gson = gsonBuilder.create();
                try {
                    JSONArray array = new JSONArray(gson.toJson(o));
                    for (int i=0;i<array.length();i++) {
                        conversations.get(i).setJson(array.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }, context, false, false), map);
        HttpManager.getInstance().getUserUids(postEntity);
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
        if (InformAdapter.CURRENT_PAGE == 1) {
            entities.clear();
            listInform.refreshComplete();
        } else {
            listInform.loadMoreComplete();
        }
        try {
            JSONObject object = new JSONObject(gson.toJson(o));
            JSONArray array = object.getJSONArray("notice");
            for (int i = 0; i < array.length(); i++) {
                entities.add(new InformEntity(array.getJSONObject(i)));
            }
            adapterInform.notifyDataSetChanged();
            LogUtils.d("notice");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(receiver);
        context.unregisterReceiver(informReceive);
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

    public class InformReceive extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            InformAdapter.CURRENT_PAGE = 1;
            radioInform.performClick();
            getMyInform();
        }
    }

    /**
     * 获取所有会话
     *
     * @return +
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation
                            .getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(
            List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList,
                new Comparator<Pair<Long, EMConversation>>() {
                    @Override
                    public int compare(final Pair<Long, EMConversation> con1,
                                       final Pair<Long, EMConversation> con2) {

                        if (con1.first == con2.first) {
                            return 0;
                        } else if (con2.first > con1.first) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }

                });
    }
}
