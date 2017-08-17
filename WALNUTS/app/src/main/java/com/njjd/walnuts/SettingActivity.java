package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.retrofit.mywidget.LoadingDialog;
import com.ios.dialog.AlertDialog;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.utils.CleanMessageUtil;
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
    @BindView(R.id.txt_cache)
    TextView txtCache;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.lv_back)
    SwipeBackLayout lvBack;
    private LoadingDialog loadingDialog;
    @Override
    public int bindLayout() {
        return R.layout.activity_setting;
    }
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {

                case 0:
                    ToastUtils.showShortToast(SettingActivity.this,"清理完成");
                    try {

                        txtCache.setText(CleanMessageUtil.getTotalCacheSize(SettingActivity.this));

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

            }

        };

    };
    @Override
    public void initView(View view) {
        back.setText("我的");
        txtTitle.setText("设置");
        try {
            txtCache.setText(CleanMessageUtil.getTotalCacheSize(this)+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadingDialog=new LoadingDialog(this);
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
                new AlertDialog(this).builder().setCancelable(false).setMsg("清理缓存后数据都会清空,确定清理吗").setTitle("提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        new Thread(new clearCache()).start();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
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
    class clearCache implements Runnable {

        @Override

        public void run() {
            try {
                CleanMessageUtil.clearAllCache(SettingActivity.this);
                Thread.sleep(3000);
                if ((CleanMessageUtil.getTotalCacheSize(SettingActivity.this)+"").startsWith("0")) {
                    handler.sendEmptyMessage(0);
                }
                loadingDialog.dismiss();
            } catch (Exception e) {
                return;
            }

        }

    }
}
