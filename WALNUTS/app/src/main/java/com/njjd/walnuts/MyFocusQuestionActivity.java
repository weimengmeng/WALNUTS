package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.njjd.utils.ImmersedStatusbarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/13.
 */

public class MyFocusQuestionActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.list_ques)
    ListView listQues;
    @Override
    public int bindLayout() {
        return R.layout.activity_mfocusques;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView(this,topView);
        back.setText("我的");
        txtTitle.setText("关注问题");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
