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
import com.njjd.http.HttpManager;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/7/12.
 * 查看其他用户信息必须参数用户id
 */

public class PeopleInfoActivity extends BaseActivity {
    @BindView(R.id.txt_add_focus)
    TextView txtAddFocus;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.img_sex)
    ImageView imgSex;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_focusNum)
    TextView txtFocusNum;
    @BindView(R.id.txt_focusedNum)
    TextView txtFocusedNum;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.txt_area)
    TextView txtArea;
    @BindView(R.id.topview)
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
        initAfterSetContentView(this,top);
        tempUser = getIntent().getStringExtra("uid");
        getUserInfo();
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void initAfterSetContentView(Activity activity,
                                               View titleViewGroup) {
        if (activity == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            // 透明状态栏
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (titleViewGroup == null)
                return;
            // 设置头部控件ViewGroup的PaddingTop,防止界面与状态栏重叠
            int statusBarHeight = ImmersedStatusbarUtils.getStatusBarHeight(activity);
            titleViewGroup.setPadding(0, statusBarHeight, 0,0);
        }
    }
    private void getUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("ouid", tempUser);
        LogUtils.d(map.toString());
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
            if (object.getString("sex").equals("0.0")) {
                imgSex.setImageDrawable(getResources().getDrawable(R.drawable.icon_girl));
            } else {
                imgSex.setImageDrawable(getResources().getDrawable(R.drawable.icon_boy));
            }
            txtFocusNum.setText("关注的人\n" + Float.valueOf(object.getString("follow_numm")).intValue());
            txtFocusedNum.setText("被关注\n" + Float.valueOf(object.getString("be_follow_numm")).intValue());
            txtVocation.setText(object.getString("industry_name"));
            txtMessage.setText(object.getString("introduction"));
            txtPosition.setText(object.isNull("position")?"未填写":object.getString("position"));
            txtArea.setText(object.getString("province_name")+object.getString("city_name"));
            txtCompany.setText(object.isNull("company")?"未填写":object.getString("company"));
            if(!object.getString("uid").equals(SPUtils.get(PeopleInfoActivity.this,"userId","").toString())){
                txtAddFocus.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.txt_back, R.id.txt_add_focus, R.id.ll_ques, R.id.ll_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_back:
                finish();
                break;
            case R.id.txt_add_focus:
                if(txtAddFocus.getText().toString().equals("关注")){
                    followUser();
                }else {
                    Intent intent=new Intent(this, ChatActivity.class);
                    intent.putExtra("openId",tempUser);
                    intent.putExtra("name",txtName.getText().toString().trim());
                    intent.putExtra("avatar",tempHead);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.ll_ques:
                ToastUtils.showShortToast(this, "他的问题");
                break;
            case R.id.ll_answer:
                ToastUtils.showShortToast(this, "他的回答");
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
            }
        }, this, false, false), map);
        HttpManager.getInstance().followUser(postEntity);
    }
}
