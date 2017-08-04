package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.StringUtil;
import com.njjd.http.HttpManager;
import com.njjd.utils.BasePopupWindow;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.TimeCountDown;
import com.njjd.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/8/4.
 */

public class ForgetPwdActivity extends BaseActivity implements TimeCountDown.OnTimerCountDownListener {
    @BindView(R.id.img_back)
    LinearLayout imgBack;
    @BindView(R.id.et_phone)
    EditText etPhone;
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
        return R.layout.activity_forgetpwd;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView(this, imgBack);
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
                web.loadUrl(HttpManager.BASE_URL + "getVerify?phone=" + etPhone.getText().toString().trim());
                LogUtils.d("点击一次");
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
                    getPhoneCode();
                    etVerify.setText("");
                }
            }
        });
        btnGetCode.setOnTimerCountDownListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.btn_get_code, R.id.img_back})
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
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                web.loadUrl(HttpManager.BASE_URL + "getVerify?phone=" + etPhone.getText().toString().trim());
                break;
        }
    }

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
        btnGetCode.initTimer();
        ToastUtils.showShortToast(this, "已发送");
        Intent intent = new Intent(this, ChangePwdActivity.class);
        intent.putExtra("phone", etPhone.getText().toString().trim());
        startActivity(intent);
        finish();
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
    }
}
