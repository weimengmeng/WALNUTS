package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njjd.adapter.ArticleConversionAdapter;
import com.njjd.domain.CommentEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.AndroidBug5497Workaround;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.KeybordS;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/10/23.
 */

public class ArticleComReplyActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.list_reply)
    ListView listReply;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    CircleImageView imgAnswerHead;
    TextView txtAnswerName;
    TextView txtAnswerMessage;
    TextView txtAnswerContent;
    @BindView(R.id.btn_add_help2)
    TextView btnAddHelp2;
    private String content = "";
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
    private CommentEntity tempComment, entity;
    private ArticleConversionAdapter adapter;
    private List<CommentEntity> list = new ArrayList<>();
    private View headerView;
    private int type = 0;

    @Override
    public int bindLayout() {
        return R.layout.activity_comment_reply;
    }

    @Override
    public void initView(View view) {
        AndroidBug5497Workaround.assistActivity(this);
        back.setText("会话列表");
        txtTitle.setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getBundleExtra("comment");
        entity = (CommentEntity) bundle.get("comment");
        getComments(Float.valueOf(entity.getCommentId()).intValue() + "");
        entity.setSec_uid("sec_uid");
        adapter = new ArticleConversionAdapter(this, list);
        headerView = LayoutInflater.from(this).inflate(R.layout.layout_comment, null);
        listReply.setAdapter(adapter);
        if (getIntent().getStringExtra("type").equals("0")) {
            headerView.setVisibility(View.VISIBLE);
            listReply.addHeaderView(headerView);
            imgAnswerHead = (CircleImageView) headerView.findViewById(R.id.img_head);
            GlideImageLoder.getInstance().displayImage(this, entity.getHead(), imgAnswerHead);
            imgAnswerHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ArticleComReplyActivity.this, PeopleInfoActivity.class);
                    intent.putExtra("uid", entity.getCommentUId());
                    startActivity(intent);
                }
            });
            txtAnswerName = (TextView) headerView.findViewById(R.id.txt_name);
            txtAnswerMessage = (TextView) headerView.findViewById(R.id.txt_message);
            txtAnswerContent = (TextView) headerView.findViewById(R.id.txt_content);
            ((TextView) headerView.findViewById(R.id.txt_reply)).setText("立即评论");
            headerView.findViewById(R.id.txt_reply).setOnClickListener(this);
            txtAnswerName.setText(entity.getName());
            txtAnswerMessage.setText(entity.getMessage());
            txtAnswerContent.setText(entity.getContent());
        } else {
            headerView.setVisibility(View.GONE);
            btnAddHelp2.setText("查看原文");
            btnAddHelp2.setVisibility(View.VISIBLE);
            btnAddHelp2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ArticleComReplyActivity.this, ColumnDetailActivity.class);
                    intent.putExtra("article_id", getIntent().getStringExtra("article_id"));
                    startActivity(intent);
                }
            });
        }
        listReply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (headerView.getVisibility() == View.VISIBLE) {
                    if (position == 0) {
                        type = 0;
                        headerView.findViewById(R.id.txt_reply).performClick();
                    } else {
                        type = 1;
                        tempComment = list.get(position - 1);
                        lvReply.setVisibility(View.VISIBLE);
                        mask.setVisibility(View.VISIBLE);
                        etContent.requestFocus();
                        btnCancle.setText("取消回复");
                        btnReply.setText("立即回复");
                        etContent.setHint("回复" + list.get(position - 1).getName());
                        KeybordS.openKeybord(etContent, ArticleComReplyActivity.this);
                    }
                } else {
                    type = 1;
                    tempComment = list.get(position);
                    lvReply.setVisibility(View.VISIBLE);
                    mask.setVisibility(View.VISIBLE);
                    etContent.requestFocus();
                    btnCancle.setText("取消回复");
                    btnReply.setText("立即回复");
                    etContent.setHint("回复" + list.get(position).getName());
                    KeybordS.openKeybord(etContent, ArticleComReplyActivity.this);
                }
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComments(Float.valueOf(entity.getCommentId()).intValue() + "");
            }
        });
    }

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
                refresh.setRefreshing(false);
                list.clear();
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    tempComment = new CommentEntity(object);
                    list.add(tempComment);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void addCommentF(String content) {
        this.content = content;
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", getIntent().getStringExtra("article_id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("content", content);
        map.put("comment_id", Float.valueOf(tempComment.getCommentId()).intValue());
        map.put("sec_comment_id", Float.valueOf(entity.getCommentId()));
        map.put("token", SPUtils.get(this, "token", "").toString());
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(commentListener, this, false, false), map);
        HttpManager.getInstance().pubComment(postEntity);
    }

    HttpOnNextListener commentListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            CommentEntity temp = new CommentEntity();
            temp.setContent(content);
            temp.setHead(SPUtils.get(ArticleComReplyActivity.this, "head", "").toString());
            temp.setName(SPUtils.get(ArticleComReplyActivity.this, "name", "").toString());
            temp.setMessage(SPUtils.get(ArticleComReplyActivity.this, "message", "").toString());
            temp.setReplyNum("0");
            if (type == 0) {
                temp.setSec_uid("sec_uid");
            } else {
                temp.setSec_uid(tempComment.getCommentUId());
            }
            temp.setSec_content(tempComment.getContent());
            temp.setSec_headimgs(tempComment.getHead());
            temp.setSec_uname(tempComment.getName());
            temp.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            list.add(0, temp);
            adapter.notifyDataSetChanged();
            ToastUtils.showShortToast(ArticleComReplyActivity.this, "回复成功");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_reply:
                type = 0;
                tempComment = entity;
                lvReply.setVisibility(View.VISIBLE);
                mask.setVisibility(View.VISIBLE);
                etContent.requestFocus();
                btnCancle.setText("取消评论");
                btnReply.setText("立即评论");
                etContent.setHint("回复" + entity.getName());
                KeybordS.openKeybord(etContent, ArticleComReplyActivity.this);
                break;
            case R.id.mask:
                mask.setVisibility(View.GONE);
                lvReply.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, ArticleComReplyActivity.this);
                break;
            case R.id.btn_cancle:
                mask.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, ArticleComReplyActivity.this);
                lvReply.setVisibility(View.GONE);
                break;
            case R.id.btn_reply:
                if (etContent.getText().toString().trim().equals("")) {
                    ToastUtils.showShortToast(ArticleComReplyActivity.this, "请输入回复内容");
                    return;
                }
                addCommentF(etContent.getText().toString().trim());
                lvReply.setVisibility(View.GONE);
                mask.setVisibility(View.GONE);
                KeybordS.closeKeybord(etContent, ArticleComReplyActivity.this);
                etContent.setText("");
                break;
        }
    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
