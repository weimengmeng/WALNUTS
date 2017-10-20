package com.njjd.walnuts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njjd.adapter.ColumnArticleAdapter;
import com.njjd.domain.ColumnArticleDetailEntity;
import com.njjd.domain.ColumnArticleEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImagePagerActivity;
import com.njjd.utils.ListViewForScrollView;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/9/15.
 */
public class ColumnDetailActivity extends BaseActivity {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_add_help)
    TextView btnAddHelp;
    @BindView(R.id.lv_root)
    ScrollView root;
    @BindView(R.id.list_select)
    ListViewForScrollView listSelect;
    @BindView(R.id.editor2)
    WebView webView;
    @BindView(R.id.txt_article_title)
    TextView txtArticleTitle;
    @BindView(R.id.txt_column_name)
    TextView txtColumnName;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_intro)
    TextView txtIntro;
    private ColumnArticleAdapter adapter;
    private List<ColumnArticleEntity> entities = new ArrayList<>();
    private ColumnArticleDetailEntity detailActivity;

    @Override
    public int bindLayout() {
        return R.layout.activity_column_detail;
    }

    @Override
    public void initView(View view) {
        txtTitle.setText("文章详情");
        btnAddHelp.setVisibility(View.VISIBLE);
        adapter = new ColumnArticleAdapter(this, entities);
        listSelect.setAdapter(adapter);
        root.smoothScrollTo(0, 0);
    }

    private void initMyView() {
        webView.loadDataWithBaseURL(null, detailActivity.getContent(), "text/html", "utf-8", null);
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
        txtArticleTitle.setText(detailActivity.getTitle());
        txtColumnName.setText(detailActivity.getColumnName());
        GlideImageLoder.getInstance().displayImage(this,detailActivity.getHead(),imgHead);
        txtName.setText(detailActivity.getName());
        txtTime.setText(detailActivity.getTime());
        txtIntro.setText(detailActivity.getDesci());
    }

    private void getArticleDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", getIntent().getStringExtra("article_id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", ""));
        LogUtils.d("huan"+map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), map);
        HttpManager.getInstance().getColumnArticleDetail(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        try {
            JSONObject object = new JSONObject(gson.toJson(o));
            detailActivity = new ColumnArticleDetailEntity(object.getJSONObject("column_detail"));
            initMyView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArticleDetail();
    }

    private class JavascriptInterface {

        private Context context;


        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            final ArrayList<String> list1 = new ArrayList<>();
            list1.add(img);
            Intent intent = new Intent(ColumnDetailActivity.this, ImagePagerActivity.class);
            intent.putStringArrayListExtra(
                    ImagePagerActivity.EXTRA_IMAGE_URLS, list1);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 1);
            startActivity(intent);
        }
    }

    @OnClick({R.id.back, R.id.txt_column_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.txt_column_name:
                Intent intent=new Intent(this, ColumnActivity.class);
                intent.putExtra("column_id",Float.valueOf(detailActivity.getArticle_id()).intValue()+"");
                startActivity(intent);
                break;
        }
    }
}
