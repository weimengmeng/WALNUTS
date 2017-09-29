package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.application.ConstantsVal;
import com.njjd.http.HttpManager;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class AnswerActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.et_answer)
    EditText etAnswer;

    @Override
    public int bindLayout() {
        return R.layout.activity_answer;
    }

    @Override
    public void initView(View view) {
        back.setText("详情");
        txtTitle.setText("回答");
        txtName.setText(getIntent().getStringExtra("quesTitle"));
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
                if(etAnswer.getText().toString().trim().equals("")){
                    ToastUtils.showShortToast(this,"请输入回答");
                    return;
                }
                MobclickAgent.onEvent(this, ConstantsVal.ANSWERQUESTION);
                pubAnswer();
                break;
        }
    }
    private void pubAnswer(){
        Map<String,Object> map=new HashMap<>();
        map.put("article_id",Float.valueOf(getIntent().getStringExtra("quesId")).intValue());
        map.put("uid", SPUtils.get(this,"userId",""));
        map.put("token", SPUtils.get(this,"token",""));
        map.put("content",etAnswer.getText().toString().trim());
        SubjectPost postEntity=new SubjectPost(new ProgressSubscriber(this,this,false,false),map);
        HttpManager.getInstance().pubComment(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        ToastUtils.showShortToast(this, "回答成功");
        MobclickAgent.onEvent(this,ConstantsVal.ANSWERRESULT);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
