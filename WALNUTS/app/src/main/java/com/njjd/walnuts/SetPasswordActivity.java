package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.JsonObject;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/26.
 */

public class SetPasswordActivity extends BaseActivity {
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.img_back)
    LinearLayout imgBack;
    private String phoneCode="";
    @Override
    public int bindLayout() {
        return R.layout.activity_setpassword;
    }

    @Override
    public void initView(View view) {

        ImmersedStatusbarUtils.initAfterSetContentView(this, imgBack);
    }

    private void userRegister() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", SPUtils.get(this, "phoneNumber", "").toString());
        map.put("pwd", etPwd.getText().toString().trim());
        map.put("invitation_code", etCode.getText().toString().trim());
        map.put("device_token",SPUtils.get(this,"deviceToken","").toString());
        map.put("code",getIntent().getStringExtra("code"));
        map.put("register_type", 1+"");
        if (getIntent().getIntExtra("bind", 0) == 1) {
            map.put("uuid", SPUtils.get(this,"uuid","").toString());
            map.put("logintye",SPUtils.get(this,"logintype","").toString());
        }
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, false, false), map);
        HttpManager.getInstance().userRegister(postEntity);
    }
    @Override
    public void onNext(Object o) {
        super.onNext(o);
        JsonObject object= JSONUtils.getAsJsonObject(o);
        SPUtils.put(this,"userId",object.get("uid").getAsString());
        Intent intent=new Intent(this,SuccessActivity.class);
        intent.putExtra("bind",getIntent().getIntExtra("bind",0));
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.img_back, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_next:
                if(etPwd.getText().toString().trim().equals("")||etPwd.getText().toString().trim().length()<6){
                    ToastUtils.showShortToast(this,"请输入六位以上的密码");
                    return;
                }
                if(etCode.getText().toString().trim().equals("")){
                    ToastUtils.showShortToast(this,"请输入邀请码");
                    return;
                }
                userRegister();
                break;
        }
    }
}
