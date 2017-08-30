package com.njjd.walnuts;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.njjd.utils.SPUtils;

/**
 * Created by mrwim on 17/7/18.
 */

public class WelcomeActivity extends BaseActivity{
    @Override
    public void initView(View view) {
        if(SPUtils.get(this,"isFirst","0").equals("0")){
            SPUtils.put(this,"isFirst","1");
            startActivity(new Intent(this,AppIntroActivity.class));
            finish();
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }
            }, 3000);
        }
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_welcome;
    }
}
