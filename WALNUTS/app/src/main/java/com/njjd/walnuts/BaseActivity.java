package com.njjd.walnuts;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.retrofit.listener.HttpOnNextListener;
import com.github.anzewei.parallaxbacklayout.ParallaxBack;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.MyActivityManager;
import com.top_snackbar.BaseTransientBottomBar;
import com.top_snackbar.TopSnackBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import butterknife.ButterKnife;

/**
 * Created by mrwim on 17/7/13.
 */
@ParallaxBack
public abstract class BaseActivity extends AppCompatActivity implements HttpOnNextListener{
    /** 是否沉浸状态栏 **/
    private boolean isSetStatusBar = true;
    /** 当前Activity渲染的视图View **/
    private static View mContextView = null;
    /** 日志输出标志 **/
    protected final String TAG = this.getClass().getSimpleName();
    public static final int REQUEST_CODE = 0; // 请求码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            mContextView = LayoutInflater.from(this)
                    .inflate(bindLayout(), null);
        setContentView(mContextView);
        ButterKnife.bind(this);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.white));
        }
        MyActivityManager.getInstance().pushOneActivity(this);
        initView(mContextView);
        PushAgent.getInstance(this).onAppStart();
        CommonUtils.setMeizuStatusBarDarkIcon(this,true);
        CommonUtils.setMiuiStatusBarDarkMode(this,true);
    }
    @Override
    public void onNext(Object o) {
    }

    /**
     * [绑定布局]
     *
     * @return
     */
    public abstract int bindLayout();

    /**
     * [初始化控件]
     *
     * @param view
     */
    public abstract void initView(final View view);

    /**
     * [绑定控件]
     *
     * @param resId
     *
     * @return
     */
    protected    <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyActivityManager.getInstance().popOneActivity(this);
    }
    /**
     * [简化Toast]
     * @param msg
     */
    public static void showToast(String msg){
        Toast.makeText(mContextView.getContext(),msg,Toast.LENGTH_SHORT).show();
//             TopSnackBar.make(mContextView, msg, BaseTransientBottomBar.LENGTH_SHORT).show();
    }
    /**
     * [简化Toast]
     * @param msg
     */
    public static void showToast2(View view,String msg){
        Toast.makeText(view.getContext(),msg,Toast.LENGTH_SHORT).show();
//      TopSnackBar.make(view, msg, BaseTransientBottomBar.LENGTH_SHORT).show();

    }
    /**
     * [是否设置沉浸状态栏]
     *
     * @param isSetStatusBar
     */
    public void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }
}
