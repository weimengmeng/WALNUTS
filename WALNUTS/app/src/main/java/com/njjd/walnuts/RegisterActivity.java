package com.njjd.walnuts;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.example.retrofit.util.StringUtil;
import com.google.gson.JsonObject;
import com.njjd.application.ConstantsVal;
import com.njjd.http.HttpManager;
import com.njjd.utils.BasePopupWindow;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.KeybordS;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.TimeCountDown;
import com.njjd.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class RegisterActivity extends BaseActivity implements TimeCountDown.OnTimerCountDownListener {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_invite_code)
    EditText etInviteCode;
    @BindView(R.id.img_back)
    LinearLayout imgBack;
    @BindView(R.id.txt_tips)
    TextView txtTips;
    @BindView(R.id.btn_get_code)
    TimeCountDown btnGetCode;
    private EditText etVerify;
    private WebView web;
    View lvImgcode;
    private ImageView imageView;
    BasePopupWindow popupWindow;
    LayoutInflater inflater;
    private String code = "";

    @Override
    public int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(View view) {
        btnGetCode.setText("获取验证码");
        inflater = LayoutInflater.from(this);
        lvImgcode = inflater.inflate(R.layout.lay_code, null);
        popupWindow = new BasePopupWindow(this);
        popupWindow.setContentView(lvImgcode);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        etVerify = (EditText) lvImgcode.findViewById(R.id.et_verify);
        web = (WebView) lvImgcode.findViewById(R.id.web);
        imageView = (ImageView) lvImgcode.findViewById(R.id.btn_resend);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.loadUrl(HttpManager.BASE_URL + "user/getVerify?phone=" + etPhone.getText().toString().trim());
            }
        });
        etVerify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    code = etVerify.getText().toString().trim();
                    popupWindow.dismiss();
                    etVerify.setText("");
                    //判断是否邀请
                    checkInvitation();
                }
            }
        });
        btnGetCode.setOnTimerCountDownListener(this);
    }
    private void checkInvitation(){
        Map<String, Object> map = new HashMap<>();
        map.put("invitation_code", etInviteCode.getText().toString().trim());
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(checkInvitation, this, true, false), map);
        HttpManager.getInstance().checkInvitation(postEntity);
    }
    HttpOnNextListener checkInvitation = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonObject object=JSONUtils.getAsJsonObject(o);
            if(object.get("code").getAsString().equals("1.0")){
                ToastUtils.showShortToast(RegisterActivity.this,"邀请码错误");
            }else{
                getPhoneCode();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.btn_get_code, R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_code:
                if (!StringUtil.isPhoneNumber(etPhone.getText().toString().trim())) {
                    ToastUtils.showShortToast(this, "请输入正确的手机号");
                    return;
                }
                if (etInviteCode.getText().toString().trim().equals("")) {
                    ToastUtils.showShortToast(RegisterActivity.this, "请输入邀请码");
                    return;
                }
                KeybordS.closeBoard(this);
                MobclickAgent.onEvent(this, ConstantsVal.GETPHONECODE);
                isUserExist();
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void isUserExist(){
        Map<String, Object> map = new HashMap<>();
        map.put("phone", etPhone.getText().toString().trim());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                JsonObject object=JSONUtils.getAsJsonObject(o);
                if(object.get("code").getAsString().equals("1.0")){
                   show();
                }else{
                    ToastUtils.showShortToast(RegisterActivity.this,"手机号已注册");
                }
            }
        }, this, true, false), map);
        HttpManager.getInstance().isExistUser(postEntity);
    }
    private void show(){
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        web.loadUrl(HttpManager.BASE_URL + "user/getVerify?phone=" + etPhone.getText().toString().trim());
    }

    private void getPhoneCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", etPhone.getText().toString().trim());
        map.put("imgcode", code);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), map);
        HttpManager.getInstance().phoneCode(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        JsonObject object = JSONUtils.getAsJsonObject(o);
        if (object.get("code").getAsString().equals("1.0")) {
            ToastUtils.showShortToast(this, "手机号已注册");
        } else {
            btnGetCode.initTimer();
            MobclickAgent.onEvent(RegisterActivity.this,ConstantsVal.REGIST_NEXT);
            ToastUtils.showShortToast(this, "已发送");
            Intent intent = new Intent(this, SetPasswordActivity.class);
            intent.putExtra("phone", etPhone.getText().toString().trim());
            intent.putExtra("imgcode", code);
            intent.putExtra("bind",0);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnGetCode.cancel();
    }

    @Override
    public void onCountDownStart() {
        btnGetCode.setEnabled(false);
        btnGetCode.setTextColor(getResources().getColor(R.color.txt_color));
        etPhone.setEnabled(false);
    }

    @Override
    public void onCountDownLoading(int currentCount) {

    }

    @Override
    public void onCountDownError() {

    }

    @Override
    public void onCountDownFinish() {
        btnGetCode.setEnabled(true);
        etPhone.setEnabled(true);
        btnGetCode.setTextColor(getResources().getColor(R.color.login));
        btnGetCode.setText("获取验证码");
    }
}
