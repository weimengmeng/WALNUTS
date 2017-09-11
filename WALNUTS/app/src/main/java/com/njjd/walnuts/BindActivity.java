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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.example.retrofit.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.njjd.http.HttpManager;
import com.njjd.utils.BasePopupWindow;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.MyActivityManager;
import com.njjd.utils.SPUtils;
import com.njjd.utils.TimeCountDown;
import com.njjd.utils.TimeCountDown2;
import com.njjd.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/8/4.
 */

public class BindActivity extends BaseActivity {
    @BindView(R.id.img_back)
    LinearLayout imgBack;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_phone_code)
    EditText etPhoneCode;
    @BindView(R.id.et_invite_code)
    EditText etInviteCode;
    @BindView(R.id.lv_invite)
    LinearLayout lvInvite;
    @BindView(R.id.lv_phonecode)
    LinearLayout lvPhone;
    @BindView(R.id.btn_get_code)
    TimeCountDown2 btnGetCode;
    @BindView(R.id.txt_tip)
    TextView txtTip;
    private EditText etVerify;
    private WebView web;
    View lvImgcode;
    private ImageView imageView;
    BasePopupWindow popupWindow;
    LayoutInflater inflater;
    String code = "";
    private String temp;

    @Override
    public int bindLayout() {
        return R.layout.activity_bind;
    }

    @Override
    public void initView(View view) {
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
                    if (lvInvite.getVisibility() == View.VISIBLE) {
                        //判断是否邀请
                        checkInvitation();
                    } else {
                        getPhoneCode();
                    }
                }
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) {
                    checkPhone();
                }
            }
        });
    }

    private void checkPhone() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", etPhone.getText().toString().trim());
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(checkListener, this, false, false), map);
        HttpManager.getInstance().checkPhone(postEntity);
    }

    HttpOnNextListener checkListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonObject object = JSONUtils.getAsJsonObject(o);
            if (object.get("code").getAsString().equals("1.0")) {
                lvInvite.setVisibility(View.GONE);
                btnGetCode.setTag("1");//有账号
            } else {
                lvInvite.setVisibility(View.VISIBLE);
                btnGetCode.setTag("0");//没有账号
            }
        }
    };

    private void checkInvitation() {
        Map<String, Object> map = new HashMap<>();
        map.put("invitation_code", etInviteCode.getText().toString().trim());
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(checkInvitation, this, true, false), map);
        HttpManager.getInstance().checkInvitation(postEntity);
    }

    HttpOnNextListener checkInvitation = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonObject object = JSONUtils.getAsJsonObject(o);
            if (object.get("code").getAsString().equals("")) {
                ToastUtils.showShortToast(BindActivity.this, "邀请码错误");
            } else {
                getPhoneCode();
            }
        }
    };

    private void getPhoneCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", etPhone.getText().toString().trim());
        map.put("imgcode", code);
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), map);
        HttpManager.getInstance().phoneCode(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        if (btnGetCode.getTag().equals("1")) {
            temp = getResources().getString(R.string.code);
            temp = String.format(temp, etPhone.getText().toString().trim());
            txtTip.setText(temp);
            txtTip.setVisibility(View.VISIBLE);
            btnGetCode.setTag("2");
            btnGetCode.setText("完成绑定");
            etPhone.setEnabled(false);
            lvPhone.setVisibility(View.VISIBLE);
        } else {
            btnGetCode.initTimer();
            ToastUtils.showShortToast(this, "已发送");
            Intent intent = new Intent(this, SetPasswordActivity.class);
            intent.putExtra("phone", etPhone.getText().toString().trim());
            intent.putExtra("bind", getIntent().getIntExtra("bind", 0));
            startActivity(intent);
            finish();
        }
    }

    private void authBind() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", SPUtils.get(this, "uuid", "").toString());
        map.put("logintype", SPUtils.get(this, "logintype", "").toString());
        map.put("code", etPhoneCode.getText().toString().trim());
        map.put("phone", etPhone.getText().toString().trim());
        map.put("device_token", SPUtils.get(this, "deviceToken", "").toString());
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(bindListener, this, false, false), map);
        HttpManager.getInstance().authBind(postEntity);
    }

    HttpOnNextListener bindListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            ToastUtils.showShortToast(BindActivity.this, "绑定成功");
            Intent intent;
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls(); //重点
            Gson gson = gsonBuilder.create();
            try {
                CommonUtils.initData(new JSONObject(gson.toJson(o)));
                MyActivityManager.getInstance().popOneActivity(LoginActivity.activity);
                intent = new Intent(BindActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.img_back, R.id.btn_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_get_code:
                if (!StringUtil.isPhoneNumber(etPhone.getText().toString().trim())) {
                    ToastUtils.showShortToast(this, "请输入正确的手机号");
                    return;
                }
                if (btnGetCode.getTag().equals("0")) {
                    //no account
                    if (etInviteCode.getText().toString().trim().equals("")) {
                        ToastUtils.showShortToast(this, "请输入邀请码");
                        return;
                    }
                } else if (btnGetCode.getTag().equals("2")) {
                    if (etPhoneCode.getText().toString().trim().equals("")) {
                        ToastUtils.showShortToast(this, "请输入短信验证码");
                        return;
                    }
                    verifyPhone();
                    return;
                }
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                web.loadUrl(HttpManager.BASE_URL + "user/getVerify?phone=" + etPhone.getText().toString().trim());
                break;
        }
    }

    private void verifyPhone() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", etPhone.getText().toString().trim());
        map.put("code", etPhoneCode.getText().toString().trim());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(verifyPhoneListener, this, false, false), map);
        HttpManager.getInstance().verifyPhone(postEntity);
    }

    HttpOnNextListener verifyPhoneListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            LogUtils.d(o.toString());
            SPUtils.put(BindActivity.this, "phoneNumber", etPhone.getText().toString().trim());
            authBind();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
