package com.njjd.walnuts;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.mywidget.LoadingDialog;
import com.njjd.utils.ImmersedStatusbarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/8/18.
 */

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.lv_web)
    LinearLayout lvWeb;
    @BindView(R.id.web)
    WebView web;
    private Context context;
    private LoadingDialog loadingDialog;

    @Override
    public int bindLayout() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView(View view) {
        txtTitle.setText(getIntent().getStringExtra("title"));
        context = this;
        init();
    }

    private void init() {
        loadingDialog = new LoadingDialog(context);
        web.getSettings().setJavaScriptEnabled(true);
        web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = web.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDisplayZoomControls(false); //隐藏webview缩放按钮
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        web.loadUrl(getIntent().getStringExtra("url"));

        // 加载数据
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    loadingDialog.dismiss();
                } else {
                    if (!WebViewActivity.this.isDestroyed())
                        loadingDialog.show();
                }
            }
        });
        web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view,
                                                    final String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (web.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
            web.goBack();
            return true;
        } else if (web.canGoBack() == false
                && keyCoder == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersedStatusbarUtils.initAfterSetContentView(this, top);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
