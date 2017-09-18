package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/9/15.
 */

public class SelectAnswerDetailActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.list_comment)
    ListView listComment;

    @Override
    public int bindLayout() {
        return R.layout.activity_selected_detail;
    }

    @Override
    public void initView(View view) {
        txtTitle.setText("精选回答");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back,R.id.txt_save})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.txt_save:
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_open:
                listComment.setVisibility(listComment.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                break;
        }
    }
}
