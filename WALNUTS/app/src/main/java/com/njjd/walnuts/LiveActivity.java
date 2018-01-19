package com.njjd.walnuts;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.listener.ProgressListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.ios.dialog.AlertDialog;
import com.njjd.adapter.LiveChatAdapter;
import com.njjd.adapter.MyPagerAdapter;
import com.njjd.application.AppAplication;
import com.njjd.domain.LiveRoom;
import com.njjd.fragment.MineFragment;
import com.njjd.http.HttpManager;
import com.njjd.utils.AndroidBug5497Workaround;
import com.njjd.utils.GlideImageLoder2;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.KeybordS;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.voice.AudioRecoderUtils;
import com.voice.PopupWindowFactory;
import com.voice.TimeUtils;
import com.youth.banner.Banner;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 18/1/15.
 */

public class LiveActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.lv_root)
    LinearLayout lvRoot;
    @BindView(R.id.txt_appoint)
    TextView txtAppoint;
    @BindView(R.id.txt_members)
    TextView txtMembers;
    @BindView(R.id.live_page)
    ViewPager livePage;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.radio1)
    RadioButton radio1;
    @BindView(R.id.radio2)
    RadioButton radio2;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.txt_position)
    TextView txtPosition;
    private List<View> viewList;
    private MyPagerAdapter adapter;
    private LayoutInflater myinflater;
    private View currentView;
    private WebView webView;
    private ListView chatListView;
    private LiveChatAdapter chatAdapter;
    private List<EMMessage> messagesList;
    EMConversation conversation;
    private ImageView masterMicro;
    private EditText userEt, masterEt;
    private TextView masterTxt;
    private LinearLayout masterLlv, customLlv;
    private boolean flag = false;
    private int length = 0;
    private AudioRecoderUtils mAudioRecoderUtils;
    private PopupWindowFactory mPop;
    private ImageView mImageView;
    private TextView mTextView;
    private String tempFilePath = "";
    private String chatRoomId = "";
    private String masterUid = "";
    private LiveRoom liveRoom;
    private File file;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                EMClient.getInstance().chatManager().loadAllConversations();
                EMClient.getInstance().chatManager().getAllConversations();
                conversation = EMClient.getInstance().chatManager().getConversation(chatRoomId);
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
                if (masterUid.equals(SPUtils.get(LiveActivity.this, "userId", "").toString())) {
                    masterLlv.setVisibility(View.VISIBLE);
                } else {
                    customLlv.setVisibility(View.VISIBLE);
                }
                initConversionLitener();
            }else {
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
        liveRoom = (LiveRoom) getIntent().getBundleExtra("liveRoom").get("liveRoom");
        chatRoomId = liveRoom.getChatRoomId();
        masterUid = liveRoom.getUid();
        txtMembers.setText(liveRoom.getMembers());
        final String[] img = liveRoom.getBannerImg().split(",");
        List<String> images = new ArrayList<>();
        for (int i = 0; i < img.length; i++) {
            images.add(img[i]);
        }
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position<=1){
                    txtPosition.setText(1+"/"+img.length);
                }else if(position>=img.length){
                    txtPosition.setText(img.length+"/"+img.length);
                }else{
                    txtPosition.setText(position+"/"+img.length);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        banner.isAutoPlay(false);
        banner.setImages(images).setImageLoader(GlideImageLoder2.getInstance()).start();
        long time = new Date().getTime();
        try {
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(liveRoom.getStartTime()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if ((time - new Date().getTime()) < 0) {
            txtTime.setText("直播已结束");
        } else {
            new CountDownTimer(time - new Date().getTime(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // (millisUntilFinished / (1000 * 60 * 60 * 24))+"天"+
                    txtTime.setText(Html.fromHtml("距离开播:<font color='#ffb129'>" +trans((millisUntilFinished / (1000 * 60 * 60 * 24)))+ "</font>天<font color='#ffb129'>"+ trans((millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)) + "</font>时<font color='#ffb129'>" + trans((millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)) + "</font>分<font color='#ffb129'>" + trans((millisUntilFinished % (1000 * 60)) / 1000) + "</font>秒"));
                }

                @Override
                public void onFinish() {
                    txtTime.setText("正在直播");
                }
            }.start();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置头部控件ViewGroup的PaddingTop,防止界面与状态栏重叠
            int statusBarHeight = ImmersedStatusbarUtils.getStatusBarHeight(this);
            findViewById(R.id.re_top).setPadding(0, statusBarHeight, 0, 0);
        }
        AndroidBug5497Workaround.assistActivity(this);
        viewList = new ArrayList<>();
        myinflater = LayoutInflater.from(this);
        currentView = myinflater.inflate(R.layout.live_item1, null);
        masterEt = currentView.findViewById(R.id.et_content);
        masterLlv = currentView.findViewById(R.id.lv_master);
        masterMicro = currentView.findViewById(R.id.btn_micro);
        masterMicro.setOnClickListener(this);
        masterTxt = currentView.findViewById(R.id.btn_voice);
        webView = currentView.findViewById(R.id.web);
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
        chatListView = currentView.findViewById(R.id.live_list);
        userEt = currentView.findViewById(R.id.et_content);
        customLlv = currentView.findViewById(R.id.lv_custom);
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
                if (position == 0) {
                    radioGroup.check(R.id.radio1);
                } else {
                    radioGroup.check(R.id.radio2);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //roomId为聊天室ID
        EMClient.getInstance().chatroomManager().joinChatRoom(chatRoomId, new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(EMChatRoom value) {
                //加入聊天室成功
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onError(final int error, String errorMsg) {
                LogUtils.d("huan", errorMsg);
            }
        });
        mAudioRecoderUtils = new AudioRecoderUtils();
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
                length = (int) time / 1000;
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                mTextView.setText(TimeUtils.long2String(0));
                tempFilePath = filePath;
                if (length < 1) {
                    ToastUtils.showShortToast(LiveActivity.this, "语音时间过短");
                    return;
                }
                new AlertDialog(LiveActivity.this).builder().setCancelable(false).setMsg("录音发送后不可撤销或撤回，您确定发送吗").setTitle("发送提示").setPositiveButton("发送", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendVoice();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
        //PopupWindow的布局文件
        final View view1 = View.inflate(this, R.layout.layout_microphone, null);

        mPop = new PopupWindowFactory(this, view1);
        //PopupWindow布局文件里面的控件
        mImageView = view1.findViewById(R.id.iv_recording_icon);
        mTextView = view1.findViewById(R.id.tv_recording_time);
        //Button的touch监听
        masterTxt.setTag("0");
        masterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (masterTxt.getTag().toString().equals("0")) {
                    mPop.showAtLocation(lvRoot, Gravity.CENTER, 0, 0);
                    mAudioRecoderUtils.startRecord();
                    masterTxt.setText("发送/取消");
                    masterTxt.setBackground(getResources().getDrawable(R.drawable.background_layout_et2));
                    masterTxt.setTag("1");
                } else {
                    mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
                    mPop.dismiss();
                    masterTxt.setText("点击录音");
                    masterTxt.setBackground(getResources().getDrawable(R.drawable.background_layout_et));
                    masterTxt.setTag("0");
                }
            }
        });
        masterEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendMasterTxt();
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.btn_add_help, R.id.radio1, R.id.radio2,R.id.txt_appoint})
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
            case R.id.txt_appoint:
                BaseActivity.showToast("预约直播");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (!flag) {
            if (!AppAplication.selfPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 100);
                return;
            }
            masterMicro.setImageResource(R.drawable.btn_jp);
            masterTxt.setVisibility(View.VISIBLE);
            masterEt.setVisibility(View.GONE);
            KeybordS.closeBoard(this);
            flag = true;
        } else {
            if (masterTxt.getTag().toString().equals("1")) {
                ToastUtils.showShortToast(LiveActivity.this, "当前正在录音");
                return;
            }
            masterMicro.setImageResource(R.drawable.btn_micro);
            masterTxt.setVisibility(View.GONE);
            masterEt.setVisibility(View.VISIBLE);
            KeybordS.openKeybord(masterEt, this);
            flag = false;
        }
    }

    private void initConversionLitener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                if (messages.get(0).getTo().equals(chatRoomId)) {
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
    }

    private void sendVoice() {
        file = new File(tempFilePath);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(sendLiveMsgLstener, this, true, false), file);
        HttpManager.getInstance().sendLiveVoice(postEntity, new MyUploadListener(),
                SPUtils.get(this, "userId", "").toString(), liveRoom.getId(),SPUtils.get(this, "token", "").toString(),"2");
    }

    private void sendMasterTxt() {
        if (masterTxt.getText().toString().trim().equals("")) {
            BaseActivity.showToast("请输入内容");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("uid",SPUtils.get(this,"userId","").toString());
        map.put("token",SPUtils.get(this,"token","").toString());
        map.put("msg",masterEt.getText().toString().trim());
        map.put("type",1);
        map.put("room_id",liveRoom.getId());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(sendLiveMsgLstener, this, false, false), map);
        HttpManager.getInstance().sendLiveMsg(postEntity);
    }
    HttpOnNextListener sendLiveMsgLstener=new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {

        }
    };
    private void sendChatMessage() {
        if (userEt.getText().toString().trim().equals("")) {
            BaseActivity.showToast("请输入内容");
            return;
        }
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(userEt.getText().toString().trim(), chatRoomId);
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
            EMClient.getInstance().chatManager().importMessages(messagesList);
        }

        @Override
        public void onError(int code, String error) {
            LogUtils.d("huan", error);
        }

        @Override
        public void onProgress(int progress, String status) {

        }
    };

    private String trans(long n) {
        if (n < 10) {
            return "0" + n;
        } else {
            return n + "";
        }
    }
    class MyUploadListener implements ProgressListener {
        @Override
        public void onProgress(long progress, long total, boolean done) {
        }
    }
}
