package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.njjd.adapter.AnswerReplyAdapter;
import com.njjd.db.DBHelper;
import com.njjd.domain.AnswerEntity;
import com.njjd.domain.CommentEntity;
import com.njjd.domain.QuestionEntity;
import com.njjd.domain.ReplyEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImagePagerActivity;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.scrollablelayout.ScrollableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class IndexDetailActivity extends BaseActivity {
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
    @BindView(R.id.scrollLayout)
    ScrollableLayout scrollView;
    @BindView(R.id.lv_tag)
    LinearLayout lvTag;
    private QuestionEntity questionEntity;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private LayoutInflater inflater;
    private List<AnswerEntity> answerEntities = new ArrayList<>();
    private List<CommentEntity> commentEntities = new ArrayList<>();
    private List<ReplyEntity> replyEntities = new ArrayList<>();
    private AnswerEntity answerEntity;
    private CommentEntity commentEntity;
    private ReplyEntity replyEntity;
    private AnswerReplyAdapter answerReplyAdapter;
    private List<String> list = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_index_detail;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        Bundle bundle = getIntent().getBundleExtra("question");
        questionEntity = (QuestionEntity) bundle.get("question");
        back.setText("关闭");
        txtTitle.setText("问题详情");
        quesTitle.setText(questionEntity.getTitle());
        txtAnswerNum.setText("回答 " + questionEntity.getAnswerNum());
        txtFocusNum.setText("关注 " + questionEntity.getFocusNum());
        txtContent.setText(questionEntity.getContent());
        inflater = LayoutInflater.from(this);
        if (!"".equals(questionEntity.getPhoto())) {
            LogUtils.d(questionEntity.getPhoto());
            lvImg.setVisibility(View.VISIBLE);
            final String[] imgs = questionEntity.getPhoto().split(",");
            final ArrayList<String> list1 = new ArrayList<>();
            for (int i = 0; i < imgs.length; i++) {
                list1.add(imgs[i].replace("\"", ""));
                relativeLayout = (RelativeLayout) inflater.inflate(R.layout.layout_img, null);
                imageView = (ImageView) relativeLayout.findViewById(R.id.img);
                imageView.setId(i);
                GlideImageLoder.getInstance().displayImage(this, imgs[i].replace("\"", ""), imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(IndexDetailActivity.this, ImagePagerActivity.class);
                        intent.putStringArrayListExtra(
                                ImagePagerActivity.EXTRA_IMAGE_URLS, list1);
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
        answerReplyAdapter = new AnswerReplyAdapter(this, answerEntities);
        exListVIew.setAdapter(answerReplyAdapter);
        exListVIew.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                getComments(answerEntities.get(groupPosition).getAnwerId());
                ToastUtils.showShortToast(IndexDetailActivity.this,answerEntities.get(groupPosition).getAnwerId());
                return false;
            }
        });
        getDetail();
    }

    private void getDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", questionEntity.getQuestionId());
        map.put("uid", SPUtils.get(this, "userId", ""));
        LogUtils.d(map.toString());
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
            questionEntity.setIsFocus(object.getInt("stat"));
            if (object.isNull("label_name") || object.isNull("label_id")) {
                return;
            }
            questionEntity.setTag(object.getString("label_name"));
            questionEntity.setTag_id(object.getString("label_id"));
            if (object.getInt("stat") == 1) {
                txtFocus.setText("已关注");
                txtFocus.setEnabled(false);
            }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getAnswerList() {
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", questionEntity.getQuestionId());
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("order", "time");
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getCommentListener, this, false, false), map);
        HttpManager.getInstance().getCommentList(postEntity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAnswerList();
    }

    HttpOnNextListener getCommentListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonArray array = JSONUtils.getAsJsonArray(o);
            JsonObject object;
            for (int i = 0; i < array.size(); i++) {
                object = array.get(i).getAsJsonObject();
                answerEntity = new AnswerEntity(object.get("id").getAsString(), object.get("uid").getAsString(), object.get("headimgs").getAsString(),
                        object.get("uname").getAsString(), object.get("introduction").getAsString(), object.get("content").getAsString(), object.get("point_num").getAsString(),
                        object.get("answer_num").getAsString(), object.get("change_time").getAsString(),object.get("c_stat").getAsString(),object.get("p_stat").getAsString());
                commentEntities.clear();
                for (int j = 0; j < 3; j++) {
                    commentEntity = new CommentEntity(j + "", j + "", "", "回答" + j,
                            "回答签名" + j, "这个回答好，我怎么没想到呢" + j, "68" + j, (j + 3) + "分钟前");
                    commentEntities.add(commentEntity);
                }
                answerEntity.setCommentEntityList(commentEntities);
                answerEntities.add(answerEntity);
            }
            answerReplyAdapter.notifyDataSetChanged();
        }
    };

    @OnClick({R.id.back, R.id.txt_focus, R.id.img_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.txt_focus:
                ToastUtils.showShortToast(this, "添加关注");
                break;
            case R.id.img_answer:
                Intent intent = new Intent(this, AnswerActivity.class);
                intent.putExtra("quesId", questionEntity.getQuestionId());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
