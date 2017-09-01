package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.JsonObject;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.TimeCountDown;
import com.njjd.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/26.
 */

public class SetPasswordActivity extends BaseActivity implements TimeCountDown.OnTimerCountDownListener{
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.img_back)
    LinearLayout imgBack;
    @BindView(R.id.txt_tip)
    TextView txtTip;
    @BindView(R.id.btn_resend)
    TimeCountDown btnResend;
    private String temp;

    @Override
    public int bindLayout() {
        return R.layout.activity_setpassword;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView2(this, imgBack);
        temp = getResources().getString(R.string.code);
        temp = String.format(temp, getIntent().getStringExtra("phone"));
        txtTip.setText(temp);
        txtTip.setVisibility(View.VISIBLE);
        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void userRegister() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", getIntent().getStringExtra("phone"));
        map.put("pwd", etPwd.getText().toString().trim());
        map.put("device_token", SPUtils.get(this, "deviceToken", "").toString());
        map.put("code", etCode.getText().toString().trim());
        map.put("register_type", 1 + "");
        if (getIntent().getIntExtra("bind", 0) == 1) {
            map.put("uuid", SPUtils.get(this, "uuid", "").toString());
            map.put("logintype", SPUtils.get(this, "logintype", "").toString());
        }
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, false, false), map);
        HttpManager.getInstance().userRegister(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        JsonObject object = JSONUtils.getAsJsonObject(o);
        SPUtils.put(this, "phoneNumber", getIntent().getStringExtra("phone"));
        SPUtils.put(this, "userId", object.get("uid").getAsString());
        SPUtils.put(this, "token", object.get("token").getAsString());
        SPUtils.put(this, "pwd", etPwd.getText().toString().trim());
        Intent intent = new Intent(this, SuccessActivity.class);
        intent.putExtra("bind", getIntent().getIntExtra("bind", 0));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.img_back, R.id.btn_next,R.id.btn_resend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_resend:
               getPhoneCode();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_next:
                if (etPwd.getText().toString().trim().equals("") || etPwd.getText().toString().trim().length() < 6) {
                    ToastUtils.showShortToast(this, "请输入六位以上的密码");
                    return;
                }
                if (etCode.getText().toString().trim().equals("")) {
                    ToastUtils.showShortToast(this, "请输入短信验证码");
                    return;
                }
                if (!etConfirmPwd.getText().toString().trim().equals(etPwd.getText().toString().trim())) {
                    ToastUtils.showShortToast(this, "密码不一致");
                    return;
                }
                verifyPhone();
//                Intent intent = new Intent(this, SuccessActivity.class);
//                startActivity(intent);
                break;
        }
    }

    private void verifyPhone() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", getIntent().getStringExtra("phone"));
        map.put("code", etCode.getText().toString().trim());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(verifyPhoneListener, this, false, false), map);
        HttpManager.getInstance().verifyPhone(postEntity);
    }

    HttpOnNextListener verifyPhoneListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            LogUtils.d(o.toString());
            SPUtils.put(SetPasswordActivity.this, "phoneNumber", getIntent().getStringExtra("phone"));
            userRegister();
        }
    };
    private void getPhoneCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", getIntent().getStringExtra("phone"));
        map.put("imgcode",getIntent().getStringExtra("imgcode"));
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getPhoneListener, this, true, false), map);
        HttpManager.getInstance().phoneCode(postEntity);
    }
    HttpOnNextListener getPhoneListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonObject object = JSONUtils.getAsJsonObject(o);
            if (object.get("code").getAsString().equals("1.0")) {
                ToastUtils.showShortToast(SetPasswordActivity.this, "手机号已注册");
            } else {
                btnResend.initTimer();
            }
        }
    };
    @Override
    public void onCountDownStart() {
        btnResend.setEnabled(false);
        btnResend.setTextColor(getResources().getColor(R.color.txt_color));
    }

    @Override
    public void onCountDownLoading(int currentCount) {

    }

    @Override
    public void onCountDownError() {

    }

    @Override
    public void onCountDownFinish() {
        btnResend.setEnabled(true);
        btnResend.setTextColor(getResources().getColor(R.color.login));
    }
}
