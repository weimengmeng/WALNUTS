package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.listener.ProgressListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.application.AppAplication;
import com.njjd.application.ConstantsVal;
import com.njjd.domain.QuestionEntity;
import com.njjd.domain.TagEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.AndroidBug5497Workaround;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lankton.flowlayout.FlowLayout;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by mrwim on 17/7/21.
 */

public class AskKindActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.tag_list)
    LinearLayout tagLayout;
    private Bundle bundle;
    private List<TagEntity> entities;
    private LayoutInflater inflater;
    private LinearLayout linearLayout;
    private FlowLayout flowLayout;
    private TagEntity entity;
    private List<String> list = new ArrayList<>();
    private List<FlowLayout> layoutList = new ArrayList<>();
    private int current = 0;
    @Override
    public int bindLayout() {
        return R.layout.activity_askkind;
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void initView(View view) {
        back.setText("提问");
        txtTitle.setText("问题话题");
        bundle = getIntent().getBundleExtra("question");
        inflater = LayoutInflater.from(this);
        entities = CommonUtils.getInstance().getTagsList();
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.setMargins(0, 0, 21, 0);
        for (int i = 0; i < entities.size(); i++) {
            entity = entities.get(i);
            if (entity.getCode().equals("1.0")) {
                linearLayout = (LinearLayout) inflater.inflate(R.layout.item_tag_list, null);
                ((TextView) linearLayout.findViewById(R.id.label_name)).setText(entity.getName());
                flowLayout = (FlowLayout) linearLayout.findViewById(R.id.flowlayout);
                tagLayout.addView(linearLayout);
                layoutList.add(flowLayout);
            } else {
                TextView tv = new TextView(this);
                tv.setText(entity.getName());
                tv.setTextColor(getResources().getColor(R.color.tag));
                tv.setTextSize(14);
                tv.setTag(Float.valueOf(entity.getId()).intValue()+"");
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setBackgroundResource(R.drawable.round_textview);
                if("2".equals(getIntent().getStringExtra("type"))){
                    String[] labelIds=getIntent().getStringExtra("ids").split(",");
                    for (int j=0;j<labelIds.length;j++){
                        if(Float.valueOf(labelIds[j]).intValue()==(Float.valueOf(entity.getId()).intValue())){
                            tv.setTextColor(getResources().getColor(R.color.white));
                            tv.setBackgroundResource(R.drawable.round_textview1);
                            break;
                        }
                    }
                }
                tv.setPadding(25, 10, 25, 10);
                tv.setOnClickListener(this);
                flowLayout.addView(tv, lp);
            }
        }
        if("2".equals(getIntent().getStringExtra("type"))){
            String[] labelIds=getIntent().getStringExtra("ids").split(",");
            for (int j=0;j<labelIds.length;j++){
                list.add(Float.valueOf(labelIds[j]).intValue()+"");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_submit:
                if("2".equals(getIntent().getStringExtra("type"))){
                    editQuestion();
                    return;
                }
                if (list.size() < 1) {
                    ToastUtils.showShortToast(this, "至少选择一个话题");
                    return;
                }
                MobclickAgent.onEvent(this, ConstantsVal.PUBQUESTION);
                pubQuesion();
                break;
        }
    }
    private void editQuestion(){
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", ""));
        map.put("title", bundle.getString("title"));
        map.put("content", bundle.getString("content"));
            String temp = "";
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    temp += list.get(i) + ",";
                } else {
                    temp += list.get(i);
                }
            }
            map.put("label_id", temp);
        map.put("article_id", Float.valueOf(getIntent().getStringExtra("article_id")).intValue());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                AskActivity.activity.finish();
                finish();
            }
        }, this, true, false), map);
        HttpManager.getInstance().editQuestion(postEntity);
    }
    private void pubQuesion() {
        String temp = "";
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                temp += list.get(i) + ",";
            } else {
                temp += list.get(i);
            }
        }
        List<String> strings = bundle.getStringArrayList("imgs");
        if (strings != null&&strings.size()>0) {
            List<File> files = new ArrayList<>();
            File file;
            for (int i = 0; i < strings.size(); i++) {
                file = new File(strings.get(i));
                files.add(file);
            }
            SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), files);
            HttpManager.getInstance().pubQuestion(postEntity, new MyUploadListener(), SPUtils.get(this, "userId", "").toString(), SPUtils.get(AppAplication.getContext(), "token", "").toString(), bundle.getString("title"),
                    bundle.getString("content"), temp);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("uid", SPUtils.get(this, "userId", ""));
            map.put("token", SPUtils.get(this, "token", ""));
            map.put("title", bundle.getString("title"));
            map.put("content", bundle.getString("content"));
            map.put("label_id", temp);
            SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), map);
            HttpManager.getInstance().pubQuestion2(postEntity);
        }
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        MobclickAgent.onEvent(this, ConstantsVal.PUBRESULT);
        List<String> strings = bundle.getStringArrayList("imgs");
        Intent intent = new Intent();
        intent.setAction(ConstantsVal.REFRESH);
        sendBroadcast(intent);
        if (strings != null&&strings.size()>0) {
            File file;
            for (int i = 0; i < strings.size(); i++) {
                file = new File(strings.get(i));
                if(file.exists()){
                    file.delete();
                }
            }
        }
        ToastUtils.showShortToast(this, "发布成功");
        AskActivity.activity.finish();
        finish();
    }

    class MyUploadListener implements ProgressListener {
        @Override
        public void onProgress(long progress, long total, boolean done) {
        }
    }

    @Override
    public void onClick(View v) {
        FlowLayout linearLayout;
        int temp = 0;
        if (list.contains(v.getTag())) {
            v.setBackgroundResource(R.drawable.round_textview);
            ((TextView)v).setTextColor(getResources().getColor(R.color.tag));
            v.setPadding(25, 10, 25, 10);
            list.remove(v.getTag());
        } else {
            if(list.size()>3){
                ToastUtils.showShortToast(this,"最多可以选择4个话题哦");
                return;
            }
            v.setBackgroundResource(R.drawable.round_textview1);
            ((TextView)v).setTextColor(getResources().getColor(R.color.white));
            list.add(v.getTag().toString());
            v.setPadding(25, 10, 25, 10);
        }
        if (current == 0) {
            for (int i = 0; i < layoutList.size(); i++) {
                linearLayout = layoutList.get(i);
                for (int n = 0; n < linearLayout.getChildCount(); n++) {
                    if (v.getTag().toString().equals(linearLayout.getChildAt(n).getTag().toString())) {
                        temp = i;
                        break;
                    }
                }
            }
            for (int i = 0; i < layoutList.size(); i++) {
                if (temp != i) {
                    linearLayout = layoutList.get(i);
                    for (int n = 0; n < linearLayout.getChildCount(); n++) {
                        linearLayout.getChildAt(n).setEnabled(false);
                        ((TextView) linearLayout.getChildAt(n)).setTextColor(getResources().getColor(R.color.txt_color));
                        (linearLayout.getChildAt(n)).setBackgroundResource(R.drawable.round_textview2);
                        (linearLayout.getChildAt(n)).setPadding(25, 10, 25, 10);
                    }
                }
            }
            current = 1;
        }
        if (list.size() == 0) {
            for (int i = 0; i < layoutList.size(); i++) {
                linearLayout = layoutList.get(i);
                for (int n = 0; n < linearLayout.getChildCount(); n++) {
                    linearLayout.getChildAt(n).setEnabled(false);
                    ((TextView) linearLayout.getChildAt(n)).setTextColor(getResources().getColor(R.color.tag));
                    (linearLayout.getChildAt(n)).setBackgroundResource(R.drawable.round_textview);
                    (linearLayout.getChildAt(n)).setPadding(25, 10, 25, 10);
                    (linearLayout.getChildAt(n)).setEnabled(true);
                }
            }
        }
        current=0;
    }
}
