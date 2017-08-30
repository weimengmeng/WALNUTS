package com.njjd.walnuts;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
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
import com.njjd.utils.AndroidBug5497Workaround;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.ToastUtils;
import com.voice.AudioRecoderUtils;
import com.voice.PopupWindowFactory;
import com.voice.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;

/**
 * Created by mrwim on 17/7/12.
 */

public class ChatActivity extends BaseActivity implements TextView.OnEditorActionListener{
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
    @BindView(R.id.lv_root)
    LinearLayout lvRoot;
    @BindView(R.id.btn_voice)
    ImageView btnVoice;
    private MSGLAdapter adapter;
    private List<EMMessage> messagesList;
    private String imagePath = "", tempFilePath = "";
    private int length = 0;
    private AudioRecoderUtils mAudioRecoderUtils;
    private PopupWindowFactory mPop;
    private ImageView mImageView;
    private TextView mTextView;
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
        etContent.setOnEditorActionListener(this);
        ImmersedStatusbarUtils.initAfterSetContentView(this, findViewById(R.id.top));
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(getIntent().getStringExtra("openId"));
        if (conversation == null) {
            messagesList = new ArrayList<>();
        } else {
            messagesList = conversation.getAllMessages();
            conversation.markAllMessagesAsRead();
        }
        adapter = new MSGLAdapter(this, messagesList);
        adapter.setAvatar(getIntent().getStringExtra("avatar"));
        listChat.setAdapter(adapter);
        handler.sendEmptyMessage(0);
        initConversionLitener();
        AndroidBug5497Workaround.assistActivity(this);
        mAudioRecoderUtils = new AudioRecoderUtils();
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
                length=(int)time/1000;
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                mTextView.setText(TimeUtils.long2String(0));
                tempFilePath=filePath;
                if(length<1){
                    ToastUtils.showShortToast(ChatActivity.this,"语音时间过短");
                    return;
                }
                sendVoice();
            }
        });

        //PopupWindow的布局文件
        final View view1 = View.inflate(this, R.layout.layout_microphone, null);

        mPop = new PopupWindowFactory(this,view1);

        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view1.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view1.findViewById(R.id.tv_recording_time);
        //Button的touch监听
        btnVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:

                        mPop.showAtLocation(lvRoot, Gravity.CENTER,0,0);
                        mAudioRecoderUtils.startRecord();
                        break;

                    case MotionEvent.ACTION_UP:

                        mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
//                        mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
                        mPop.dismiss();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initConversionLitener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                if (messages.get(0).getFrom().equals(getIntent().getStringExtra("openId"))) {
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

    @OnClick({R.id.back, R.id.btn_micro, R.id.btn_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_micro:
             lvVoice.setVisibility(lvVoice.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                break;
            case R.id.btn_img:
                Picker.from(this)
                        .count(1)
                        .enableCamera(true)
                        .setEngine(new GlideEngine())
                        .forResult(REQUEST_CODE);
//                //1多选 2 单选 单选才有裁剪功能
//                ImageSelectorActivity.start(this, 1, 2, true, true, true);
                break;
        }
    }

    private void sendTextMessage() {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(etContent.getText().toString().trim(), getIntent().getStringExtra("openId"));
        EMClient.getInstance().chatManager().sendMessage(message);
        messagesList.add(message);
        message.setMessageStatusCallback(emCallBack);
        etContent.setText("");
    }

    private void sendImageMessage() {
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, getIntent().getStringExtra("openId"));
        EMClient.getInstance().chatManager().sendMessage(message);
        messagesList.add(message);
        message.setMessageStatusCallback(emCallBack);
    }

    private void sendVoice() {
        //filePath为语音文件路径，length为录音时间(秒)
        EMMessage message = EMMessage.createVoiceSendMessage(tempFilePath, length, getIntent().getStringExtra("openId"));
        EMClient.getInstance().chatManager().sendMessage(message);
        messagesList.add(message);
        message.setMessageStatusCallback(emCallBack);
    }

    private EMCallBack emCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            handler.sendEmptyMessage(0);
            LogUtils.d("huanxin success");
        }

        @Override
        public void onError(int code, String error) {
            LogUtils.d("huanxin" + error.toString());
        }

        @Override
        public void onProgress(int progress, String status) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
//            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            imagePath = CommonUtils.getRealPathFromUri(this,mSelected.get(0));
            sendImageMessage();
        }
    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(etContent.getText().toString().trim().equals("")){
            ToastUtils.showShortToast(this,"请输入发送内容");
            return true;
        }
        sendTextMessage();
        return true;
    }
}
