package com.njjd.walnuts;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.njjd.adapter.AnswerReplyAdapter;
import com.njjd.domain.AnswerEntity;
import com.njjd.domain.CommentEntity;
import com.njjd.domain.QuestionEntity;
import com.njjd.domain.ReplyEntity;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.MyScrollView;
import com.njjd.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class IndexDetailActivity extends BaseActivity implements MyScrollView.OnScrollListener{
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_top_quesTitle)
    TextView txtQuesTitle;
    @BindView(R.id.lv_topTitle)
    LinearLayout lvTopTiTle;
    @BindView(R.id.lv_top)
    LinearLayout lvTop;
    @BindView(R.id.lv_img)
    LinearLayout lvImg;
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
    @BindView(R.id.scrollView)
    MyScrollView scrollView;
    private QuestionEntity questionEntity;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private LayoutInflater inflater;
    private List<AnswerEntity> answerEntities=new ArrayList<>();
    private List<CommentEntity> commentEntities=new ArrayList<>();
    private List<ReplyEntity> replyEntities=new ArrayList<>();
    private AnswerEntity answerEntity;
    private CommentEntity commentEntity;
    private ReplyEntity replyEntity;
    private AnswerReplyAdapter answerReplyAdapter;
    @Override
    public int bindLayout() {
        return R.layout.activity_index_detail;
    }

    @Override
    public void initView(View view) {
        Bundle bundle = getIntent().getBundleExtra("question");
        questionEntity = (QuestionEntity) bundle.get("question");
        back.setText("关闭");
        txtTitle.setText("问题详情");
        txtQuesTitle.setText(questionEntity.getTitle());
        txtAnswerNum.setText("回答 " + questionEntity.getAnswerNum());
        txtFocusNum.setText("关注 " + questionEntity.getFocusNum());
        txtContent.setText(questionEntity.getContent());
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NotoSansHans-Medium.ttf");
        txtContent.setTypeface(typeface);
        inflater=LayoutInflater.from(this);
        if (!"".equals(questionEntity.getPhoto())) {
            lvImg.setVisibility(View.VISIBLE);
            String[] imgs= questionEntity.getPhoto().split(",");
            for(int i=0;i<imgs.length;i++){
                relativeLayout=(RelativeLayout) inflater.inflate(R.layout.layout_img,null);
                imageView=(ImageView) relativeLayout.findViewById(R.id.img);
                imageView.setId(i);
                GlideImageLoder.getInstance().displayImage(this,imgs[i],imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShortToast(IndexDetailActivity.this,v.getId()+"");
                    }
                });
                lvImg.addView(relativeLayout);
            }
        } else {
            lvImg.setVisibility(View.GONE);
        }
        findViewById(R.id.parent_layout).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(scrollView.getScrollY());
            }
        });
        scrollView.setOnScrollListener(this);
        getAnswerList();
        scrollView.smoothScrollTo(0,0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void getAnswerList(){
        for(int i=0;i<4;i++){
            answerEntity=new AnswerEntity(i+"", i+"", "",
                    "王者"+i, "我是国民老公"+i, "这个问题不错，值得学习参考"+i, "40"+i,
                    "30"+i,(15+i)+"分钟前");
            commentEntities.clear();
            for(int j=0;j<3;j++){
                commentEntity=new CommentEntity(j+"",j+"", "", "回答"+j,
                        "回答签名"+j,"这个回答好，我怎么没想到呢"+j, "68"+j,(j+3)+"分钟前");
                commentEntities.add(commentEntity);
            }
            answerEntity.setCommentEntityList(commentEntities);
            answerEntities.add(answerEntity);
        }
        answerReplyAdapter=new AnswerReplyAdapter(this,answerEntities);
        exListVIew.setAdapter(answerReplyAdapter);
    }
    @OnClick({R.id.back, R.id.txt_focus, R.id.img_answer,R.id.txt_tag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.txt_focus:
                ToastUtils.showShortToast(this, "添加关注");
                break;
            case R.id.img_answer:
                ToastUtils.showShortToast(this, "回答问题");
                break;
            case R.id.txt_tag:
                startActivity(new Intent(this,TagActivity.class));
                break;
        }
    }

    @Override
    public void onScroll(int scrollY) {
        int mBuyLayout2ParentTop = Math.max(scrollY, lvTop.getTop());
        lvTopTiTle.layout(getResources().getDimensionPixelSize(R.dimen.margin_small),mBuyLayout2ParentTop,lvTop.getWidth()+getResources().getDimensionPixelSize(R.dimen.margin_litter_big), lvTopTiTle.getHeight()+mBuyLayout2ParentTop);
    }
}
