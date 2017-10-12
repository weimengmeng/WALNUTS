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
        initAfterSetContentView(this,top);
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
            txtFocusNum.setText("关注的人\n" + Float.valueOf(object.getString("follow_numm")).intValue());
            txtFocusedNum.setText("被关注\n" + Float.valueOf(object.getString("be_follow_numm")).intValue());
            txtVocation.setText(object.getString("industry_name"));
            txtMessage.setText(object.isNull("introduction")||object.getString("introduction").equals("")?"未填写":object.getString("introduction"));
            txtPosition.setText(object.isNull("position")?"未填写":object.getString("position"));
            txtArea.setText(object.getString("province_name")+object.getString("city_name"));
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

    @OnClick({R.id.back, R.id.btn_add_help2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_add_help2:
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
