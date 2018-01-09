package com.njjd.walnuts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.njjd.adapter.ArticleCommentAdapter;
import com.njjd.adapter.RecommendArticleAdapter;
import com.njjd.application.ConstantsVal;
import com.njjd.domain.ColumnArticleDetailEntity;
import com.njjd.domain.ColumnArticleEntity;
import com.njjd.domain.CommentEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.AndroidBug5497Workaround;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImagePagerActivity;
import com.njjd.utils.KeybordS;
import com.njjd.utils.ListViewForScrollView;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/9/15.
 */
public class ColumnDetailActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_add_help)
    TextView btnAddHelp;
    @BindView(R.id.lv_root)
    ScrollView root;
    @BindView(R.id.list_article)
    ListViewForScrollView listSelect;
    @BindView(R.id.list_comment)
    ListViewForScrollView listComment;
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
    @BindView(R.id.txt_answerNum)
    TextView txtAnswerNum;
    @BindView(R.id.txt_save)
    TextView txtSave;
    @BindView(R.id.txt_agree)
    TextView txtAgree;
    private RecommendArticleAdapter adapter;
    private List<ColumnArticleEntity> entities = new ArrayList<>();
    private ColumnArticleDetailEntity detailActivity;
    private List<CommentEntity> list=new ArrayList<>();
    private ArticleCommentAdapter madapter;
    @BindView(R.id.mask)
    RelativeLayout mask;
    @BindView(R.id.lv_reply)
    LinearLayout lvReply;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.btn_cancle)
    Button btnCancle;
    @BindView(R.id.btn_reply)
    Button btnReply;
    private int open=0;
    private CommentEntity commentEntity;
    private String content="";
    private View footView;
    @BindView(R.id.lv_share)
    LinearLayout lvShare;
    private UMShareListener mShareListener;
    @Override
    public int bindLayout() {
        return R.layout.activity_column_detail;
    }

    @Override
    public void initView(View view) {
        AndroidBug5497Workaround.assistActivity(this);
        footView=LayoutInflater.from(this).inflate(R.layout.footer,null);
        txtTitle.setText("文章详情");
        btnAddHelp.setVisibility(View.VISIBLE);
        root.smoothScrollTo(0, 0);
        madapter=new ArticleCommentAdapter(this,list);
        list.add(new CommentEntity());
        ((TextView)footView.findViewById(R.id.txt_review)).setText("暂无评论");
        footView.findViewById(R.id.txt_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((TextView)footView.findViewById(R.id.txt_review)).getText().toString().equals("查看更多")){
                    return;
                }
                madapter.setPage(madapter.getPage()+1);
                getComment();
            }
        });
        ((TextView)footView.findViewById(R.id.txt_review)).setTextColor(getResources().getColor(R.color.txt_color));
        listComment.addFooterView(footView);
        listComment.setAdapter(madapter);
        listComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ColumnDetailActivity.this,ArticleComReplyActivity.class);
                Bundle bundle=new Bundle();
                intent.putExtra("type","0");
                bundle.putSerializable("comment",list.get(position));
                intent.putExtra("comment",bundle);
                intent.putExtra("article_id",getIntent().getStringExtra("article_id"));
                startActivity(intent);
            }
        });
        adapter = new RecommendArticleAdapter(this, entities);
        listSelect.setAdapter(adapter);
        listSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ColumnDetailActivity.this, ColumnDetailActivity.class);
                intent.putExtra("article_id",Float.valueOf(entities.get(position).getArticle_id()).intValue()+"");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.in, R.anim.out);
            }
        });
        mShareListener = new CustomShareListener(this);
    }

    private void initMyView() {
        webView.loadDataWithBaseURL(null, detailActivity.getContent(), "text/html", "utf-8", null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
               webView.loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName('img'); " +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{"
                        + "var img = objs[i];   " +
                        "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                        "}" +
                        "})()");
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
        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ColumnDetailActivity.this, PeopleInfoActivity.class);
                intent.putExtra("uid", detailActivity.getUid());
                startActivity(intent);
            }
        });
        GlideImageLoder.getInstance().displayImage(this, detailActivity.getHead(), imgHead);
        txtName.setText(detailActivity.getName());
        txtTime.setText(detailActivity.getTime());
        txtIntro.setText(detailActivity.getDesci());
        if("1.0".equals(detailActivity.getIsSave())){
            txtSave.setText("取消收藏");
            txtSave.setTextColor(getResources().getColor(R.color.txt_color));
        }else{
            txtSave.setText("收藏");
            txtSave.setTextColor(getResources().getColor(R.color.login));
        }
        if("1.0".equals(detailActivity.getIsPoint())){
            txtAgree.setBackgroundResource(R.drawable.background_button_div_grey);
            txtAgree.setSelected(false);
            txtAgree.setTextColor(getResources().getColor(R.color.txt_color));
        }else{
            txtAgree.setBackgroundResource(R.drawable.background_button_div);
            txtAgree.setSelected(true);
            txtAgree.setTextColor(getResources().getColor(R.color.white));
        }
        txtAgree.setText(Float.valueOf(detailActivity.getPointNum()).intValue()+"");
        txtAnswerNum.setText("展开评论"+Float.valueOf(detailActivity.getCommentNum()).intValue());
        open=Float.valueOf(detailActivity.getCommentNum()).intValue();
        if(Float.valueOf(detailActivity.getCommentNum()).intValue()==0) {
            txtAnswerNum.setText("评论");
        }
        listComment.setVisibility(View.GONE);
        getComment();
    }
    private void getComment() {
        Map<String, Object> map = new HashMap<>();
        map.put("article_id",getIntent().getStringExtra("article_id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("page",madapter.getPage());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getCommentListener, this, false, false), map);
        HttpManager.getInstance().getAnswerList(postEntity);
    }

    HttpOnNextListener getCommentListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls(); //重点
            Gson gson = gsonBuilder.create();
            JSONObject object = null;
            try {
                JSONArray array = new JSONArray(gson.toJson(o));
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    commentEntity = new CommentEntity(object);
                    list.add(commentEntity);
                }
                madapter.notifyDataSetChanged();
                if(array.length()<10){
                    ((TextView)footView.findViewById(R.id.txt_review)).setText("已加载全部");
                }else{
                    ((TextView)footView.findViewById(R.id.txt_review)).setText("查看更多");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private void getArticleDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", getIntent().getStringExtra("article_id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", ""));
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
        getRecommendArticle();
    }

    private void getRecommendArticle() {
        Map<String, Object> map = new HashMap<>();
        map.put("article_id",getIntent().getStringExtra("article_id"));
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getRecommendListener, this, true, false), map);
        HttpManager.getInstance().getRecommendArticle(postEntity);
    }

    HttpOnNextListener getRecommendListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls(); //重点
            Gson gson = gsonBuilder.create();
            try {
                JSONObject object = new JSONObject(gson.toJson(o));
                JSONArray array = object.getJSONArray("column");
                ColumnArticleEntity entity;
                for (int i = 0; i < array.length(); i++) {
                    entity = new ColumnArticleEntity(array.getJSONObject(i));
                    entities.add(entity);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_save:
                saveArticle();
                 break;
            case R.id.txt_agree:
                pointArticle();
                break;
            case R.id.mask:
                mask.setVisibility(View.GONE);
                lvReply.setVisibility(View.GONE);
                lvShare.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, ColumnDetailActivity.this);
                break;
            case R.id.et_comment:
                lvReply.setVisibility(View.VISIBLE);
                mask.setVisibility(View.VISIBLE);
                lvShare.setVisibility(View.GONE);
                etContent.requestFocus();
                etContent.setHint("回复" + txtName.getText().toString());
                KeybordS.openKeybord(etContent, ColumnDetailActivity.this);
                btnCancle.setText("取消评论");
                btnReply.setText("立即评论");
                listComment.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_answerNum:
                if(((TextView)v).getText().toString().equals("评论")){
                    lvReply.setVisibility(View.VISIBLE);
                    mask.setVisibility(View.VISIBLE);
                    etContent.requestFocus();
                    etContent.setHint("回复" + txtName.getText().toString());
                    KeybordS.openKeybord(etContent, ColumnDetailActivity.this);
                    btnCancle.setText("取消评论");
                    btnReply.setText("立即评论");
                    listComment.setVisibility(View.VISIBLE);
                    return;
                }
                listComment.setVisibility(listComment.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                if(listComment.getVisibility()==View.VISIBLE){
                    txtAnswerNum.setText("收起评论"+open);
                }else{
                    txtAnswerNum.setText("展开评论"+open);
                }
                break;
            case R.id.btn_cancle:
                mask.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, ColumnDetailActivity.this);
                lvReply.setVisibility(View.GONE);
                break;
            case R.id.btn_reply:
                if (etContent.getText().toString().trim().equals("")) {
                    ToastUtils.showShortToast(ColumnDetailActivity.this, "请输入回复内容");
                    return;
                }
                    addComment(etContent.getText().toString().trim());
                lvReply.setVisibility(View.GONE);
                mask.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, ColumnDetailActivity.this);
                etContent.setText("");
                break;
        }
    }
    private void addComment(String comment) {
        this.content = comment;
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", getIntent().getStringExtra("article_id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("content", comment);
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("sec_comment_id",  getIntent().getStringExtra("article_id"));
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(commentListener1, this, false, false), map);
        HttpManager.getInstance().pubComment(postEntity);
    }

    HttpOnNextListener commentListener1 = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonObject object= JSONUtils.getAsJsonObject(o);
            commentEntity = new CommentEntity();
            commentEntity.setContent(content);
            commentEntity.setCommentId(object.get("id").getAsString());
            commentEntity.setHead(SPUtils.get(ColumnDetailActivity.this, "head", "").toString());
            commentEntity.setName(SPUtils.get(ColumnDetailActivity.this, "name", "").toString());
            commentEntity.setSec_uid("sec_uid");
            commentEntity.setMessage(SPUtils.get(ColumnDetailActivity.this, "message", "").toString());
            commentEntity.setReplyNum("0");
            commentEntity.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            list.add(1, commentEntity);
            txtAnswerNum.setText("收起评论"+(++open) + "");
            madapter.notifyDataSetChanged();
            ToastUtils.showShortToast(ColumnDetailActivity.this, "评论成功");
        }
    };
    private class JavascriptInterface {

        private Context context;
        final ArrayList<String> list1 = new ArrayList<>();

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            list1.clear();
            list1.add(img);
            Intent intent = new Intent(ColumnDetailActivity.this, ImagePagerActivity.class);
            intent.putStringArrayListExtra(
                    ImagePagerActivity.EXTRA_IMAGE_URLS, list1);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 1);
            startActivity(intent);
        }
    }

    @OnClick({R.id.back, R.id.txt_column_name, R.id.share_aili, R.id.share_qq, R.id.share_qzone, R.id.share_sina, R.id.share_wechat, R.id.share_wechat_circle1, R.id.btn_add_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_add_help:
                mask.setVisibility(View.VISIBLE);
                InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                            0);
                }
                lvReply.setVisibility(View.GONE);
                lvShare.setVisibility(View.VISIBLE);
                break;
            case R.id.share_aili:
                shareAction(SHARE_MEDIA.ALIPAY);
                break;
            case R.id.share_qq:
                shareAction(SHARE_MEDIA.QQ);
                break;
            case R.id.share_qzone:
                shareAction(SHARE_MEDIA.QZONE);
                break;
            case R.id.share_sina:
                shareAction(SHARE_MEDIA.SINA);
                break;
            case R.id.share_wechat:
                shareAction(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_wechat_circle1:
                shareAction(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.txt_column_name:
                Intent intent = new Intent(this, ColumnActivity.class);
                intent.putExtra("column_id", Float.valueOf(detailActivity.getColumn_id()).intValue() + "");
                startActivity(intent);
                break;
        }
    }
    private void saveArticle(){
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", getIntent().getStringExtra("article_id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", "").toString());
        if(detailActivity.getIsSave().equals("1.0")){
            map.put("select", 0);
        }else{
            map.put("select", 1);
        }
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                if("0.0".equals(detailActivity.getIsSave())){
                    txtSave.setText("取消收藏");
                    txtSave.setTextColor(getResources().getColor(R.color.txt_color));
                    detailActivity.setIsSave("1.0");
                }else{
                    txtSave.setText("收藏");
                    txtSave.setTextColor(getResources().getColor(R.color.login));
                    detailActivity.setIsSave("0.0");
                }
            }
        }, this, false, false), map);
        HttpManager.getInstance().saveArticle(postEntity);
    }
    private void pointArticle(){
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", getIntent().getStringExtra("article_id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", "").toString());
        if(detailActivity.getIsPoint().equals("1.0")){
            map.put("select", 0);
        }else{
            map.put("select", 1);
        }
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                if("0.0".equals(detailActivity.getIsPoint())){
                    txtAgree.setBackgroundResource(R.drawable.background_button_div_grey);
                    txtAgree.setSelected(false);
                    txtAgree.setTextColor(getResources().getColor(R.color.txt_color));
                    detailActivity.setIsPoint("1.0");
                    txtAgree.setText(""+(Float.valueOf(detailActivity.getPointNum()).intValue()+1));
                    detailActivity.setPointNum(""+(Float.valueOf(detailActivity.getPointNum()).intValue()+1));
                }else{
                    txtAgree.setBackgroundResource(R.drawable.background_button_div);
                    txtAgree.setSelected(true);
                    txtAgree.setTextColor(getResources().getColor(R.color.white));
                    detailActivity.setIsPoint("0.0");
                    txtAgree.setText(""+(Float.valueOf(detailActivity.getPointNum()).intValue()-1));
                    detailActivity.setPointNum(""+(Float.valueOf(detailActivity.getPointNum()).intValue()-1));
                }
            }
        }, this, false, false), map);
        HttpManager.getInstance().pointArticle(postEntity);
    }
    private void  shareAction(SHARE_MEDIA share_media){
        UMWeb web;
        UMImage image;
        mask.setVisibility(View.GONE);
        lvShare.setVisibility(View.GONE);
        web = new UMWeb(ConstantsVal.SHAREURL+Float.valueOf(detailActivity.getArticle_id()).intValue());
        web.setTitle(detailActivity.getTitle());//标题
        image = new UMImage(this,HttpManager.BASE_URL2+detailActivity.getPic().split(",")[0].replace("\"",""));//资源文件
        web.setThumb(image);
        web.setDescription(detailActivity.getDesci());//描述
        new ShareAction(this).setPlatform(share_media).withMedia(web).setCallback(mShareListener).share();
        mask.setVisibility(View.GONE);
        lvShare.setVisibility(View.GONE);
    }
    private class CustomShareListener implements UMShareListener {
        private WeakReference<ColumnDetailActivity> mActivity;
        private CustomShareListener(ColumnDetailActivity activity) {
            mActivity = new WeakReference(activity);
        }
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), "分享失败", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showShortToast(ColumnDetailActivity.this,"分享取消");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(webView!=null) {
            webView.onPause();
            webView.pauseTimers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(webView!=null){
            webView.resumeTimers();
            webView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webView!=null){
            webView.destroy();
            webView=null;
        }
    }
}
