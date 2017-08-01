package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/13.
 */

public class PersonalActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_sex)
    TextView txtSex;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.txt_province)
    TextView txtProvince;
    @BindView(R.id.txt_city)
    TextView txtCity;
    @BindView(R.id.txt_position)
    TextView txtPosition;
    @BindView(R.id.txt_company)
    TextView txtCompany;
    @BindView(R.id.btn_add_help)
    TextView btnAddHelp;
    @BindView(R.id.lv_back)
    SwipeBackLayout lvBack;

    @Override
    public int bindLayout() {
        return R.layout.activity_personal;
    }

    @Override
    public void initView(View view) {
        back.setText("我的");
        txtTitle.setText("个人资料");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersedStatusbarUtils.initAfterSetContentView(this, top);
        btnAddHelp.setVisibility(View.VISIBLE);
        lvBack.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }

    @OnClick({R.id.back,R.id.btn_add_help})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_add_help:
                ToastUtils.showShortToast(this,"修改信息");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
