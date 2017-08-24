package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njjd.domain.QuestionEntity;
import com.njjd.domain.SaveEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.DateUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImagePagerActivity;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.scrollablelayout.ScrollableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/7/12.
 */

public class SaveDetailActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top_view)
    RelativeLayout topView;
    @BindView(R.id.lv_tag)
    LinearLayout lvTag;
    @BindView(R.id.txt_quesTitle)
    TextView txtQuesTitle;
    @BindView(R.id.lv_img)
    LinearLayout lvImg;
    @BindView(R.id.txt_content)
    TextView txtcontent;
    @BindView(R.id.txt_answerNum)
    TextView txtAnswerNum;
    @BindView(R.id.txt_focusNum)
    TextView txtFocusNum;
    @BindView(R.id.txt_focus)
    TextView txtFocus;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_message)
    TextView txtMessage;
    @BindView(R.id.txt_agree)
    TextView txtAgree;
    @BindView(R.id.txt_content1)
    TextView txtContent1;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_save)
    TextView txtSave;
    @BindView(R.id.txt_report)
    TextView txtReport;
    @BindView(R.id.txt_open)
    TextView txtOpen;
    @BindView(R.id.lv_root)
    ScrollView lvRoot;
    @BindView(R.id.scrollLayout)
    ScrollableLayout scrollLayout;
    private SaveEntity saveEntity;
    private LayoutInflater inflater;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    @Override
    public int bindLayout() {
        return R.layout.activity_save_detail;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        back.setText("我的");
        txtTitle.setText("收藏详情");
        saveEntity=(SaveEntity)getIntent().getBundleExtra("save").get("save");
        txtQuesTitle.setText(saveEntity.getTitle());
        txtAnswerNum.setText("回答 " + Float.valueOf(saveEntity.getArticle_answer_num()).intValue());
        txtFocusNum.setText("关注 " + Float.valueOf(saveEntity.getArticle_follow_num()).intValue());
        txtContent1.setText(saveEntity.getArticle_content());
        inflater = LayoutInflater.from(this);
        if (!"".equals(saveEntity.getArticle_imgs())) {
            lvImg.setVisibility(View.VISIBLE);
            final String[] imgs = saveEntity.getArticle_imgs().split(",");
            final ArrayList<String> list1 = new ArrayList<>();
            for (int i = 0; i < imgs.length; i++) {
                list1.add(HttpManager.BASE_URL2+imgs[i].replace("\"", ""));
                relativeLayout = (RelativeLayout) inflater.inflate(R.layout.layout_img, null);
                imageView = (ImageView) relativeLayout.findViewById(R.id.img);
                imageView.setId(i);
                GlideImageLoder.getInstance().displayImage(this, HttpManager.BASE_URL2+imgs[i].replace("\"", ""), imageView);
                final int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SaveDetailActivity.this, ImagePagerActivity.class);
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
        scrollLayout.getHelper().setCurrentScrollableContainer(lvRoot);
        lvRoot.smoothScrollTo(0, 0);
        GlideImageLoder.getInstance().displayImage(this,saveEntity.getComment_uid_headimg(), imgHead);
        txtName.setText(saveEntity.getComment_uid_name());
        txtMessage.setText(saveEntity.getComment_uid_introduction());
        txtAgree.setText(Float.valueOf(saveEntity.getComment_point_num()).intValue()+"");
        txtcontent.setText(saveEntity.getComment_content());
        txtOpen.setText("评论 " + Float.valueOf(saveEntity.getComment_collect_num()).intValue());
        if (saveEntity.getPoint_comment_stat().equals("1")) {
            txtAgree.setBackgroundResource(R.drawable.background_button_div_grey);
        } else {
            txtAgree.setBackgroundResource(R.drawable.background_button_div);
        }
        txtSave.setText("取消收藏");
        txtSave.setTag("0");
        ParsePosition pos = new ParsePosition(0);
        txtTime.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(saveEntity.getCollect_time(), pos)));
        getDetail();
    }
    private void getDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", saveEntity.getArticle_id());
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
            if (object.isNull("label_name") || object.isNull("label_id")) {
                return;
            }
            if (object.getInt("stat") == 1) {
                txtFocus.setText("取消关注");
                txtFocus.setTag("0");
            }else{
                txtFocus.setTag("1");
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
                        Intent intent = new Intent(SaveDetailActivity.this, TagActivity.class);
                        intent.putExtra("tag_id", v.getTag().toString());
                        intent.putExtra("name", ((TextView) v).getText().toString());
                        startActivity(intent);
                    }
                });
                lvTag.addView(linearLayout);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void addFocus(){
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token",SPUtils.get(this,"token",""));
        map.put("article_id", Float.valueOf(saveEntity.getArticle_id()).intValue());
        //0 取消关注 1 关注
        map.put("select",txtFocus.getTag().toString().equals("0")?0:1);
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(focusListener, this, true, false), map);
        HttpManager.getInstance().focusQuestion(postEntity);
    }
    HttpOnNextListener focusListener=new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            ToastUtils.showShortToast(SaveDetailActivity.this,txtFocus.getTag().toString().equals("0")?"成功关注":"取消成功");
            txtFocus.setTag(txtFocus.getTag().toString().equals("0")?"1":"0");
            txtFocus.setText(txtFocus.getTag().toString().equals("1")?"+关注问题":"取消关注");
        }
    };
    @OnClick({R.id.back,R.id.txt_more, R.id.txt_focus, R.id.img_answer,R.id.txt_agree,R.id.txt_save})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.txt_more:
                QuestionEntity entity=new QuestionEntity();
                entity.setIsFocus(Integer.valueOf(txtFocus.getTag().toString()));
                entity.setTitle(saveEntity.getTitle());
                entity.setContent(saveEntity.getArticle_content());
                entity.setPhoto(saveEntity.getArticle_imgs());
                entity.setFocusNum(saveEntity.getArticle_follow_num());
                entity.setAnswerNum(saveEntity.getArticle_answer_num());
                entity.setQuestionId(saveEntity.getArticle_id());
                intent = new Intent(this, IndexDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("question", entity);
                intent.putExtra("question", bundle);
                intent.putExtra("type","1");
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.txt_focus:
                addFocus();
                break;
            case R.id.txt_save:
                if(txtSave.getTag().toString().equals("1")) {
                    agreeOrSave("collect_comment_id");
                }else{
                    agreeOrSave("collect_comment_id_not");
                }
                break;
            case R.id.txt_agree:
                if(String.valueOf(Float.valueOf(saveEntity.getPoint_comment_stat()).intValue()).equals("0")) {
                    agreeOrSave("point_comment_id");
                }else{
                    agreeOrSave("point_comment_id_not");
                }
                break;
            case R.id.img_answer:
                intent = new Intent(this, AnswerActivity.class);
                intent.putExtra("quesId", saveEntity.getArticle_id());
                startActivity(intent);
                break;
        }
    }
    private void agreeOrSave(final String params){
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put(params, saveEntity.getComment_id());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                switch (params){
                    case "point_comment_id":
                        saveEntity.setPoint_comment_stat("1");
                        txtAgree.setBackgroundResource(R.drawable.background_button_div_grey);
                        txtAgree.setText((Integer.valueOf(txtAgree.getText().toString())+1)+"");
                        break;
                    case "point_comment_id_not":
                        saveEntity.setPoint_comment_stat("0");
                        txtAgree.setBackgroundResource(R.drawable.background_button_div);
                        txtAgree.setText((Integer.valueOf(txtAgree.getText().toString())-1)+"");
                        break;
                    case "collect_comment_id":
                        txtSave.setText("取消收藏");
                        txtSave.setTag("0");
                        break;
                    case "collect_comment_id_not":
                        txtSave.setText("收藏");
                        txtSave.setTag("1");
                        break;
                }
            }
        }, this, true, false), map);
        HttpManager.getInstance().agreeOrPraise(postEntity);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
