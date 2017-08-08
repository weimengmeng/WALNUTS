package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class AnswerActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.et_answer)
    EditText etAnswer;

    @Override
    public int bindLayout() {
        return R.layout.activity_answer;
    }

    @Override
    public void initView(View view) {
        back.setText("详情");
        txtTitle.setText("回答");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
    }

    @OnClick({R.id.back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_submit:
                ToastUtils.showShortToast(this, "提交回答");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
