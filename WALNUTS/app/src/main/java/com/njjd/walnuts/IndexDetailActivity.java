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
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.retrofit.mywidget.LoadingDialog;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.adapter.AnswerReplyAdapter;
import com.njjd.domain.AnswerEntity;
import com.njjd.domain.CommentEntity;
import com.njjd.domain.QuestionEntity;
import com.njjd.domain.ReplyEntity;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.MyScrollView;
import com.njjd.utils.ToastUtils;
import com.scrollablelayout.ScrollableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class IndexDetailActivity extends BaseActivity{
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
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NotoSansHans-Medium.ttf");
//        quesTitle.setTypeface(typeface);
        inflater = LayoutInflater.from(this);
        if (!"".equals(questionEntity.getPhoto())) {
            lvImg.setVisibility(View.VISIBLE);
            String[] imgs = questionEntity.getPhoto().split(",");
            for (int i = 0; i < imgs.length; i++) {
                relativeLayout = (RelativeLayout) inflater.inflate(R.layout.layout_img, null);
                imageView = (ImageView) relativeLayout.findViewById(R.id.img);
                imageView.setId(i);
                GlideImageLoder.getInstance().displayImage(this, imgs[i], imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShortToast(IndexDetailActivity.this, v.getId() + "");
                    }
                });
                lvImg.addView(relativeLayout);
            }
        } else {
            lvImg.setVisibility(View.GONE);
        }
        String[] string=questionEntity.getTag().split(",");
        LinearLayout linearLayout;
        TextView tag;
        for(int i=0;i<string.length;i++){
            linearLayout=(LinearLayout) inflater.inflate(R.layout.tag_item,null);
            tag = (TextView) linearLayout.findViewById(R.id.txt_tag);
            tag.setText(string[i]);
            tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(IndexDetailActivity.this, TagActivity.class));
                }
            });
            lvTag.addView(linearLayout);
        }
        scrollView.getHelper().setCurrentScrollableContainer(root);
        root.smoothScrollTo(0,0);
        getAnswerList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getAnswerList() {
        list.add("看哪个方法成功率高啦，销售最重要的还是业绩，如果按照你的方法做业绩好的话，领导也说不出来什么");
        list.add("做过2年电销，去年刚转行；\n" +
                "我是比较支持你的做法的，因为对于电销来说，固然数量很重要，但是无前提工作的乱打只会降低成功率，让自己意志消沉；\n" +
                "我们都接到过同行的电话，推销商铺炒股等等；很多人上来就问需不需要，这样的封闭式回答很容易被客户否掉，或者有些人听了就直接挂掉了；\n" +
                "开口的前三句或者极端些说，第一句话能不能勾引主客户的兴趣，是最重要的。这个技巧的关键就是对他了解有多少。你了解的越多就，讲的和他相关性越高，就越容易抓住他的兴趣，提高成功率。");
        list.add("听你领导的吧！在哪混就要服从命令！");
        list.add("不了解你的行业，像我们做保险类的，目前来说，还是以数量取胜的");
        list.add("我也遇到过你的问题，但是我和你正好相反，我是想抓紧时间多大，我领导则是让我多收集资料！关键是要加班收集，所以我不干了");
        list.add("还做电销？现在成功率太低 劝你转行");
        list.add("曾经做电销做到失声。。。。");
        list.add("还是听领导的吧小伙子 人家肯定比你打的多");
        for (int i = 0; i < list.size(); i++) {
            answerEntity = new AnswerEntity(i + "", i + "", "",
                    "回答" + i, "民老我是国公" + i, list.get(i), "40" + i,
                    "30" + i, (15 + i) + "分钟前");
            commentEntities.clear();
            for (int j = 0; j < 3; j++) {
                commentEntity = new CommentEntity(j + "", j + "", "", "回答" + j,
                        "回答签名" + j, "这个回答好，我怎么没想到呢" + j, "68" + j, (j + 3) + "分钟前");
                commentEntities.add(commentEntity);
            }
            answerEntity.setCommentEntityList(commentEntities);
            answerEntities.add(answerEntity);
        }
        answerReplyAdapter = new AnswerReplyAdapter(this, answerEntities);
        exListVIew.setAdapter(answerReplyAdapter);
    }

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
               Intent intent=new Intent(this,AnswerActivity.class);
                intent.putExtra("quesId",questionEntity.getQuestionId());
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
