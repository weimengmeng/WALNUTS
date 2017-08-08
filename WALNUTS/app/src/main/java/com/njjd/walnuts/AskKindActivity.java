package com.njjd.walnuts;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.ProgressListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.domain.CommonEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

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
    @BindView(R.id.tag_list)
    LinearLayout tagLayout;
    private Bundle bundle;
    private List<CommonEntity> entities;
    private LayoutInflater inflater;
    private LinearLayout linearLayout;
    private FlowLayout flowLayout;
    private CommonEntity entity;
    private List<String> list=new ArrayList<>();
    @Override
    public int bindLayout() {
        return R.layout.activity_askkind;
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void initView(View view) {
        back.setText("提问");
        txtTitle.setText("问题标签");
        bundle=getIntent().getBundleExtra("question");
        inflater=LayoutInflater.from(this);
        entities= CommonUtils.getInstance().getTagsList();
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.setMargins(25, 0, 15, 0);
        for(int i=0;i<entities.size();i++){
            entity=entities.get(i);
            if(entity.getCode().equals("1")){
                linearLayout=(LinearLayout) inflater.inflate(R.layout.item_tag_list,null);
               ((TextView)linearLayout.findViewById(R.id.label_name)).setText(entity.getName());
                flowLayout=(FlowLayout)linearLayout.findViewById(R.id.flowlayout);
            }else{
                TextView tv = new TextView(this);
                tv.setText(entity.getName());
                tv.setTextColor(getResources().getColor(R.color.login));
                tv.setTextSize(12);
                tv.setTag(entity.getId());
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setBackgroundResource(R.drawable.round_textview);
                tv.setOnClickListener(this);
                flowLayout.addView(tv, lp);
            }
            tagLayout.addView(linearLayout);
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
                if(list.size()<1){
                    ToastUtils.showShortToast(this,"至少选择一个标签");
                    return;
                }
                ToastUtils.showShortToast(this,list.toArray().length);
//                pubArticle();
                break;
        }
    }
    private void pubArticle(){
        Map<String,String> map=new HashMap<>();
        map.put("uid",SPUtils.get(this,"userId","").toString());
        map.put("title",bundle.getString("title"));
        map.put("content",bundle.getString("content"));
        String temp="";
        for(int i=0;i<list.size();i++){
            if(i!=list.size()-1){
                temp+=list.get(i)+",";
            }else{
                temp+=list.get(i);
            }
        }
        map.put("label_id",temp);
        List<File> files=new ArrayList<>();
        List<String> strings=bundle.getStringArrayList("imgs");
        for(int i=0;i<strings.size();i++){
            files.add(new File(strings.get(i)));
        }
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), files);
        HttpManager.getInstance().pubQuestion(postEntity, new MyUploadListener(), map);
    }
    @Override
    public void onNext(Object o) {
        super.onNext(o);
        ToastUtils.showShortToast(this,"发布成功");
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
        if(list.contains(v.getTag())){
            v.setBackgroundResource(R.drawable.round_textview);
            list.remove(v.getTag());
        }else{
            v.setBackgroundResource(R.drawable.round_textview1);
            list.add(v.getTag().toString());
        }
    }
}
