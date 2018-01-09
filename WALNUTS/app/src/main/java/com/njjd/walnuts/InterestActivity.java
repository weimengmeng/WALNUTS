package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njjd.application.ConstantsVal;
import com.njjd.domain.TagEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.MyActivityManager;
import com.njjd.utils.SPUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/8/6.
 */

public class InterestActivity extends BaseActivity{
    @BindView(R.id.btn_one)
    RadioButton btnOne;
    @BindView(R.id.btn_two)
    RadioButton btnTwo;
    @BindView(R.id.btn_three)
    RadioButton btnThree;
    @BindView(R.id.btn_four)
    RadioButton btnFour;
    @BindView(R.id.btn_five)
    RadioButton btnFive;
    @BindView(R.id.btn_six)
    RadioButton btnSix;
    @BindView(R.id.btn_seven)
    RadioButton btnSeven;
    @BindView(R.id.btn_eight)
    RadioButton btnEight;
    @BindView(R.id.btn_skip)
    Button btnSkip;
    private List<TagEntity> entities;
    private String strs="";
    private List<String> ids=new ArrayList<>();
    @Override
    public int bindLayout() {
        return R.layout.activity_interest;
    }

    @Override
    public void initView(View view) {
        entities=CommonUtils.getInstance().getTagsList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.btn_close,R.id.btn_selector,R.id.btn_one, R.id.btn_two, R.id.btn_three, R.id.btn_four, R.id.btn_five, R.id.btn_six, R.id.btn_seven, R.id.btn_eight, R.id.btn_skip})
    public void onViewClicked(View view) {
        if(view instanceof RadioButton){
            if(view.getTag().toString().equals("0")){
                ids.add(((RadioButton) view).getText().toString());
                ((RadioButton) view).setChecked(true);
                view.setTag("1");
            }else{
                ((RadioButton) view).setChecked(false);
                view.setTag("0");
                ids.remove(((RadioButton) view).getText().toString());
            }
        }
        switch (view.getId()) {
            case R.id.btn_close:
                doLogin();
                break;
            case R.id.btn_selector:
                btnOne.setChecked(true);
                btnEight.setChecked(true);
                btnTwo.setChecked(true);
                btnFive.setChecked(true);
                btnThree.setChecked(true);
                btnSeven.setChecked(true);
                btnFour.setChecked(true);
                btnSix.setChecked(true);
                btnOne.setTag("1");
                btnEight.setTag("1");
                btnTwo.setTag("1");
                btnFive.setTag("1");
                btnThree.setTag("1");
                btnSeven.setTag("1");
                btnFour.setTag("1");
                btnSix.setTag("1");
                break;
            case R.id.btn_one:
                break;
            case R.id.btn_two:
                break;
            case R.id.btn_three:
                break;
            case R.id.btn_four:
                break;
            case R.id.btn_five:
                break;
            case R.id.btn_six:
                break;
            case R.id.btn_seven:
                break;
            case R.id.btn_eight:
                break;
            case R.id.btn_skip:
                if(ids.size()==0){
                    doLogin();
                }else{
                    addFocus();
                }
                break;
        }
    }
    private void addFocus() {
        for(int i=0;i<entities.size();i++){
            if(ids.contains(entities.get(i).getName())){
                strs+=String.valueOf(Float.valueOf(entities.get(i).getId()).intValue())+",";
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("select", "1");
        map.put("label_id", strs);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                MobclickAgent.onEvent(InterestActivity.this, ConstantsVal.SELECTTAG);
               doLogin();
            }
        }, this, true, false), map);
        HttpManager.getInstance().focusLabel(postEntity);
    }
    private void doLogin() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", SPUtils.get(this, "phoneNumber", "").toString());
        map.put("pwd", SPUtils.get(this, "pwd", "").toString());
        map.put("device_token", SPUtils.get(this, "deviceToken", "").toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(longinListener, this, true, false), map);
        HttpManager.getInstance().userLogin(postEntity);
        MyActivityManager.getInstance().popOneActivity(LoginActivity.activity);
    }

    HttpOnNextListener longinListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls(); //重点
            Gson gson = gsonBuilder.create();
            try {
                SPUtils.put(InterestActivity.this,"firstUse","1");
                CommonUtils.initData(new JSONObject(gson.toJson(o)));
                Intent intent = new Intent(InterestActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
