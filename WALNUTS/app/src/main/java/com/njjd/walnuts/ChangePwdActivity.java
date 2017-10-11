package com.njjd.walnuts;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.http.HttpManager;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class ChangePwdActivity extends BaseActivity {
    @BindView(R.id.img_back)
    LinearLayout imgBack;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_re_pwd)
    EditText etRePwd;
    @BindView(R.id.et_code)
    EditText etCode;

    @Override
    public int bindLayout() {
        return R.layout.activity_changepwd;
    }

    @Override
    public void initView(View view) {
//        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
//        etRePwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.img_back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit:
                if (etPwd.getText().toString().equals("") || etRePwd.getText().toString().equals("")) {
                    ToastUtils.showShortToast(this, "请输入新密码");
                    return;
                }
                if (!etPwd.getText().toString().trim().equals(etRePwd.getText().toString().trim())) {
                    ToastUtils.showShortToast(this, "密码不一致");
                    return;
                }
                verifyPhone();
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
            SPUtils.put(ChangePwdActivity.this, "phoneNumber", getIntent().getStringExtra("phone"));
            setNewPwd();
        }
    };

    private void setNewPwd() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", getIntent().getStringExtra("phone"));
        map.put("code", etCode.getText().toString().trim());
        map.put("pwd", etPwd.getText().toString().trim());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), map);
        HttpManager.getInstance().setNewPwd(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        ToastUtils.showShortToast(this, "重置成功");
        SPUtils.put(this,"pwd","");
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
