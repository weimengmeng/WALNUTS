package com.njjd.walnuts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.njjd.utils.CommonUtils;
import com.njjd.utils.PhotoUtil;
import com.njjd.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by mrwim on 17/10/16.
 */

public class PubArticleActivity extends BaseActivity {

    @BindView(R.id.editor)
    RichEditor mEditor;
    @BindView(R.id.editor2)
    WebView webView;
    @Override
    public int bindLayout() {
        return R.layout.activity_pubarticle;
    }

    @Override
    public void initView(View view) {
        mEditor.setEditorFontSize(22);//设置字体大小
        mEditor.setEditorFontColor(Color.BLACK);//设置字体颜色
        mEditor.setBold();//设置粗体
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.btn_add, R.id.btn_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                Picker.from(this)
                        .count(1)
                        .enableCamera(true)
                        .setEngine(new GlideEngine())
                        .forResult(0);
                break;
            case R.id.btn_show:
                webView.loadDataWithBaseURL(null, mEditor.getHtml(), "text/html", "utf-8", null);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        //这段js函数的功能就是注册监听，遍历所有的img标签，并添加onClick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
                        webView.loadUrl("javascript:(function(){"
                                + "var objs = document.getElementsByTagName(\"img\"); "
                                + "for(var i=0;i<objs.length;i++)  " + "{"
                                + "    objs[i].onclick=function()  " + "    {  "
                                + "        window.imagelistner.openImage(this.src);  "
                                + "    }  " + "}" + "})()");
                    }
                });
                webView.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
                ToastUtils.showShortToast(this,mEditor.getHtml());
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0) {
            List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
            String imgpath = PhotoUtil.saveMyBitmapWH(CommonUtils.getRealPathFromUri(this, mSelected.get(0)), 480, 800);
            mEditor.insertImage("http://a.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=bbba1da0d60735fa91a546bdab612385/9825bc315c6034a84e7d073ac9134954082376e9.jpg", "image");
        }
    }
    private class JavascriptInterface {

        private Context context;


        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            ToastUtils.showShortToast(context,img);
//            Intent intent = new Intent();
//            intent.putExtra("img", img);
//            intent.setClass(context, ImageActivity.class);
//            context.startActivity(intent);
        }
    }
}
