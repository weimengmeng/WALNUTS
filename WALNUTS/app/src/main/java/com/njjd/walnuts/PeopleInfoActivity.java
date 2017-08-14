package com.njjd.walnuts;

import android.view.View;

/**
 * Created by mrwim on 17/7/12.\
 * 查看其他用户信息必须参数用户id
 */

public class PeopleInfoActivity extends BaseActivity {
    @Override
    public int bindLayout() {
        return R.layout.activity_people;
    }

    @Override
    public void initView(View view) {
        getUserInfo();
    }
    private void getUserInfo(){

    }
}
