package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.adapter.MySaveAdapter;
import com.njjd.http.HttpManager;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.\
 * 查看其他用户信息必须参数用户id
 */

public class PeopleInfoActivity extends BaseActivity {
    @BindView(R.id.txt_add_focus)
    TextView txtAddFocus;
    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_focusNum)
    TextView txtFocusNum;
    @BindView(R.id.txt_focusedNum)
    TextView txtFocusedNum;
    private String tempUser="";
    @Override
    public int bindLayout() {
        return R.layout.activity_people;
    }

    @Override
    public void initView(View view) {
        tempUser=getIntent().getStringExtra("uid");
        getUserInfo();
    }

    private void getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("ouid",tempUser);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, false, false), map);
        HttpManager.getInstance().getUidSave(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.txt_back, R.id.txt_add_focus, R.id.ll_ques, R.id.ll_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_back:
                finish();
                break;
            case R.id.txt_add_focus:
                ToastUtils.showShortToast(this,"关注他或私信他");
                break;
            case R.id.ll_ques:
                ToastUtils.showShortToast(this,"他的问题");
                break;
            case R.id.ll_answer:
                ToastUtils.showShortToast(this,"他的回答");
                break;
        }
    }
}
