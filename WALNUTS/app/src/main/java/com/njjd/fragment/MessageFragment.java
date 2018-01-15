package com.njjd.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.njjd.adapter.ConversationAdapter;
import com.njjd.adapter.InformAdapter;
import com.njjd.adapter.MyPagerAdapter;
import com.njjd.adapter.OnItemClickListener;
import com.njjd.application.ConstantsVal;
import com.njjd.db.DBHelper;
import com.njjd.domain.CommentEntity;
import com.njjd.domain.InformEntity;
import com.njjd.domain.MyConversation;
import com.njjd.domain.QuestionEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.ItemRemoveRecyclerView;
import com.njjd.utils.LogUtils;
import com.njjd.utils.NetworkUtils;
import com.njjd.utils.RecycleViewDivider;
import com.njjd.utils.SPUtils;
import com.njjd.utils.TipButton;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.ArticleComReplyActivity;
import com.njjd.walnuts.ChatActivity;
import com.njjd.walnuts.ColumnActivity;
import com.njjd.walnuts.ColumnDetailActivity;
import com.njjd.walnuts.IndexDetailActivity;
import com.njjd.walnuts.MainActivity;
import com.njjd.walnuts.MyAnswerActivity;
import com.njjd.walnuts.MyQuestionActivity;
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
    ItemRemoveRecyclerView listMes;
    XRecyclerView listInform;
    @BindView(R.id.radio_inform)
    TipButton radioInform;
    private Context context;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.mess_page)
    ViewPager messPage;
    @BindView(R.id.radio_mess)
    TipButton radioMess;
    private List<View> viewList;
    private MyPagerAdapter pagerAdapter;
    Map<String, EMConversation> conversationsMap;
    private ConversationAdapter adapter;
    List<MyConversation> conversations = new ArrayList<>();
    private Receiver receiver;
    private InformReceive informReceive;
    private InformAdapter adapterInform;
    private List<InformEntity> entities = new ArrayList<>();
    private int temp = 0;
    private boolean loadmoe = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), top);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        conversations.clear();
        EMClient.getInstance().chatManager().loadAllConversations();
        conversationsMap = EMClient.getInstance().chatManager().getAllConversations();
        if (conversationsMap != null)
            for (EMConversation conversation1 : conversationsMap.values()) {
                if (conversation1.getLastMessage().getChatType() != EMMessage.ChatType.ChatRoom) {
                    MyConversation conversation = new MyConversation(conversation1);
                    if (conversation1.getLatestMessageFromOthers() != null) {
                        conversation.setOpenId(conversation1.getLatestMessageFromOthers().getFrom());
                    } else {
                        conversation.setOpenId(conversation1.getLastMessage().getTo());
                    }
                    conversations.add(conversation);
                }
            }
        adapter.notifyDataSetChanged();
        if (conversations.size() > 0) {
            getUserInfoByOpenId();
        }
        if (EMClient.getInstance().chatManager().getUnreadMessageCount() > 0) {
            if (MainActivity.temp != 2 || messPage.getCurrentItem() != 0) {
                radioMess.setTipOn(true, 1);
                radioMess.invalidate();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsVal.MESSAGE_RECEIVE);
        context = getContext();
        context.registerReceiver(receiver, filter);
        informReceive = new InformReceive();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(ConstantsVal.NEW_INFORM);
        context.registerReceiver(informReceive, filter1);
        LinearLayout linearLayout = (LinearLayout) view.inflate(context, R.layout.mess_chat, null);
        listMes = linearLayout.findViewById(R.id.list_mes);
        listMes.setEmptyView(linearLayout.findViewById(R.id.empty));
        ((ImageView) linearLayout.findViewById(R.id.img_nodata)).setImageDrawable(getResources().getDrawable(R.drawable.no_news));
        ((TextView) linearLayout.findViewById(R.id.txt_content)).setText("暂无新消息");
        if (NetworkUtils.getNetworkType(context) == 0 || NetworkUtils.getNetworkType(context) == 1) {
            ((ImageView) linearLayout.findViewById(R.id.img_nodata)).setImageDrawable(getResources().getDrawable(R.drawable.no_net));
            ((TextView) linearLayout.findViewById(R.id.txt_content)).setText("请检查网络设置");
        }
        adapter = new ConversationAdapter(getContext(), conversations);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        listMes.setLayoutManager(layoutManager);//这里用线性显示 类似于listview
        listMes.setAdapter(adapter);
        listMes.setRefreshProgressStyle(ProgressStyle.BallPulse);
        viewList = new ArrayList<>();
        viewList.add(linearLayout);
        linearLayout = (LinearLayout) view.inflate(context, R.layout.mess_inform, null);
        listInform = linearLayout.findViewById(R.id.list_inform);
        listInform.setEmptyView(linearLayout.findViewById(R.id.empty));
        ((TextView) linearLayout.findViewById(R.id.txt_content)).setText("暂无新通知");
        viewList.add(linearLayout);
        pagerAdapter = new MyPagerAdapter(viewList);
        messPage.setAdapter(pagerAdapter);
        messPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    radioMess.setChecked(true);
                    radioMess.setTipOn(false, 1);
                    radioMess.invalidate();
                } else {
                    radioInform.setChecked(true);
                    radioInform.setTipOn(false, 1);
                    radioInform.invalidate();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        listMes.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                onResume();
                radioMess.setTipOn(false, 1);
                radioMess.invalidate();
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
                radioMess.setTipOn(false, 1);
                radioMess.invalidate();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("openId", conversations.get(position).getOpenId());
                intent.putExtra("name", conversations.get(position).getName());
                intent.putExtra("avatar", conversations.get(position).getAvatar());
                startActivity(intent);
            }
        });
        listMes.setmListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //删除和某个user会话，如果需要保留聊天记录，传false
                if (conversations.get(position - 1).getConversation().getLatestMessageFromOthers() != null) {
                    EMClient.getInstance().chatManager().deleteConversation(conversations.get(position - 1).getConversation().getLatestMessageFromOthers().getFrom(), true);
                } else {
                    EMClient.getInstance().chatManager().deleteConversation(conversations.get(position - 1).getConversation().getLastMessage().getTo(), true);
                }
                adapter.remove(position - 1);
            }

            @Override
            public void onDeleteClick(int position) {
            }
        });
        listMes.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL));
        listMes.setItem_delete(R.id.item_delete);
        /**
         * tongzhi
         */
        adapterInform = new InformAdapter(context, entities);
        final LinearLayoutManager layoutManager1 = new LinearLayoutManager(context);
        listInform.setLayoutManager(layoutManager1);//这里用线性显示 类似于listview
        listInform.setAdapter(adapterInform);
        listInform.setRefreshProgressStyle(ProgressStyle.BallPulse);
        listInform.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        listInform.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.getNetworkType(context) == 0 || NetworkUtils.getNetworkType(context) == 1) {
                    ToastUtils.showShortToast(context, "网络中断，请检查您的网络状态");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listInform.refreshComplete();
                        }
                    }, 500);
                    return;
                }
                InformAdapter.CURRENT_PAGE = 1;
                radioInform.setTipOn(false, 1);
                radioInform.invalidate();
                getMyInform();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        listInform.refreshComplete();
                    }
                }, 6000);
            }

            @Override
            public void onLoadMore() {
                if (NetworkUtils.getNetworkType(context) == 0 || NetworkUtils.getNetworkType(context) == 1) {
                    ToastUtils.showShortToast(context, "网络中断，请检查您的网络状态");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listInform.loadMoreComplete();
                        }
                    }, 500);
                    return;
                }
                if (!loadmoe) {
                    ToastUtils.showShortToast(context, "已加载全部数据啦");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listInform.loadMoreComplete();
                        }
                    }, 500);
                    return;
                }
                InformAdapter.CURRENT_PAGE++;
                getMyInform();
            }
        });
        getMyInform();
        adapterInform.setOnItemClickListener(new InformAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                radioInform.setTipOn(false, 1);
                radioInform.invalidate();
                Intent intent;
                QuestionEntity entity = null;
                Bundle bundle = null;
                switch (entities.get(position).getType()) {
//            0 系统通知 1 关注用户 2 关注问题 3回答问题 4 评论回答 5 收藏回答 6 点赞",
                    case "0.0":
                        try {
                            if (entities.get(position).getContent().getString("type").equals("0.0")) {
                            } else if (entities.get(position).getContent().getString("type").equals("1.0")) {
                                intent = new Intent(context, MyQuestionActivity.class);
                                intent.putExtra("uid", entities.get(position).getContent().getString("uid"));
                                startActivity(intent);
                            } else if (entities.get(position).getContent().getString("type").equals("2.0")) {
                                intent = new Intent(context, MyAnswerActivity.class);
                                intent.putExtra("uid", entities.get(position).getContent().getString("uid"));
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "1.0":
                    case "5.0":
                    case "2.0":
                        intent = new Intent(context, PeopleInfoActivity.class);
                        intent.putExtra("uid", entities.get(position).getUid());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                        break;
                    case "3.0":
                        try {
                            if (entities.get(position).getContent().getString("type").equals("2.0")) {
                                intent = new Intent(context, ColumnDetailActivity.class);
                                intent.putExtra("article_id", Float.valueOf(entities.get(position).getArticle_id()).intValue() + "");
                                context.startActivity(intent);
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        entity = DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().load(entities.get(position).getArticle_id());
                        if (entity == null) {
                            getDetail(Float.valueOf(entities.get(position).getArticle_id()).intValue() + "", String.valueOf(Float.valueOf(entities.get(position).getComment_id())), entities.get(position).getComment_id());
                        } else {
                            intent = new Intent(context, IndexDetailActivity.class);
                            bundle = new Bundle();
                            intent.putExtra("comment_id", String.valueOf(Float.valueOf(entities.get(position).getComment_id())));
                            bundle.putSerializable("question", entity);
                            intent.putExtra("question", bundle);
                            intent.putExtra("current_commentId", entities.get(position).getComment_id());
                            intent.putExtra("type", "3");
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                        }
                        break;
                    case "6.0":
                        intent = new Intent(context, IndexDetailActivity.class);
                        bundle = new Bundle();
                        try {
                            entity = DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().load(entities.get(position).getContent().getString("article_id"));
                            if (entity == null) {
                                getDetail(Float.valueOf(entities.get(position).getContent().getString("article_id")).intValue() + "", String.valueOf(Float.valueOf(entities.get(position).getContent().getString("comment_id"))), entities.get(position).getComment_id());
                            } else {
                                intent.putExtra("comment_id", String.valueOf(Float.valueOf(entities.get(position).getContent().getString("comment_id"))));
                                bundle.putSerializable("question", entity);
                                intent.putExtra("current_commentId", entities.get(position).getComment_id());
                                intent.putExtra("question", bundle);
                                intent.putExtra("type", "3");
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "4.0":
                        try {
                            if (entities.get(position).getContent().isNull("type") || entities.get(position).getContent().getString("type").equals("1.0")) {
                                intent = new Intent(context, IndexDetailActivity.class);
                                bundle = new Bundle();
                                try {
                                    entity = DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().load(entities.get(position).getContent().getString("article_id"));
                                    if (entity == null) {
                                        getDetail(Float.valueOf(entities.get(position).getContent().getString("article_id")).intValue() + "", String.valueOf(Float.valueOf(entities.get(position).getContent().getString("answer_id"))), entities.get(position).getComment_id());
                                    } else {
                                        intent.putExtra("comment_id", String.valueOf(Float.valueOf(entities.get(position).getContent().getString("answer_id"))));
                                        bundle.putSerializable("question", entity);
                                        intent.putExtra("current_commentId", entities.get(position).getComment_id());
                                        intent.putExtra("question", bundle);
                                        intent.putExtra("type", "3");
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                intent = new Intent(context, ArticleComReplyActivity.class);
                                intent.putExtra("type", "1");
                                CommentEntity commentEntity = new CommentEntity();
                                commentEntity.setCommentId(entities.get(position).getContent().getString("answer_id"));
                                bundle = new Bundle();
                                bundle.putSerializable("comment", commentEntity);
                                intent.putExtra("comment", bundle);
                                intent.putExtra("article_id", String.valueOf(Float.valueOf(entities.get(position).getContent().getString("article_id")).intValue()));
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "7.0":
                        intent = new Intent(context, ColumnActivity.class);
                        try {
                            intent.putExtra("column_id", String.valueOf(Float.valueOf(entities.get(position).getContent().getString("id"))));
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

    private void getDetail(final String id, final String answer_id, final String current_commentId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("uid", SPUtils.get(context, "userId", ""));
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.serializeNulls(); //重点
                Gson gson = gsonBuilder.create();
                Intent intent = new Intent(context, IndexDetailActivity.class);
                QuestionEntity entity = null;
                try {
                    entity = new QuestionEntity(new JSONObject(gson.toJson(o)), "1");
                    entity.setQuestionId(Float.valueOf(id).intValue() + "");
                    Bundle bundle = new Bundle();
                    intent.putExtra("comment_id", answer_id);
                    bundle.putSerializable("question", entity);
                    intent.putExtra("current_commentId", current_commentId);
                    intent.putExtra("question", bundle);
                    intent.putExtra("type", "3");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, context, false, false), map);
        HttpManager.getInstance().getArticleDetail(postEntity);
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
                    for (int i = 0; i < array.length(); i++) {
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
            if (array.length() < 10) {
                loadmoe = false;
            } else {
                loadmoe = true;
            }
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
                messPage.setCurrentItem(0);
                radioMess.setTipOn(false, 1);
                radioMess.invalidate();
                break;
            case R.id.radio_inform:
                messPage.setCurrentItem(1);
                radioInform.setTipOn(false, 1);
                radioInform.invalidate();
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
            radioInform.setTipOn(true, 1);
            radioInform.invalidate();
            getMyInform();
        }
    }
}
