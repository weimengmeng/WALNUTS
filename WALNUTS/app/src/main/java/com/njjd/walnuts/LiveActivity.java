package com.njjd.walnuts;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.njjd.adapter.LiveChatAdapter;
import com.njjd.adapter.MSGLAdapter;
import com.njjd.adapter.MyPagerAdapter;
import com.njjd.utils.AndroidBug5497Workaround;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 18/1/15.
 */

public class LiveActivity extends BaseActivity{
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_focus)
    TextView txtFocus;
    @BindView(R.id.live_page)
    ViewPager livePage;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.radio1)
    RadioButton radio1;
    @BindView(R.id.radio2)
    RadioButton radio2;
    private List<View> viewList;
    private MyPagerAdapter adapter;
    private LayoutInflater myinflater;
    private View currentView;
    private WebView webView;
    private ListView chatListView;
    private LiveChatAdapter chatAdapter;
    private List<EMMessage> messagesList;
    EMConversation conversation;
    private EditText userEt;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().chatManager().getAllConversations();
                conversation = EMClient.getInstance().chatManager().getConversation("38379021467649");
                if (conversation == null) {
                    messagesList = new ArrayList<>();
                } else {
                    conversation.loadMoreMsgFromDB(conversation.getLastMessage().getMsgId(), 1000);
                    messagesList = conversation.getAllMessages();
                    conversation.markAllMessagesAsRead();
                }
                chatAdapter = new LiveChatAdapter(LiveActivity.this, messagesList);
                chatListView.setAdapter(chatAdapter);
                chatListView.setSelection(chatAdapter.getCount());
                initConversionLitener();
            }else{
                chatAdapter.notifyDataSetChanged();
                chatListView.setSelection(chatAdapter.getCount());
            }
        }
    };
    @Override
    public int bindLayout() {
        return R.layout.activity_live;
    }

    @Override
    public void initView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置头部控件ViewGroup的PaddingTop,防止界面与状态栏重叠
            int statusBarHeight = ImmersedStatusbarUtils.getStatusBarHeight(this);
            findViewById(R.id.re_top).setPadding(0, statusBarHeight, 0,0);
        }
        AndroidBug5497Workaround.assistActivity(this);
        viewList = new ArrayList<>();
        myinflater = LayoutInflater.from(this);
        currentView = myinflater.inflate(R.layout.live_item1, null);
        webView=currentView.findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDisplayZoomControls(false); //隐藏webview缩放按钮
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webView.loadUrl("http://www.baidu.com");
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view,
                                                    final String url) {
                view.loadUrl(url);
                return true;
            }
        });
        viewList.add(currentView);
        currentView = myinflater.inflate(R.layout.live_item2, null);
        chatListView=currentView.findViewById(R.id.live_list);
        userEt=currentView.findViewById(R.id.et_content);
        userEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendChatMessage();
                return true;
            }
        });
        viewList.add(currentView);
        adapter = new MyPagerAdapter(viewList);
        livePage.setAdapter(adapter);
        livePage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    radioGroup.check(R.id.radio1);
                }else{
                    radioGroup.check(R.id.radio2);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //roomId为聊天室ID
        EMClient.getInstance().chatroomManager().joinChatRoom("38379021467649", new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(EMChatRoom value) {
                //加入聊天室成功
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onError(final int error, String errorMsg) {
                //加入聊天室失败
                userEt.setVisibility(View.GONE);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.btn_add_help, R.id.radio1, R.id.radio2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_add_help:
                BaseActivity.showToast("分享直播");
                break;
            case R.id.radio1:
                livePage.setCurrentItem(0);
                break;
            case R.id.radio2:
                livePage.setCurrentItem(1);
                break;
        }
    }
    private void initConversionLitener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                if(messages.get(0).getTo().equals("38379021467649")) {
                    messagesList.add(messages.get(0));
                    handler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EMClient.getInstance().chatroomManager().leaveChatRoom("38379021467649");
    }
    private void sendChatMessage(){
        if(userEt.getText().toString().trim().equals("")){
            BaseActivity.showToast("请输入内容");
            return;
        }
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(userEt.getText().toString().trim(), "38379021467649");
        message.setAttribute("avatar", SPUtils.get(this, "head", "").toString());
        message.setAttribute("username", SPUtils.get(this, "name", "").toString());
        message.setChatType(EMMessage.ChatType.ChatRoom);
        EMClient.getInstance().chatManager().sendMessage(message);
        messagesList.add(message);
        handler.sendEmptyMessage(0);
        message.setMessageStatusCallback(emCallBack);
        userEt.setText("");
    }
    private EMCallBack emCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            LogUtils.d("huan","succeedMsg");
            EMClient.getInstance().chatManager().importMessages(messagesList);
        }

        @Override
        public void onError(int code, String error) {
            LogUtils.d("huan",error);
        }

        @Override
        public void onProgress(int progress, String status) {

        }
    };

}
