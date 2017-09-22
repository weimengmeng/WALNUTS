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
 * Created by mrwim on 17/9/22.
 */

public class SelectQuestionActivity extends BaseActivity {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.listview)
    ListView listview;

    @Override
    public int bindLayout() {
        return R.layout.acitivity_selectquestion;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView(this,top);
        txtTitle.setText(getIntent().getStringExtra("title"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
