package com.njjd.walnuts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.njjd.adapter.AnswerCommentAdapter;
import com.njjd.db.DBHelper;
import com.njjd.domain.CommentEntity;
import com.njjd.domain.QuestionEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.DateUtils;
import com.njjd.utils.FolderTextView;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.KeybordS;
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
import java.text.ParsePosition;
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

public class SelectAnswerDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.list_comment)
    ListView listComment;
    @BindView(R.id.btn_add_help)
    TextView btnAddHelp;
    @BindView(R.id.txt_quesTitle)
    TextView txtQuesTitle;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_message)
    TextView txtMessage;
    @BindView(R.id.txt_agree)
    TextView txtAgree;
    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.txt_save)
    TextView txtSave;
    @BindView(R.id.txt_open)
    TextView txtOpen;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.mask)
    RelativeLayout mask;
    @BindView(R.id.lv_reply)
    LinearLayout lvReply;
    @BindView(R.id.lv_answer)
    LinearLayout lvAnswer;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.lv_share)
    LinearLayout lvShare;
    @BindView(R.id.btn_cancle)
    Button btnCancle;
    @BindView(R.id.btn_reply)
    Button btnReply;
    @BindView(R.id.txt_folder)
    FolderTextView txtFolder;
    private QuestionEntity questionEntity;
    private String answer_id = "";
    private CommentEntity commentEntity, tempComment;
    private List<CommentEntity> commentEntityList = new ArrayList<>();
    private AnswerCommentAdapter adapter;
    private int type = 0, open = 0, agree = 0;
    private String content;
    private String questionId = "", questiontitle = "";
    private UMShareListener mShareListener;

    @Override
    public int bindLayout() {
        return R.layout.activity_selected_detail;
    }

    @Override
    public void initView(View view) {
        txtTitle.setText("回答详情");
        questionId = getIntent().getStringExtra("questionId");
        answer_id = getIntent().getStringExtra("answer_id");
        questiontitle = getIntent().getStringExtra("questionTitle");
        txtQuesTitle.setText(questiontitle);
        txtFolder.setText(getIntent().getStringExtra("contents"));
        adapter = new AnswerCommentAdapter(this, commentEntityList);
        listComment.setAdapter(adapter);
        commentEntityList.add(new CommentEntity());
        getComments(answer_id);
        getAnswerList();
        getDetail();
        listComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = 0;
                lvReply.setVisibility(View.VISIBLE);
                lvShare.setVisibility(View.GONE);
                mask.setVisibility(View.VISIBLE);
                etContent.requestFocus();
                btnCancle.setText("取消回复");
                btnReply.setText("立即回复");
                tempComment = commentEntityList.get(position);
                etContent.setHint("回复" + commentEntityList.get(position).getName());
                KeybordS.openKeybord(etContent, SelectAnswerDetailActivity.this);
            }
        });
        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAnswerDetailActivity.this, PeopleInfoActivity.class);
                intent.putExtra("uid", imgHead.getTag().toString());
                startActivity(intent);
            }
        });
        mShareListener = new CustomShareListener(this);
    }

    private void getDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", Float.valueOf(questionId).intValue());
        map.put("uid", SPUtils.get(this, "userId", ""));
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, false, false), map);
        HttpManager.getInstance().getArticleDetail(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        JSONObject object;
        try {
            object = new JSONObject(gson.toJson(o));
            questionEntity = new QuestionEntity(object, "");
            questionEntity.setQuestionId(questionId);
            DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().insertOrReplace(questionEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAnswerList() {
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", Float.valueOf(questionId).intValue());
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("page", "1");
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getAnswerListener, this, true, false), map);
        HttpManager.getInstance().getAnswerList(postEntity);
    }

    HttpOnNextListener getAnswerListener = new HttpOnNextListener() {
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
                    if (answer_id.equals(object.getString("id"))) {
                        GlideImageLoder.getInstance().displayImage(SelectAnswerDetailActivity.this, object.isNull("headimgs") ? "" : object.getString("headimgs"), imgHead);
                        imgHead.setTag(object.getString("uid"));
                        txtName.setText(object.getString("uname"));
                        txtMessage.setText(object.getString("introduction"));
                        txtContent.setText(object.getString("content"));
                        if (object.getString("p_stat").equals("1")) {
                            txtAgree.setBackgroundResource(R.drawable.background_button_div_grey);
                            txtAgree.setSelected(false);
                            txtAgree.setTag("0");
                            txtAgree.setTextColor(SelectAnswerDetailActivity.this.getResources().getColor(R.color.txt_color));
                        } else {
                            txtAgree.setBackgroundResource(R.drawable.background_button_div);
                            txtAgree.setTextColor(SelectAnswerDetailActivity.this.getResources().getColor(R.color.white));
                            txtAgree.setSelected(true);
                            txtAgree.setTag("1");
                        }
                        agree = Float.valueOf(object.getString("point_num")).intValue();
                        txtAgree.setText(String.valueOf(Float.valueOf(object.getString("point_num")).intValue()));
                        if (Float.valueOf(object.getString("answer_num")).intValue() == 0) {
                            txtOpen.setText("评论");
                        } else {
                            txtOpen.setText("展开评论" + String.valueOf(Float.valueOf(object.getString("answer_num")).intValue()));
                        }
                        open = Float.valueOf(object.getString("answer_num")).intValue();
                        if (object.getString("c_stat").equals("1")) {
                            txtSave.setText("取消收藏");
                            txtSave.setTextColor(getResources().getColor(R.color.txt_color));
                            txtSave.setTag("0");
                        } else {
                            txtSave.setText("收藏");
                            txtSave.setTextColor(getResources().getColor(R.color.login));
                            txtSave.setTag("1");
                        }
                        ParsePosition pos = new ParsePosition(0);
                        txtTime.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getString("change_time"), pos)));
                        lvAnswer.setVisibility(View.VISIBLE);
                        break;
                    }
                }
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
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    commentEntity = new CommentEntity(object);
                    commentEntityList.add(commentEntity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.mask, R.id.tv_cancle, R.id.share_aili, R.id.share_qq, R.id.share_qzone, R.id.share_sina, R.id.share_wechat, R.id.share_wechat_circle1, R.id.btn_add_help, R.id.back, R.id.txt_save, R.id.txt_agree, R.id.txt_more, R.id.txt_quesTitle})
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
            case R.id.txt_quesTitle:
            case R.id.txt_more:
                Intent intent = new Intent(this, IndexDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("question", questionEntity);
                intent.putExtra("question", bundle);
                intent.putExtra("type", "1");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.mask:
                mask.setVisibility(View.GONE);
                lvReply.setVisibility(View.GONE);
                lvShare.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, SelectAnswerDetailActivity.this);
                break;
            case R.id.txt_save:
                if (txtSave.getTag().toString().equals("1")) {
                    agreeOrSave("collect_comment_id");
                } else {
                    agreeOrSave("collect_comment_id_not");
                }
                break;
            case R.id.txt_agree:
                if (txtAgree.getTag().toString().equals("1")) {
                    agreeOrSave("point_comment_id");
                } else {
                    agreeOrSave("point_comment_id_not");
                }
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
        }
    }

    private void shareAction(SHARE_MEDIA share_media) {
        UMWeb web;
        UMImage image;
        mask.setVisibility(View.GONE);
        lvShare.setVisibility(View.GONE);
        web = new UMWeb("http://mp.heardtalk.com/web/mobile/articleShare?article_id=" + Float.valueOf(questionEntity.getQuestionId()).intValue());
        web.setTitle(questionEntity.getTitle());//标题
        if ("".equals(questionEntity.getPhoto())) {
            image = new UMImage(SelectAnswerDetailActivity.this, R.drawable.logo);//资源文件
        } else {
            String[] strings = questionEntity.getPhoto().split(",");
            image = new UMImage(SelectAnswerDetailActivity.this, strings[0].replace("\"", ""));//资源文件
        }
        web.setThumb(image);
        web.setDescription(questionEntity.getContent());//描述
        new ShareAction(this).setPlatform(share_media).withMedia(web).setCallback(mShareListener).share();
        mask.setVisibility(View.GONE);
        lvShare.setVisibility(View.GONE);
    }

    private void agreeOrSave(final String params) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put(params, Float.valueOf(answer_id).intValue());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                switch (params) {
                    case "point_comment_id":
                        txtAgree.setBackgroundResource(R.drawable.background_button_div_grey);
                        txtAgree.setTextColor(getResources().getColor(R.color.txt_color));
                        txtAgree.setSelected(false);
                        txtAgree.setTag("0");
                        txtAgree.setText((Integer.valueOf(txtAgree.getText().toString()) + 1) + "");
                        break;
                    case "point_comment_id_not":
                        txtAgree.setBackgroundResource(R.drawable.background_button_div);
                        txtAgree.setTextColor(getResources().getColor(R.color.white));
                        txtAgree.setSelected(true);
                        txtAgree.setTag("1");
                        txtAgree.setText((Integer.valueOf(txtAgree.getText().toString()) - 1) + "");
                        break;
                    case "collect_comment_id":
                        txtSave.setText("取消收藏");
                        txtSave.setTextColor(getResources().getColor(R.color.txt_color));
                        txtSave.setTag("0");
                        break;
                    case "collect_comment_id_not":
                        txtSave.setText("收藏");
                        txtSave.setTextColor(getResources().getColor(R.color.login));
                        txtSave.setTag("1");
                        break;
                }
            }
        }, this, true, false), map);
        HttpManager.getInstance().agreeOrPraise(postEntity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mask:
                mask.setVisibility(View.GONE);
                lvReply.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, SelectAnswerDetailActivity.this);
                break;
            case R.id.et_comment:
                type = 1;
                lvReply.setVisibility(View.VISIBLE);
                mask.setVisibility(View.VISIBLE);
                etContent.requestFocus();
                etContent.setHint("回复" + txtName.getText().toString());
                KeybordS.openKeybord(etContent, SelectAnswerDetailActivity.this);
                btnCancle.setText("取消评论");
                btnReply.setText("立即评论");
                listComment.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_open:
                if (((TextView) v).getText().toString().equals("评论")) {
                    type = 1;
                    lvReply.setVisibility(View.VISIBLE);
                    mask.setVisibility(View.VISIBLE);
                    etContent.requestFocus();
                    etContent.setHint("回复" + txtName.getText().toString());
                    KeybordS.openKeybord(etContent, SelectAnswerDetailActivity.this);
                    btnCancle.setText("取消评论");
                    btnReply.setText("立即评论");
                    listComment.setVisibility(View.VISIBLE);
                    return;
                }
                listComment.setVisibility(listComment.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                if (listComment.getVisibility() == View.VISIBLE) {
                    txtOpen.setText("收起评论" + open);
                } else {
                    txtOpen.setText("展开评论" + open);
                }
                break;
            case R.id.btn_cancle:
                mask.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, SelectAnswerDetailActivity.this);
                lvReply.setVisibility(View.GONE);
                break;
            case R.id.btn_reply:
                if (etContent.getText().toString().trim().equals("")) {
                    ToastUtils.showShortToast(SelectAnswerDetailActivity.this, "请输入回复内容");
                    return;
                }
                if (type == 0) {
                    addCommentF(etContent.getText().toString().trim());
                } else {
                    addComment(etContent.getText().toString().trim());
                }
                lvReply.setVisibility(View.GONE);
                mask.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, SelectAnswerDetailActivity.this);
                etContent.setText("");
                break;
        }
    }

    private void addComment(String comment) {
        this.content = comment;
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", Float.valueOf(questionId).intValue());
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("content", comment);
        map.put("comment_id", String.valueOf(Float.valueOf(answer_id).intValue()));
        map.put("sec_comment_id", String.valueOf(Float.valueOf(answer_id).intValue()));
        map.put("token", SPUtils.get(this, "token", "").toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(commentListener1, this, false, false), map);
        HttpManager.getInstance().pubComment(postEntity);
    }

    HttpOnNextListener commentListener1 = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonObject object= JSONUtils.getAsJsonObject(o);
            commentEntity = new CommentEntity();
            commentEntity.setCommentId(object.get("id").getAsString());
            commentEntity.setContent(content);
            commentEntity.setHead(SPUtils.get(SelectAnswerDetailActivity.this, "head", "").toString());
            commentEntity.setName(SPUtils.get(SelectAnswerDetailActivity.this, "name", "").toString());
            commentEntity.setSec_uid("sec_uid");
            commentEntity.setMessage(SPUtils.get(SelectAnswerDetailActivity.this, "message", "").toString());
            commentEntity.setReplyNum("0");
            commentEntity.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            commentEntityList.add(1, commentEntity);
            txtOpen.setText("收起评论" + (++open) + "");
            adapter.notifyDataSetChanged();
            ToastUtils.showShortToast(SelectAnswerDetailActivity.this, "评论成功");
        }
    };

    private void addCommentF(String content) {
        this.content = content;
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", Float.valueOf(questionId).intValue());
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("content", content);
        if ("sec_content".equals(tempComment.getSec_content())) {
            map.put("comment_id", tempComment.getCommentId());
            map.put("sec_comment_id", Float.valueOf(answer_id).intValue());
        } else {
            map.put("comment_id", tempComment.getCommentId());
            map.put("sec_comment_id", Float.valueOf(answer_id).intValue());
        }
        map.put("token", SPUtils.get(this, "token", "").toString());
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(commentListener, this, false, false), map);
        HttpManager.getInstance().pubComment(postEntity);
    }

    HttpOnNextListener commentListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonObject object=JSONUtils.getAsJsonObject(o);
            commentEntity = new CommentEntity();
            commentEntity.setCommentId(object.get("id").getAsString());
            commentEntity.setContent(content);
            commentEntity.setHead(SPUtils.get(SelectAnswerDetailActivity.this, "head", "").toString());
            commentEntity.setName(SPUtils.get(SelectAnswerDetailActivity.this, "name", "").toString());
            commentEntity.setMessage(SPUtils.get(SelectAnswerDetailActivity.this, "message", "").toString());
            commentEntity.setSec_uid("sec_uid");
            commentEntity.setReplyNum("0");
            commentEntity.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            commentEntityList.add(1, commentEntity);
            txtOpen.setText("收起评论" + (++open) + "");
            adapter.notifyDataSetChanged();
            ToastUtils.showShortToast(SelectAnswerDetailActivity.this, "回复成功");
        }
    };

    private class CustomShareListener implements UMShareListener {
        private WeakReference<SelectAnswerDetailActivity> mActivity;

        private CustomShareListener(SelectAnswerDetailActivity activity) {
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
                Toast.makeText(mActivity.get(), " 分享失败", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showShortToast(SelectAnswerDetailActivity.this, "分享取消");
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
