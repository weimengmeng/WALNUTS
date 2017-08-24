//package com.njjd.walnuts;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Hashtable;
//import java.util.List;
//
//import org.apache.http.Header;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.util.Pair;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.bdkj.gmys.R;
//import com.bdkj.gmys.adapter.ChatHistoryLAdapter;
//import com.bdkj.gmys.domain.MyConversation;
//import com.bdkj.gmys.utils.BaseResponseHandler;
//import com.bdkj.gmys.utils.HttpUtils;
//import com.bdkj.gmys.utils.SPUtil;
//import com.bdkj.gmys.utils.StringUtil;
//import com.easemob.EMEventListener;
//import com.easemob.EMNotifierEvent;
//import com.easemob.applib.controller.HXSDKHelper;
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMContactManager;
//import com.easemob.chat.EMConversation;
//import com.easemob.chat.EMMessage;
//import com.easemob.exceptions.EaseMobException;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.loopj.android.http.RequestParams;
//import com.umeng.message.PushAgent;
//import com.zf.iosdialog.widget.AlertDialog;
//
///**
// * @author younminx
// * @description 消息历史activity
// * @date 2015-11-12 上午11:11:56
// */
//public class ChatHistoryActivity extends Activity implements EMEventListener{
//	Activity context;
//	String username;
//	PullToRefreshListView listView;
//	ChatHistoryLAdapter adapter;
//	List<MyConversation> list = new ArrayList<MyConversation>();
//	Handler mainHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if (msg.what ==1) {
//				onResume();
//			}else if(msg.what==0){
//				adapter.notifyDataSetChanged();
//			}
//		};
//	};
//	List<EMConversation> messageList = new ArrayList<EMConversation>();
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_chat_history);
//		PushAgent.getInstance(this).onAppStart();
//		context = this;
//		EMChatManager.getInstance().registerEventListener(
//				this,
//				new EMNotifierEvent.Event[] {
//						EMNotifierEvent.Event.EventNewMessage,
//						EMNotifierEvent.Event.EventOfflineMessage,
//						EMNotifierEvent.Event.EventConversationListChanged });
//		init();
//	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//		init();
//	}
//	private void init() {
//		list.clear();
//		username = StringUtil.getMD5("doctor"
//				+ SPUtil.getShareP(context).getString("userId", "-1"));
//		((TextView) findViewById(R.id.txt_title)).setText("实时诊疗");
//		messageList = loadConversationsWithRecentChat();
//		for(int i=0;i<messageList.size();i++){
//			MyConversation conversation = new MyConversation(messageList.get(i));
//			conversation.setOpenId(messageList.get(i).getUserName());
//			list.add(conversation);
//		}
//		listView = (PullToRefreshListView) findViewById(R.id.listview);
//		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
//
//			@Override
//			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				list.clear();
//				messageList = loadConversationsWithRecentChat();
//				for(int i=0;i<messageList.size();i++){
//					MyConversation conversation = new MyConversation(messageList.get(i));
//					conversation.setOpenId(messageList.get(i).getUserName());
//					list.add(conversation);
//				}
//				if (list.size() > 0)
//					getDoctorByHuanXinId();
//			}
//		});
//		adapter = new ChatHistoryLAdapter(context, list);
//		listView.setAdapter(adapter);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Intent intent = new Intent(context, RealTimeActivity.class);
//				intent.putExtra("openId", list.get(position - 1).getOpenId());
//				startActivity(intent);
//			}
//		});
//		listView.getRefreshableView().setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					final int arg2, long arg3) {
//				new AlertDialog(ChatHistoryActivity.this).builder()
//				.setTitle("删除提醒").setMsg("确定删除此会话？")
//				.setNegativeButton("取消", new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//
//					}
//				}).setPositiveButton("删除", new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						try {
//							EMContactManager.getInstance()
//									.deleteContact(
//											messageList.get(arg2-1)
//													.getUserName());
//							messageList.remove(arg2-1);
//							list.remove(arg2-1);
//							mainHandler.sendEmptyMessage(0);
//						} catch (EaseMobException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}).show();
//				return true;
//			}
//
//		});
//		if (list.size() > 0)
//			getDoctorByHuanXinId();
//	}
//
//	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.btn_top_left:
//			finish();
//			break;
//		default:
//			break;
//		}
//	}
//
//	public void getDoctorByHuanXinId() {
//		String ids = "";
//		for (MyConversation conversation : list)
//			ids = ids + conversation.getOpenId() + ",";
//		RequestParams params = new RequestParams();
//		params.put("huanxin_id", ids);
//		HttpUtils.post(context, HttpUtils.SERVERIP + "getDoctorByHuanXinId",
//				params, new BaseResponseHandler(context, false,
//						BaseResponseHandler.TOAST) {
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							JSONObject response) {
//						// TODO Auto-generated method stub
//							listView.onRefreshComplete();
//						try {
//							Log.i("", response.toString());
//							if (response.getInt("resultCode") == 0) {
//								JSONObject resultData = response
//										.getJSONObject("resultData");
//								JSONArray array = resultData
//										.getJSONArray("list");
//								for (int i = 0; i < array.length(); i++) {
//									list.get(i).setJson(array.getJSONObject(i));
//								}
//								adapter.notifyDataSetChanged();
//							} else {
//								super.onSuccess(statusCode, headers, response);
//							}
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				});
//	}
//	/**
//	 * 事件监听
//	 *
//	 * see {@link EMNotifierEvent}
//	 */
//	@Override
//	public void onEvent(EMNotifierEvent event) {
//		switch (event.getEvent()) {
//		case EventNewMessage:
//			// 声音和震动提示有新消息
//			HXSDKHelper.getInstance().getNotifier()
//					.viberateAndPlayTone((EMMessage)event.getData());
//			mainHandler.sendEmptyMessage(1);
//			break;
//		case EventDeliveryAck:
//		case EventReadAck:
//		case EventOfflineMessage:
//			break;
//		default:
//			break;
//		}
//	}
//	/**
//	 * 获取所有会话
//	 *
//	 * @param context
//	 * @return +
//	 */
//	private List<EMConversation> loadConversationsWithRecentChat() {
//		// 获取所有会话，包括陌生人
//		Hashtable<String, EMConversation> conversations = EMChatManager
//				.getInstance().getAllConversations();
//		// 过滤掉messages size为0的conversation
//		/**
//		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
//		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
//		 */
//		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
//		synchronized (conversations) {
//			for (EMConversation conversation : conversations.values()) {
//				if (conversation.getAllMessages().size() != 0) {
//					// if(conversation.getType() !=
//					// EMConversationType.ChatRoom){
//					sortList.add(new Pair<Long, EMConversation>(conversation
//							.getLastMessage().getMsgTime(), conversation));
//					// }
//				}
//			}
//		}
//		try {
//			// Internal is TimSort algorithm, has bug
//			sortConversationByLastChatTime(sortList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		List<EMConversation> list = new ArrayList<EMConversation>();
//		for (Pair<Long, EMConversation> sortItem : sortList) {
//			list.add(sortItem.second);
//		}
//		return list;
//	}
//
//	/**
//	 * 根据最后一条消息的时间排序
//	 *
//	 * @param usernames
//	 */
//	private void sortConversationByLastChatTime(
//			List<Pair<Long, EMConversation>> conversationList) {
//		Collections.sort(conversationList,
//				new Comparator<Pair<Long, EMConversation>>() {
//					@Override
//					public int compare(final Pair<Long, EMConversation> con1,
//							final Pair<Long, EMConversation> con2) {
//
//						if (con1.first == con2.first) {
//							return 0;
//						} else if (con2.first > con1.first) {
//							return 1;
//						} else {
//							return -1;
//						}
//					}
//
//				});
//	}
//
//}
