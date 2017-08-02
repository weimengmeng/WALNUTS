package com.njjd.walnuts;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.JsonObject;
import com.njjd.http.HttpManager;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.handler.UMSSOHandler;
import com.umeng.socialize.handler.UMWXHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private UMShareAPI umShareAPI;
    private UMAuthListener authListener;
    private Map<String, String> thirdMap;

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(View view) {
        umShareAPI = UMShareAPI.get(this);

        authListener = new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                ToastUtils.showShortToast(LoginActivity.this, "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                ToastUtils.showShortToast(LoginActivity.this, "授权成功");
                LogUtils.d(map.toString());
                thirdMap = map;
                doThirdLogin((share_media == SHARE_MEDIA.QQ ? 1 : share_media == SHARE_MEDIA.WEIXIN ? 2 : 3), map);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                LogUtils.d(throwable.toString() + "--------->" + i);
                ToastUtils.showShortToast(LoginActivity.this, "授权错误");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                ToastUtils.showShortToast(LoginActivity.this, "授权取消");
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.txt_misspwd, R.id.txt_register, R.id.btn_close, R.id.btn_login, R.id.btn_sina, R.id.btn_wx, R.id.btn_qq})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.txt_misspwd:
                intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("isFind",1);
                startActivity(intent);
                break;
            case R.id.txt_register:
                intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("bind",0);
                intent.putExtra("isFind",0);
                startActivity(intent);
                break;
            case R.id.btn_close:
                break;
            case R.id.btn_login:
                if(etPhone.getText().toString().equals("")||etPwd.getText().toString().equals("")){
                    ToastUtils.showShortToast(this,"请输入账号和密码");
                    return;
                }
                doLogin();
//                intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
                break;
            case R.id.btn_sina:
                umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, authListener);
                break;
            case R.id.btn_wx:
                umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                break;
            case R.id.btn_qq:
                umShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
                break;
        }
    }

    private void doLogin() {
        Map<String, String> map = new HashMap<>();
        map.put("phone", etPhone.getText().toString().trim());
        map.put("pwd", etPwd.getText().toString().trim());
        map.put("device_token", SPUtils.get(this, "deviceToken", "").toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), map);
        HttpManager.getInstance().userLogin(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        JsonObject json = JSONUtils.getAsJsonObject(o);
        LogUtils.d(json.toString());

        // introduction=null,
        // token=0e181cfaef7b59f6cd4df09190250dd3,, stat=1.0,
        SPUtils.put(LoginActivity.this, "userId", json.get("uid").getAsString());
        SPUtils.put(LoginActivity.this, "head", json.get("headimg").getAsString());
        SPUtils.put(LoginActivity.this, "name", json.get("uname").getAsString());
        SPUtils.put(LoginActivity.this, "province", json.get("province").getAsString());
        SPUtils.put(LoginActivity.this, "city", json.get("city").getAsString());
//        SPUtils.put(LoginActivity.this, "company", json.get("company").getAsString());
        SPUtils.put(LoginActivity.this, "position", json.get("position").getAsString());
        SPUtils.put(LoginActivity.this, "industry", json.get("industry").getAsString());
        SPUtils.put(LoginActivity.this, "token", json.get("token").getAsString());
        SPUtils.put(LoginActivity.this, "message", json.get("introduction").getAsString());
        SPUtils.put(LoginActivity.this, "isBind", json.get("idBind").getAsString());
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
//        finish();
        }

    private void doThirdLogin(int type, Map<String, String> result) {
        //0 qq 1 wechat  2 sina
        //{uid=353DD23F3BCED8DD283BD5104EDDD1A5, ret=0, is_yellow_year_vip=0, accessToken=0708D873B69FDF530B2F06534CFCA9B8, expiration=1508982165007, unionid=, yellow_vip_level=0, expires_in=1508982165007, iconurl=http://q.qlogo.cn/qqapp/1106091328/353DD23F3BCED8DD283BD5104EDDD1A5/100, msg=, city=南京, vip=0, level=0, name=奋斗的青年, province=江苏, is_yellow_vip=0, gender=男, openid=353DD23F3BCED8DD283BD5104EDDD1A5, screen_name=奋斗的青年, profile_image_url=http://q.qlogo.cn/qqapp/1106091328/353DD23F3BCED8DD283BD5104EDDD1A5/100, access_token=0708D873B69FDF530B2F06534CFCA9B8}
        Map<String, String> map = new HashMap<>();
        map.put("logintype", "" + type);
        map.put("uuid", result.get("uid"));
        map.put("device_token", SPUtils.get(this, "deviceToken", "").toString());
        LogUtils.d("---------->"+map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(thirdLoginListener, this, true, false), map);
        HttpManager.getInstance().thirdLogin(postEntity);
    }

    HttpOnNextListener thirdLoginListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            Intent intent;
            JsonObject json = JSONUtils.getAsJsonObject(o);
            if(json.toString().contains("uuid")){
                //用户未绑定
                SPUtils.put(LoginActivity.this, "uuid", json.get("uuid").getAsString());
                SPUtils.put(LoginActivity.this, "logintype", json.get("logintype").getAsString());
                SPUtils.put(LoginActivity.this, "thirdName", thirdMap.get("name"));
                SPUtils.put(LoginActivity.this, "thirdSex", thirdMap.get("gender"));
                SPUtils.put(LoginActivity.this, "thirdHead", thirdMap.get("iconurl"));
                SPUtils.put(LoginActivity.this, "thirdCity", thirdMap.get("city"));
                SPUtils.put(LoginActivity.this, "thirdProvince", thirdMap.get("province"));
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("bind",1);
                startActivity(intent);
                ToastUtils.showShortToast(LoginActivity.this,"请先绑定手机号");
            } else {
                SPUtils.put(LoginActivity.this, "head", json.get("headimg").getAsString());
                SPUtils.put(LoginActivity.this, "name", json.get("uname").getAsString());
                SPUtils.put(LoginActivity.this, "province", json.get("province").getAsString());
                SPUtils.put(LoginActivity.this, "city", json.get("city").getAsString());
                SPUtils.put(LoginActivity.this, "company", json.get("company").getAsString());
                SPUtils.put(LoginActivity.this, "position", json.get("position").getAsString());
                SPUtils.put(LoginActivity.this, "industry", json.get("industry").getAsString());
                SPUtils.put(LoginActivity.this, "token", json.get("token").getAsString());
                SPUtils.put(LoginActivity.this, "message", json.get("introduction").getAsString());
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
