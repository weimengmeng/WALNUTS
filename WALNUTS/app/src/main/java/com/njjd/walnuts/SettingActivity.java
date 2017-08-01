package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.lv_back)
    SwipeBackLayout lvBack;

    @Override
    public int bindLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(View view) {
        back.setText("我的");
        txtTitle.setText("设置");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lvBack.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
    }

    @OnClick({R.id.back, R.id.lv_clean, R.id.lv_about, R.id.lv_user_agreement, R.id.lv_feedback, R.id.btn_exit})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.lv_clean:
                ToastUtils.showShortToast(this, "清理缓存");
                break;
            case R.id.lv_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.lv_user_agreement:
                intent = new Intent(this, AgreementActivity.class);
                startActivity(intent);
                break;
            case R.id.lv_feedback:
                intent = new Intent(this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_exit:
                ToastUtils.showShortToast(this, "退出登陆");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
