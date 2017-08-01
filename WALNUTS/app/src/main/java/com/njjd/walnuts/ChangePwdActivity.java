package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class ChangePwdActivity extends BaseActivity {
    @BindView(R.id.img_back)
    LinearLayout imgBack;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_re_pwd)
    EditText etRePwd;

    @Override
    public int bindLayout() {
        return R.layout.activity_changepwd;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersedStatusbarUtils.initAfterSetContentView(this,imgBack);
    }

    @OnClick({R.id.img_back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit:
                ToastUtils.showShortToast(this,"修改密码");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
