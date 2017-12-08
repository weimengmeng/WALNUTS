package com.njjd.walnuts;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
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
import com.njjd.adapter.AnswerReplyAdapter;
import com.njjd.db.DBHelper;
import com.njjd.domain.AnswerEntity;
import com.njjd.domain.CommentEntity;
import com.njjd.domain.QuestionEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.AndroidBug5497Workaround;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImagePagerActivity;
import com.njjd.utils.KeybordS;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.scrollablelayout.ScrollableLayout;
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

/**
 * Created by mrwim on 17/7/12.
 */

public class SearchQuesDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_quesTitle)
    TextView quesTitle;
    @BindView(R.id.txt_sort)
    TextView txtSort;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.mask)
    RelativeLayout mask;
    @BindView(R.id.lv_img)
    LinearLayout lvImg;
    @BindView(R.id.lv_root)
    ScrollView root;
    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.txt_answerNum)
    TextView txtAnswerNum;
    @BindView(R.id.txt_focusNum)
    TextView txtFocusNum;
    @BindView(R.id.txt_focus)
    TextView txtFocus;
    @BindView(R.id.ex_list)
    ExpandableListView exListVIew;
    @BindView(R.id.lv_reply)
    LinearLayout lvRply;
    @BindView(R.id.scrollLayout)
    ScrollableLayout scrollView;
    @BindView(R.id.lv_tag)
    LinearLayout lvTag;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.btn_add_help)
    TextView btnAddHelp;
    @BindView(R.id.lv_share)
    LinearLayout lvShare;
    @BindView(R.id.btn_cancle)
    Button btnCancle;
    @BindView(R.id.btn_reply)
    Button btnReply;
    private QuestionEntity questionEntity;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private LayoutInflater inflater;
    private List<AnswerEntity> answerEntities = new ArrayList<>();
    private AnswerEntity answerEntity;
    private CommentEntity commentEntity;
    private AnswerReplyAdapter answerReplyAdapter;
    private int current_group_id = 0, tempFocus = 0, tempAnswer = 0;
    private String comment_id = "";
    private PopupWindow popupWindow;
    private View mainView;
    private RadioButton layoutTop, layoutTime;
    private String tempOrder = "time";
    private AnswerEntity tempAnswerInfo;
    private CommentEntity tempComment;
    private String content = "";
    private int currentId = 0, type = 0;
    private UMShareListener mShareListener;

    @Override
    public int bindLayout() {
        return R.layout.activity_index_detail;
    }

    @Override
    public void initView(View view) {
        AndroidBug5497Workaround.assistActivity(this);
        btnAddHelp.setText("");
        btnAddHelp.setVisibility(View.VISIBLE);
        back.setText("返回");
        txtTitle.setText("问题详情");
        findViewById(R.id.txt_sort).setVisibility(View.VISIBLE);
    }

    private void initData() {
        quesTitle.setText(questionEntity.getTitle());
        txtAnswerNum.setText("回答 " + Float.valueOf(questionEntity.getAnswerNum()).intValue());
        txtFocusNum.setText("关注 " + Float.valueOf(questionEntity.getFocusNum()).intValue());
        tempFocus = Float.valueOf(questionEntity.getFocusNum()).intValue();
        tempAnswer = Float.valueOf(questionEntity.getAnswerNum()).intValue();
        txtContent.setText(questionEntity.getContent());
        if (questionEntity.getIsFocus() == 1) {
            txtFocus.setText("取消关注");
            txtFocus.setTextColor(getResources().getColor(R.color.txt_color));
            txtFocus.setBackgroundResource(R.drawable.txt_shape);
        }
        inflater = LayoutInflater.from(this);
        if (!"".equals(questionEntity.getPhoto())) {
            LogUtils.d(questionEntity.getPhoto());
            lvImg.setVisibility(View.VISIBLE);
            final String[] imgs = questionEntity.getPhoto().split(",");
            final ArrayList<String> list1 = new ArrayList<>();
            for (int i = 0; i < imgs.length; i++) {
                if (!imgs[i].contains("http:")) {
                    imgs[i] = HttpManager.BASE_URL2 + imgs[i];
                }
                list1.add(imgs[i].replace("\"", ""));
                relativeLayout = (RelativeLayout) inflater.inflate(R.layout.layout_img, null);
                imageView = (ImageView) relativeLayout.findViewById(R.id.img);
                imageView.setId(i);
                GlideImageLoder.getInstance().displayImage(this, imgs[i].replace("\"", ""), imageView);
                final int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchQuesDetailActivity.this, ImagePagerActivity.class);
                        intent.putStringArrayListExtra(
                                ImagePagerActivity.EXTRA_IMAGE_URLS, list1);
                        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, finalI);
                        startActivity(intent);
                    }
                });
                lvImg.addView(relativeLayout);
            }
        } else {
            lvImg.setVisibility(View.GONE);
        }
        scrollView.getHelper().setCurrentScrollableContainer(root);
        root.smoothScrollTo(0, 0);
        exListVIew.setEmptyView(findViewById(R.id.empty));
        answerReplyAdapter = new AnswerReplyAdapter(this, answerEntities, questionEntity.getQuestionId());
        exListVIew.setAdapter(answerReplyAdapter);
        exListVIew.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (!parent.isGroupExpanded(groupPosition)) {
                    if (answerEntities.get(groupPosition).getCommentEntityList().size() > 1) {
                        return false;
                    }
                    getComments(answerEntities.get(groupPosition).getAnwerId() + "");
                    current_group_id = groupPosition;
                }
                return false;
            }
        });
        mainView = LayoutInflater.from(this).inflate(R.layout.layout_pop, null);
        layoutTop = ((RadioButton) mainView.findViewById(R.id.rb_hot));
        layoutTime = (RadioButton) mainView.findViewById(R.id.rb_time);
        layoutTop.setOnClickListener(this);
        layoutTime.setOnClickListener(this);
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(mainView);
        popupWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setAnimationStyle(R.style.AnimTools);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setFocusable(true);
        popupWindow.update();
        scrollView.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int i, int i1) {
                if (exListVIew.getCount() > 0 && exListVIew.getChildAt(exListVIew.getLastVisiblePosition()).getVisibility() == View.VISIBLE)
                    AnswerReplyAdapter.CURRENT_PAGE++;
                getAnswerList();
            }
        });
        exListVIew.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                type = 0;
                lvRply.setVisibility(View.VISIBLE);
                lvShare.setVisibility(View.GONE);
                mask.setVisibility(View.VISIBLE);
                currentId = groupPosition;
                etContent.requestFocus();
                btnCancle.setText("取消回复");
                btnReply.setText("立即回复");
                etContent.setHint("回复" + answerEntities.get(groupPosition).getCommentEntityList().get(childPosition).getName());
                KeybordS.openKeybord(etContent, SearchQuesDetailActivity.this);
                tempAnswerInfo = answerEntities.get(groupPosition);
                tempComment = answerEntities.get(groupPosition).getCommentEntityList().get(childPosition);
                return false;
            }
        });
        exListVIew.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0, count = exListVIew
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    if (groupPosition != i) {
                        exListVIew.collapseGroup(i);
                    }
                }
            }
        });
        mShareListener = new CustomShareListener(this);
    }

    private void getDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, false, false), map);
        HttpManager.getInstance().getArticleDetail(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        lvTag.removeAllViews();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        JSONObject object;
        try {
            object = new JSONObject(gson.toJson(o));
            questionEntity = new QuestionEntity(object, "1");
            initData();
            questionEntity.setIsFocus(object.getInt("stat"));
            if (object.isNull("label_name") || object.isNull("label_id")) {
                return;
            }
            questionEntity.setTag(object.getString("label_name"));
            questionEntity.setTag_id(object.getString("label_id"));
            if (object.getInt("stat") == 1) {
                txtFocus.setText("取消关注");
                txtFocus.setTextColor(getResources().getColor(R.color.txt_color));
                txtFocus.setBackgroundResource(R.drawable.txt_shape);
            }
            txtAnswerNum.setText("回答 " + Float.valueOf(object.getString("answer_num")).intValue());
            txtFocusNum.setText("关注 " + Float.valueOf(object.getString("follow_num")).intValue());
            String[] strings = object.getString("label_name").split(",");
            String[] tags = object.getString("label_id").split(",");
            LinearLayout linearLayout;
            TextView tag;
            for (int i = 0; i < strings.length; i++) {
                linearLayout = (LinearLayout) inflater.inflate(R.layout.tag_item, null);
                tag = (TextView) linearLayout.findViewById(R.id.txt_tag);
                tag.setText(strings[i]);
                tag.setTag(tags[i]);
                tag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchQuesDetailActivity.this, TagActivity.class);
                        intent.putExtra("tag_id", v.getTag().toString());
                        intent.putExtra("name", ((TextView) v).getText().toString());
                        startActivity(intent);
                    }
                });
                lvTag.addView(linearLayout);
            }
            DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().insertOrReplace(questionEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnswerReplyAdapter.CURRENT_PAGE = 1;
        getDetail();
        getAnswerList();
//        for (int i = 0, count = exListVIew
//                .getExpandableListAdapter().getGroupCount(); i < count; i++) {
//            exListVIew.collapseGroup(i);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getAnswerList() {
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", getIntent().getStringExtra("id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("order", tempOrder);
        map.put("page", AnswerReplyAdapter.CURRENT_PAGE);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getAnswerListener, this, false, false), map);
        HttpManager.getInstance().getAnswerList(postEntity);
    }

    HttpOnNextListener getAnswerListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls(); //重点
            Gson gson = gsonBuilder.create();
            JSONObject object = null;
            if (AnswerReplyAdapter.CURRENT_PAGE == 1) {
                answerEntities.clear();
            }
            try {
                JSONArray array = new JSONArray(gson.toJson(o));
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    answerEntity = new AnswerEntity(object);
                    List<CommentEntity> commentEntities = new ArrayList<>();
                    commentEntities.add(new CommentEntity());
                    answerEntity.setCommentEntityList(commentEntities);
                        answerEntities.add(answerEntity);
                }
                answerReplyAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void getComments(String answer_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("comment_id", answer_id);
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("page", "1");
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getCommentListener, this, false, false), map);
        HttpManager.getInstance().getCommentList(postEntity);
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
                answerEntities.get(current_group_id).getCommentEntityList().clear();
                answerEntities.get(current_group_id).getCommentEntityList().add(new CommentEntity());
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    commentEntity = new CommentEntity(object);
                        answerEntities.get(current_group_id).getCommentEntityList().add(commentEntity);
                }
                answerReplyAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @OnClick({R.id.tv_cancle, R.id.share_aili, R.id.share_qq, R.id.share_qzone, R.id.share_sina, R.id.share_wechat, R.id.share_wechat_circle1, R.id.btn_add_help, R.id.back, R.id.txt_focus, R.id.img_answer, R.id.txt_sort, R.id.btn_reply, R.id.btn_cancle, R.id.mask})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_help:
                mask.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                            0);
                }
                lvShare.setVisibility(View.VISIBLE);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_cancle:
                mask.setVisibility(View.GONE);
                lvShare.setVisibility(View.GONE);
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
            case R.id.mask:
                mask.setVisibility(View.GONE);
                lvRply.setVisibility(View.GONE);
                lvShare.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, SearchQuesDetailActivity.this);
                break;
            case R.id.txt_sort:
                popupWindow.showAsDropDown(findViewById(R.id.txt_sort), 0, 0);
                break;
            case R.id.txt_focus:
                addFocus();
                break;
            case R.id.btn_cancle:
                mask.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, SearchQuesDetailActivity.this);
                lvRply.setVisibility(View.GONE);
                break;
            case R.id.btn_reply:
                if (etContent.getText().toString().trim().equals("")) {
                    ToastUtils.showShortToast(SearchQuesDetailActivity.this, "请输入回复内容");
                    return;
                }
                if (type == 0) {
                    addCommentF(etContent.getText().toString().trim());
                } else {
                    addComment(etContent.getText().toString().trim());
                }
                lvRply.setVisibility(View.GONE);
                mask.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, SearchQuesDetailActivity.this);
                etContent.setText("");
                break;
            case R.id.img_answer:
                Intent intent = new Intent(this, AnswerActivity.class);
                intent.putExtra("quesId", questionEntity.getQuestionId());
                intent.putExtra("quesTitle", questionEntity.getTitle());
                intent.putExtra("content", questionEntity.getContent());
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
        }
    }

    private void shareAction(SHARE_MEDIA share_media) {
        UMWeb web;
        UMImage image;
        mask.setVisibility(View.GONE);
        lvShare.setVisibility(View.GONE);
        web = new UMWeb("http://mp.heardtalk.com/web/mobile/articleShare?article_id=" +getIntent().getStringExtra("id"));
        web.setTitle(questionEntity.getTitle());//标题
        if ("".equals(questionEntity.getPhoto())) {
            image = new UMImage(SearchQuesDetailActivity.this, R.drawable.share);//资源文件
        } else {
            String[] strings = questionEntity.getPhoto().split(",");
            image = new UMImage(SearchQuesDetailActivity.this, strings[0].replace("\"", ""));//资源文件
        }
        web.setThumb(image);
        web.setDescription(questionEntity.getContent());//描述
        new ShareAction(this).setPlatform(share_media).withMedia(web).setCallback(mShareListener).share();
        mask.setVisibility(View.GONE);
        lvShare.setVisibility(View.GONE);
    }

    private void addComment(String comment) {
        this.content = comment;
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", getIntent().getStringExtra("id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("content", comment);
        map.put("comment_id", answerEntities.get(currentId).getAnwerId());
        map.put("sec_comment_id", answerEntities.get(currentId).getAnwerId());
        map.put("token", SPUtils.get(this, "token", "").toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(commentListener1, this, false, false), map);
        HttpManager.getInstance().pubComment(postEntity);
    }

    HttpOnNextListener commentListener1 = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonObject object = JSONUtils.getAsJsonObject(o);
            commentEntity = new CommentEntity();
            commentEntity.setContent(content);
            commentEntity.setCommentId(object.get("id").getAsString());
            commentEntity.setHead(SPUtils.get(SearchQuesDetailActivity.this, "head", "").toString());
            commentEntity.setName(SPUtils.get(SearchQuesDetailActivity.this, "name", "").toString());
            commentEntity.setSec_uid("sec_uid");
            commentEntity.setMessage(SPUtils.get(SearchQuesDetailActivity.this, "message", "").toString());
            commentEntity.setReplyNum("0");
            commentEntity.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            answerEntities.get(currentId).getCommentEntityList().add(1, commentEntity);
            answerEntities.get(currentId).setOpen((Float.valueOf(answerEntities.get(currentId).getOpen()).intValue() + 1) + "");
            answerReplyAdapter.notifyDataSetChanged();
            ToastUtils.showShortToast(SearchQuesDetailActivity.this, "评论成功");
        }
    };

    private void addCommentF(String content) {
        this.content = content;
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", getIntent().getStringExtra("id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("content", content);
        if ("sec_content".equals(tempComment.getSec_content())) {
            map.put("comment_id", tempComment.getCommentId());
            map.put("sec_comment_id", tempAnswerInfo.getAnwerId());
        } else {
            map.put("comment_id", tempComment.getCommentId());
            map.put("sec_comment_id", tempAnswerInfo.getAnwerId());
        }
        map.put("token", SPUtils.get(this, "token", "").toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(commentListener, this, false, false), map);
        HttpManager.getInstance().pubComment(postEntity);
    }

    HttpOnNextListener commentListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonObject object = JSONUtils.getAsJsonObject(o);
            commentEntity = new CommentEntity();
            commentEntity.setCommentId(object.get("id").getAsString());
            commentEntity.setContent(content);
            commentEntity.setHead(SPUtils.get(SearchQuesDetailActivity.this, "head", "").toString());
            commentEntity.setName(SPUtils.get(SearchQuesDetailActivity.this, "name", "").toString());
            commentEntity.setMessage(SPUtils.get(SearchQuesDetailActivity.this, "message", "").toString());
            commentEntity.setSec_uid(tempComment.getCommentUId());
            commentEntity.setSec_content(tempComment.getContent());
            commentEntity.setSec_headimgs(tempComment.getHead());
            commentEntity.setSec_uname(tempComment.getName());
            commentEntity.setReplyNum("0");
            commentEntity.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            answerEntities.get(currentId).getCommentEntityList().add(1, commentEntity);
            answerEntities.get(currentId).setOpen((Float.valueOf(answerEntities.get(currentId).getOpen()).intValue() + 1) + "");
            answerReplyAdapter.notifyDataSetChanged();
            ToastUtils.showShortToast(SearchQuesDetailActivity.this, "回复成功");
        }
    };

    private void addFocus() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", ""));
        map.put("article_id", getIntent().getStringExtra("id"));
        //0 取消关注 1 关注
        map.put("select", questionEntity.getIsFocus() == 0 ? 1 : 0);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(focusListener, this, true, false), map);
        HttpManager.getInstance().focusQuestion(postEntity);
    }

    HttpOnNextListener focusListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            ToastUtils.showShortToast(SearchQuesDetailActivity.this, questionEntity.getIsFocus() == 0 ? "成功关注" : "取消关注");
            if (questionEntity.getIsFocus() == 0) {
                txtFocus.setText("取消关注");
                txtFocus.setTextColor(getResources().getColor(R.color.txt_color));
                txtFocus.setBackgroundResource(R.drawable.txt_shape);
            } else {
                txtFocus.setText("+关注问题");
                txtFocus.setTextColor(getResources().getColor(R.color.white));
                txtFocus.setBackgroundResource(R.drawable.txt_shape_login);
            }
            txtFocusNum.setText("关注 " + (questionEntity.getIsFocus() == 0 ? ++tempFocus : --tempFocus));
            questionEntity.setIsFocus(questionEntity.getIsFocus() == 0 ? 1 : 0);
            DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().insertOrReplace(questionEntity);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_open:
                if (((TextView) v).getText().toString().equals("评论")) {
                    type = 1;
                    lvRply.setVisibility(View.VISIBLE);
                    lvShare.setVisibility(View.GONE);
                    mask.setVisibility(View.VISIBLE);
                    currentId = Integer.valueOf(v.getTag().toString());
                    for (int i = 0, count = exListVIew
                            .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                        if (currentId != i) {// 关闭其他分组
                            exListVIew.collapseGroup(i);
                        } else {
                            exListVIew.expandGroup(i, true);
                        }
                    }
                    etContent.requestFocus();
                    etContent.setHint("回复" + answerEntities.get(Integer.valueOf(v.getTag().toString())).getName());
                    KeybordS.openKeybord(etContent, SearchQuesDetailActivity.this);
                    btnCancle.setText("取消评论");
                    btnReply.setText("立即评论");
                    return;
                }
                if (!exListVIew.isGroupExpanded(Integer.valueOf(v.getTag().toString()))) {
                    exListVIew.expandGroup(Integer.valueOf(v.getTag().toString()), true);
                    if (answerEntities.get(Integer.valueOf(v.getTag().toString())).getCommentEntityList().size() > 1) {
                        return;
                    }
                    getComments(answerEntities.get(Integer.valueOf(v.getTag().toString())).getAnwerId() + "");
                    current_group_id = Integer.valueOf(v.getTag().toString());
                } else {
                    exListVIew.collapseGroup(Integer.valueOf(v.getTag().toString()));
                }
                break;
            case R.id.rb_hot:
                tempOrder = "hot";
                AnswerReplyAdapter.CURRENT_PAGE = 1;
                getAnswerList();
                txtSort.setText("按质量排序");
                popupWindow.dismiss();
                for (int i = 0, count = exListVIew
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    exListVIew.collapseGroup(i);
                }
                break;
            case R.id.rb_time:
                tempOrder = "time";
                AnswerReplyAdapter.CURRENT_PAGE = 1;
                txtSort.setText("按时间排序");
                getAnswerList();
                popupWindow.dismiss();
                for (int i = 0, count = exListVIew
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    exListVIew.collapseGroup(i);
                }
                break;
            case R.id.et_comment:
                type = 1;
                btnCancle.setText("取消评论");
                btnReply.setText("立即评论");
                lvRply.setVisibility(View.VISIBLE);
                lvShare.setVisibility(View.GONE);
                mask.setVisibility(View.VISIBLE);
                currentId = Integer.valueOf(v.getTag().toString());
                etContent.requestFocus();
                etContent.setHint("回复" + answerEntities.get(Integer.valueOf(v.getTag().toString())).getName());
                KeybordS.openKeybord(etContent, SearchQuesDetailActivity.this);
                break;
        }
    }

    private class CustomShareListener implements UMShareListener {
        private WeakReference<SearchQuesDetailActivity> mActivity;

        private CustomShareListener(SearchQuesDetailActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {
//            loadingDialog.show();
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
            ToastUtils.showShortToast(SearchQuesDetailActivity.this, "分享取消");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
