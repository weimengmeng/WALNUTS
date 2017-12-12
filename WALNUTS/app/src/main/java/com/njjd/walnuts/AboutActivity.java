package com.njjd.walnuts;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.mywidget.LoadingDialog;
import com.ios.dialog.AlertDialog;
import com.njjd.utils.MyActivityManager;
import com.njjd.utils.ToastUtils;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_version)
    TextView txtVersion;
    @BindView(R.id.top)
    LinearLayout topView;
    @Override
    public int bindLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void initView(View view) {
        back.setText("设置");
        txtTitle.setText("关于核桃");
        txtVersion.setText(getLocalVersionName(this) + "版");
    }
    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.lv_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.lv_update:
                checkUpdate();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unregister();
    }

    private void checkUpdate() {
        PgyUpdateManager.register(AboutActivity.this,
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {
                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        if (appBean.getVersionName().equals(getLocalVersionName(AboutActivity.this))) {
                            return;
                        }
                        AlertDialog dialog = new AlertDialog(AboutActivity.this).builder();
                        dialog.setTitle("更新提示").setCancelable(false).setPositiveButton("立即更新", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startDownloadTask(
                                        AboutActivity.this,
                                        appBean.getDownloadURL());
                            }
                        });
                        dialog.setMsg(appBean.getReleaseNote()).setNegativeButton("忽略", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        dialog.show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        ToastUtils.showShortToast(getBaseContext(), "当前已是最新版本");
                    }
                });
    }
}
