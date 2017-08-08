package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.njjd.utils.ImmersedStatusbarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/8/7.
 */

public class MyFocusActivity extends BaseActivity {
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
        txtTitle.setText("我的关注");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back,R.id.radio_one, R.id.radio_two,R.id.radio_three})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.radio_one:
                break;
            case R.id.radio_two:
                break;
            case R.id.radio_three:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
