package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.njjd.utils.ImmersedStatusbarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/14.
 */

public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;

    @Override
    public int bindLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView(View view) {
        txtTitle.setText("帮助反馈");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersedStatusbarUtils.initAfterSetContentView(this,topView);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
