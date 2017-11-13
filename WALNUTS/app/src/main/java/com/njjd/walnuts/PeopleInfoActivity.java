package com.njjd.walnuts;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyphenate.chat.EMImageMessageBody;
import com.njjd.http.HttpManager;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.SpaceImageDetailActivity;
import com.njjd.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/7/12.
 * 查看其他用户信息必须参数用户id
 */

public class PeopleInfoActivity extends BaseActivity {
    @BindView(R.id.btn_add_help2)
    TextView txtAddFocus;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_sex)
    ImageView imgSex;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_focus)
    TextView txtFocusNum;
    @BindView(R.id.txt_focused)
    TextView txtFocusedNum;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.txt_area)
    TextView txtArea;
    @BindView(R.id.txt_products)
    TextView txtProducts;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.txt_position)
    TextView txtPosition;
    @BindView(R.id.txt_company)
    TextView txtCompany;
    @BindView(R.id.txt_message)
    TextView txtMessage;
    private String tempUser = "";
    private String tempHead="";
    @Override
    public int bindLayout() {
        return R.layout.activity_people;
    }

    @Override
    public void initView(View view) {
        tempUser = getIntent().getStringExtra("uid");
        txtAddFocus.setText("关注");
        txtAddFocus.setVisibility(View.VISIBLE);
        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeopleInfoActivity.this, SpaceImageDetailActivity.class);
                ArrayList<String> datas=new ArrayList<>();
                datas.add(imgHead.getTag().toString());
                intent.putExtra("images", datas);
                intent.putExtra("position", 0);
                int[] location = new int[2];
                imgHead.getLocationOnScreen(location);
                intent.putExtra("locationX", location[0]);
                intent.putExtra("locationY", location[1]);
                intent.putExtra("width", imgHead.getWidth());
                intent.putExtra("height", imgHead.getHeight());
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        getUserInfo();
        txtTitle.setText("个人详情");
        txtAddFocus.setTextColor(getResources().getColor(R.color.login));
    }
    private void getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("ouid", tempUser);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, false, false), map);
        HttpManager.getInstance().getUserInfo(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        JSONObject object = null;
        try {
            object = new JSONObject(gson.toJson(o));
            txtName.setText(object.isNull("uname")?"未填写":object.getString("uname"));
            tempHead=object.isNull("headimg")?"":object.getString("headimg");
            GlideImageLoder.getInstance().displayImage(this, object.isNull("headimg")?"":object.getString("headimg"), imgHead);
            imgHead.setTag(tempHead);
            if (object.getString("sex").equals("0.0")) {
                imgSex.setImageDrawable(getResources().getDrawable(R.drawable.icon_girl));
            } else {
                imgSex.setImageDrawable(getResources().getDrawable(R.drawable.icon_boy));
            }
            txtProducts.setText(object.isNull("product")||object.getString("product").equals("")?"未填写":object.getString("product"));
            txtFocusNum.setText("关注的人\n" + Float.valueOf(object.getString("follow_numm")).intValue());
            txtFocusedNum.setText("被关注\n" + Float.valueOf(object.getString("be_follow_numm")).intValue());
            txtVocation.setText(object.getString("f_industry_name")+"-"+object.getString("industry_name"));
            txtMessage.setText(object.isNull("introduction")||object.getString("introduction").equals("")?"未填写":object.getString("introduction"));
            txtPosition.setText(object.isNull("position")?"未填写":object.getString("position"));
            txtArea.setText(object.getString("province_name")+"-"+object.getString("city_name"));
            txtCompany.setText(object.isNull("company")||object.getString("company").equals("")?"未填写":object.getString("company"));
            if(!object.getString("uid").equals(SPUtils.get(PeopleInfoActivity.this,"userId","").toString())){
                txtAddFocus.setVisibility(View.VISIBLE);
            }else{
                txtAddFocus.setVisibility(View.GONE);
            }
            if(object.getString("follow_stat").equals("1.0")){
                txtAddFocus.setText("私信");
            }else{
                txtAddFocus.setText("关注");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.btn_add_help2,R.id.lv_answer,R.id.lv_question})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_add_help2:
                if(txtAddFocus.getText().toString().equals("关注")){
                    followUser();
                }else {
                   intent=new Intent(this, ChatActivity.class);
                    intent.putExtra("openId",tempUser);
                    intent.putExtra("name",txtName.getText().toString().trim());
                    intent.putExtra("avatar",tempHead);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.lv_answer:
                intent = new Intent(this, MyAnswerActivity.class);
                intent.putExtra("uid",tempUser);
                startActivity(intent);
                break;
            case R.id.lv_question:
                intent = new Intent(this, MyQuestionActivity.class);
                intent.putExtra("uid",tempUser);
                startActivity(intent);
                break;
        }
    }
    private void followUser(){
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("be_uid", tempUser);
        map.put("select",1);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                txtAddFocus.setText("私信");
                ToastUtils.showShortToast(PeopleInfoActivity.this,"关注成功");
                SPUtils.put(PeopleInfoActivity.this,"focus",Integer.valueOf(SPUtils.get(PeopleInfoActivity.this,"focus",0).toString())+1);
            }
        }, this, false, false), map);
        HttpManager.getInstance().followUser(postEntity);
    }
}
