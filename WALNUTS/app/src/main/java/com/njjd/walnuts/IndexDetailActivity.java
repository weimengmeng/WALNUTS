package com.njjd.walnuts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njjd.adapter.AnswerReplyAdapter;
import com.njjd.db.DBHelper;
import com.njjd.domain.AnswerEntity;
import com.njjd.domain.CommentEntity;
import com.njjd.domain.QuestionEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.AndroidBug5497Workaround;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImagePagerActivity;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.KeybordS;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.scrollablelayout.ScrollableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class IndexDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_quesTitle)
    TextView quesTitle;
    @BindView(R.id.top)
    LinearLayout topView;
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
    private QuestionEntity questionEntity;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private LayoutInflater inflater;
    private List<AnswerEntity> answerEntities = new ArrayList<>();
    private List<CommentEntity> commentEntities = new ArrayList<>();
    private AnswerEntity answerEntity;
    private CommentEntity commentEntity;
    private AnswerReplyAdapter answerReplyAdapter;
    private List<String> list = new ArrayList<>();
    private int current_group_id = 0, tempFocus = 0, tempAnswer = 0;
    private String comment_id = "";
    private PopupWindow popupWindow;
    private View mainView;
    private RadioButton layoutTop, layoutTime;
    private String tempOrder = "time";
    private AnswerEntity tempAnswerInfo;
    private CommentEntity tempComment;
    private String content="";
    private int currentId=0;
    @Override
    public int bindLayout() {
        return R.layout.activity_index_detail;
    }

    @Override
    public void initView(View view) {
        AndroidBug5497Workaround.assistActivity(this);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        Bundle bundle = getIntent().getBundleExtra("question");
        questionEntity = (QuestionEntity) bundle.get("question");
        back.setText("返回");
        if (getIntent().getStringExtra("type").equals("1")) {
            txtTitle.setText("问题详情");
            findViewById(R.id.txt_sort).setVisibility(View.VISIBLE);
        } else {
            txtTitle.setText("通知详情");
            comment_id = getIntent().getStringExtra("comment_id");
            findViewById(R.id.txt_sort).setVisibility(View.GONE);
        }
        quesTitle.setText(questionEntity.getTitle());
        txtAnswerNum.setText("回答 " + Float.valueOf(questionEntity.getAnswerNum()).intValue());
        txtFocusNum.setText("关注 " + Float.valueOf(questionEntity.getFocusNum()).intValue());
        tempFocus = Float.valueOf(questionEntity.getFocusNum()).intValue();
        tempAnswer = Float.valueOf(questionEntity.getAnswerNum()).intValue();
        txtContent.setText(questionEntity.getContent());
        if (questionEntity.getIsFocus() == 1) {
            txtFocus.setText("取消关注");
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
                        Intent intent = new Intent(IndexDetailActivity.this, ImagePagerActivity.class);
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
        exListVIew.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            AnswerReplyAdapter.CURRENT_PAGE++;
                            getAnswerList();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
        exListVIew.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                lvRply.setVisibility(View.VISIBLE);
                currentId=groupPosition;
                etContent.setHint("回复"+answerEntities.get(groupPosition).getCommentEntityList().get(childPosition).getName());
                KeybordS.openKeybord(etContent,IndexDetailActivity.this);
                tempAnswerInfo=answerEntities.get(groupPosition);
                tempComment=answerEntities.get(groupPosition).getCommentEntityList().get(childPosition);
                return false;
            }
        });
    }

    private void getDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", questionEntity.getQuestionId());
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
            questionEntity.setIsFocus(object.getInt("stat"));
            if (object.isNull("label_name") || object.isNull("label_id")) {
                return;
            }
            questionEntity.setTag(object.getString("label_name"));
            questionEntity.setTag_id(object.getString("label_id"));
            if (object.getInt("stat") == 1) {
                txtFocus.setText("取消关注");
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
                        Intent intent = new Intent(IndexDetailActivity.this, TagActivity.class);
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getAnswerList() {
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", Float.valueOf(questionEntity.getQuestionId()).intValue());
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
                    if (getIntent().getStringExtra("type").equals("2")) {
                        if (String.valueOf(Float.valueOf(answerEntity.getAnwerId()).intValue()).equals(String.valueOf(Float.valueOf(comment_id).intValue()))) {
                            answerEntities.add(answerEntity);
                        }
                    } else {
                        answerEntities.add(answerEntity);
                    }
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
        LogUtils.d(map.toString());
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

    @OnClick({R.id.back, R.id.txt_focus, R.id.img_answer, R.id.txt_sort,R.id.btn_reply,R.id.btn_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.txt_sort:
                popupWindow.showAsDropDown(findViewById(R.id.txt_sort), 0, 0);
                break;
            case R.id.txt_focus:
                addFocus();
                break;
            case R.id.btn_cancle:
                   KeybordS.closeKeybord(etContent,IndexDetailActivity.this);
               lvRply.setVisibility(View.GONE);
                break;
            case R.id.btn_reply:
                if(etContent.getText().toString().trim().equals("")){
                    ToastUtils.showShortToast(IndexDetailActivity.this,"请输入回复内容");
                    return ;
                }
                addCommentF(etContent.getText().toString().trim());
                KeybordS.closeKeybord(etContent,IndexDetailActivity.this);
                etContent.setText("");
                break;
            case R.id.img_answer:
                Intent intent = new Intent(this, AnswerActivity.class);
                intent.putExtra("quesId", questionEntity.getQuestionId());
                intent.putExtra("quesTitle", questionEntity.getTitle());
                startActivity(intent);
                break;
        }
    }
    private void addCommentF(String content){
        this.content=content;
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", questionEntity.getQuestionId());
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("content", content);
        if(tempComment.getSec_content().equals("sec_content")){
            map.put("comment_id", tempComment.getCommentId());
            map.put("sec_comment_id",tempAnswerInfo.getAnwerId());
        }else{
            map.put("comment_id", tempComment.getCommentId());
            map.put("sec_comment_id",tempAnswerInfo.getAnwerId());
        }
        map.put("token",SPUtils.get(this,"token","").toString());
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(commentListener, this, false, false), map);
        HttpManager.getInstance().pubComment(postEntity);
    }
    HttpOnNextListener commentListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            commentEntity=new CommentEntity();
            commentEntity.setContent(content);
            commentEntity.setHead(SPUtils.get(IndexDetailActivity.this,"head","").toString());
            commentEntity.setName(SPUtils.get(IndexDetailActivity.this,"name","").toString());
            commentEntity.setMessage(SPUtils.get(IndexDetailActivity.this,"message","").toString());
            commentEntity.setSec_uid("sec_uid");
            commentEntity.setReplyNum("0");
            commentEntity.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            answerEntities.get(currentId).getCommentEntityList().add(1,commentEntity);
            answerReplyAdapter.notifyDataSetChanged();
            ToastUtils.showShortToast(IndexDetailActivity.this, "回复成功");
        }
    };
    private void addFocus() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", ""));
        map.put("article_id", Float.valueOf(questionEntity.getQuestionId()).intValue());
        //0 取消关注 1 关注
        map.put("select", questionEntity.getIsFocus() == 0 ? 1 : 0);
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(focusListener, this, true, false), map);
        HttpManager.getInstance().focusQuestion(postEntity);
    }

    HttpOnNextListener focusListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            ToastUtils.showShortToast(IndexDetailActivity.this, questionEntity.getIsFocus() == 0 ? "成功关注" : "取消关注");
            txtFocus.setText(questionEntity.getIsFocus() == 0 ? "取消关注" : "+关注问题");
            txtFocusNum.setText("关注 " + (questionEntity.getIsFocus() == 0 ? ++tempFocus : --tempFocus));
            questionEntity.setIsFocus(questionEntity.getIsFocus() == 0 ? 1 : 0);
            DBHelper.getInstance().getmDaoSession().getQuestionEntityDao().insertOrReplace(questionEntity);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_hot:
                tempOrder = "hot";
                AnswerReplyAdapter.CURRENT_PAGE = 1;
                getAnswerList();
                popupWindow.dismiss();
                break;
            case R.id.rb_time:
                tempOrder = "time";
                AnswerReplyAdapter.CURRENT_PAGE = 1;
                getAnswerList();
                popupWindow.dismiss();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
