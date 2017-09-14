package com.njjd.walnuts;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.View;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njjd.application.ConstantsVal;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrwim on 17/7/18.
 */

public class WelcomeActivity extends BaseActivity{
    @Override
    public void initView(View view) {
        if(SPUtils.get(this,ConstantsVal.AUTOLOGIN,"false").equals("true")){
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }
            }, 3000);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
    }
    @Override
    public int bindLayout() {
        return R.layout.activity_welcome;
    }
}
