package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.njjd.utils.ToastUtils;

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

    @Override
    public int bindLayout() {
        return R.layout.activity_people;
    }

    @Override
    public void initView(View view) {
        getUserInfo();
    }

    private void getUserInfo() {

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
