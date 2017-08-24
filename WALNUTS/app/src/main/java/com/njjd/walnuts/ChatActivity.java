package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.njjd.adapter.MSGLAdapter;
import com.njjd.application.AppAplication;
import com.njjd.utils.AndroidBug5497Workaround;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.NotificationUtils;
import com.njjd.utils.ToastUtils;
import com.yongchun.library.view.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class ChatActivity extends BaseActivity {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.list_chat)
    ListView listChat;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.lv_voice)
    LinearLayout lvVoice;
    @BindView(R.id.btn_img)
    ImageView btnImg;
    @BindView(R.id.btn_send)
    Button btnSend;
    private MSGLAdapter adapter;
    private List<EMMessage> messagesList;
    private String imagePath = "";

    @Override
    public int bindLayout() {
        return R.layout.activity_chat;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
            listChat.smoothScrollToPosition(
                    messagesList.size() - 1);
        }
    };

    @Override
    public void initView(View view) {
        txtTitle.setText(getIntent().getStringExtra("name"));
        ImmersedStatusbarUtils.initAfterSetContentView(this,findViewById(R.id.top));
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(getIntent().getStringExtra("name"));
        if (conversation == null) {
            messagesList = new ArrayList<>();
        } else {
            messagesList = conversation.getAllMessages();
        }
        adapter = new MSGLAdapter(this, messagesList);
        listChat.setAdapter(adapter);
        listChat.smoothScrollToPosition(
                messagesList.size() - 1);
        initConversionLitener();
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etContent.getText().toString().trim().length() <= 0) {
                    btnImg.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.GONE);
                }else{
                    btnImg.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                }
            }
        });
        AndroidBug5497Workaround.assistActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initConversionLitener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                LogUtils.d(messages.get(0).getBody());
                if (AppAplication.isApplicationBroughtToBackground(AppAplication.getContext())) {
                    Intent intent = new Intent(AppAplication.getContext(), ChatActivity.class);
                    intent.putExtra("name", messages.get(0).getFrom());
                    NotificationUtils.createNotif(AppAplication.getContext(), R.drawable.logo, "", messages.get(0).getFrom(), messages.get(0).getBody().toString(), intent, 1);
                }
                if (messages.get(0).getFrom().equals(getIntent().getStringExtra("name"))) {
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

    @OnClick({R.id.back, R.id.btn_micro, R.id.btn_img,R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_send:
                sendTextMessage();
                break;
            case R.id.btn_micro:
                ToastUtils.showShortToast(this, "发送语音");
                break;
            case R.id.btn_img:
                //1多选 2 单选 单选才有裁剪功能
                ImageSelectorActivity.start(this, 1, 2, true, true, true);
                break;
        }
    }

    private void sendTextMessage() {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(etContent.getText().toString().trim(), getIntent().getStringExtra("name"));
        message.setAttribute("head", "http://192.168.1.112/hetao_api/public/uploads/482015018/headImg/599692475018f.JPEG");
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(emCallBack);
    }

    private void sendImageMessage() {
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, getIntent().getStringExtra("name"));
        message.setAttribute("head", "http://192.168.1.112/hetao_api/public/uploads/482015018/headImg/599692475018f.JPEG");
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(emCallBack);
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(getIntent().getStringExtra("name"));
        conversation.appendMessage(message);
    }

    private EMCallBack emCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            handler.sendEmptyMessage(0);
        }

        @Override
        public void onError(int code, String error) {

        }

        @Override
        public void onProgress(int progress, String status) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            imagePath = images.get(0);
            sendImageMessage();
        }
    }
}
