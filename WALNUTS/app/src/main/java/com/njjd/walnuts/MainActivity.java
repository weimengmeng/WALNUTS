package com.njjd.walnuts;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RadioButton;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.njjd.application.AppAplication;
import com.njjd.application.ConstantsVal;
import com.njjd.utils.LogUtils;
import com.njjd.utils.MyActivityManager;
import com.njjd.utils.NotificationUtils;
import com.njjd.utils.TipButton;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author MrWim
 * @deprecated Created  on 17/7/12.
 */
public class MainActivity extends FragmentActivity {

    @BindView(R.id.radio4)
    TipButton radio4;
    private FragmentManager fm;
    private Fragment indexFragment, findFragment, messFragment, mineFragment, pubFragment;
    private int temp = 0;
    public static Activity activity;
    private MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        ButterKnife.bind(this);
        initView();
        MyActivityManager.getInstance().pushOneActivity(this);
    }

    private void initView() {
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsVal.NEW_INFORM);
        registerReceiver(receiver, filter);
        fm = getSupportFragmentManager();
        indexFragment = fm.findFragmentById(R.id.index_fragment);
        findFragment = fm.findFragmentById(R.id.find_fragment);
        messFragment = fm.findFragmentById(R.id.mess_fragment);
        pubFragment = fm.findFragmentById(R.id.pub_fragment);
        mineFragment = fm.findFragmentById(R.id.my_fragment);
        fm.beginTransaction().hide(findFragment).hide(messFragment).hide(mineFragment).hide(pubFragment)
                .show(indexFragment).commit();
        initConversionLitener();
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
                EMClient.getInstance().chatManager().importMessages(messages);
                EMClient.getInstance().chatManager().loadAllConversations();
                Intent intent = new Intent();
                intent.setAction(ConstantsVal.MESSAGE_RECEIVE);
                sendBroadcast(intent);
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
        unregisterReceiver(receiver);
        MyActivityManager.getInstance().popOneActivity(this);
    }

    @OnClick({R.id.radio1, R.id.radio2, R.id.radio3, R.id.radio4, R.id.radio5})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.radio1:
                fm.beginTransaction().hide(findFragment).hide(messFragment).hide(mineFragment).hide(pubFragment)
                        .show(indexFragment)
                        .commitAllowingStateLoss();
                temp = 0;
                break;
            case R.id.radio2:
                fm.beginTransaction().hide(indexFragment).hide(messFragment).hide(mineFragment).hide(pubFragment)
                        .show(findFragment)
                        .commitAllowingStateLoss();
                temp = 1;
                break;
            case R.id.radio3:
                intent = new Intent(this, AskActivity.class);
                startActivity(intent);
                break;
            case R.id.radio4:
                fm.beginTransaction().hide(findFragment).hide(indexFragment).hide(mineFragment).hide(pubFragment)
                        .show(messFragment)
                        .commitAllowingStateLoss();
                temp = 2;
                radio4.setTipOn(false);
                radio4.invalidate();
                break;
            case R.id.radio5:
                fm.beginTransaction().hide(findFragment).hide(messFragment).hide(indexFragment).hide(pubFragment)
                        .show(mineFragment)
                        .commitAllowingStateLoss();
                temp = 3;
                break;
        }
        if (view.getId() == R.id.radio3) {
            switch (temp) {
                case 0:
                    ((RadioButton) findViewById(R.id.radio1)).setChecked(true);
                    break;
                case 1:
                    ((RadioButton) findViewById(R.id.radio2)).setChecked(true);
                    break;
                case 2:
                    ((RadioButton) findViewById(R.id.radio4)).setChecked(true);
                    break;
                case 3:
                    ((RadioButton) findViewById(R.id.radio5)).setChecked(true);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.getSupportFragmentManager().findFragmentByTag("mine").onActivityResult(requestCode, resultCode, data);
    }

    private class MyReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            radio4.setTipOn(true);
            radio4.invalidate();
        }
    }
}
