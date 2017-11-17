package com.njjd.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.listener.ProgressListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.PhotoUtil;
import com.njjd.utils.SPUtils;
import com.njjd.walnuts.AttentionActivity;
import com.njjd.walnuts.MyAnswerActivity;
import com.njjd.walnuts.MyFocusActivity;
import com.njjd.walnuts.MyQuestionActivity;
import com.njjd.walnuts.MySaveActivity;
import com.njjd.walnuts.PersonalActivity;
import com.njjd.walnuts.R;
import com.njjd.walnuts.SettingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;

/**
 * Created by mrwim on 17/7/10.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.btn_add_help2)
    TextView txtChange;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.img_sex)
    ImageView imgSex;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_focus)
    TextView txtFocus;
    @BindView(R.id.txt_focused)
    TextView txtFocused;
    @BindView(R.id.txt_products)
    TextView txtProducts;
    @BindView(R.id.txt_message)
    TextView txtMessage;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.txt_area)
    TextView txtArea;
    @BindView(R.id.txt_position)
    TextView txtPosition;
    @BindView(R.id.txt_company)
    TextView txtCompany;
    @BindView(R.id.top)
    LinearLayout top;
    private Context context;
    private File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        ImmersedStatusbarUtils.initAfterSetContentView(getActivity(),top);
        getUserInfo();
        return view;
    }
    @Override
    public void lazyInitData() {
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
        } else {
            txtFocus.setText("关注的人\n" + SPUtils.get(context, "focus", 0));
            txtFocused.setText("被关注\n" + SPUtils.get(context, "focused", 0));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        txtName.setText(SPUtils.get(context, "name", "").toString());
        txtMessage.setText(SPUtils.get(context, "message", "").toString());
        txtPosition.setText(SPUtils.get(context, "position", "").toString());
        txtProducts.setText(SPUtils.get(context, "product", "").toString());
        txtArea.setText(SPUtils.get(context, "province", "").toString() + SPUtils.get(context, "city", "").toString());
        txtVocation.setText(SPUtils.get(context, "industry", "").toString());
        txtCompany.setText(SPUtils.get(context,"company","").toString());
        txtFocus.setText("关注的人\n" + SPUtils.get(context, "focus", 0));
        txtFocused.setText("被关注\n" + SPUtils.get(context, "focused", 0));
        GlideImageLoder.getInstance().displayImage(context, SPUtils.get(context, "head", "").toString(), imgHead);
        if (SPUtils.get(context, "sex", "0").toString().equals("0.0")) {
            imgSex.setImageDrawable(getResources().getDrawable(R.drawable.icon_girl));
        } else {
            imgSex.setImageDrawable(getResources().getDrawable(R.drawable.icon_boy));
        }
    }

    private void getUserInfo() {
        final Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(context, "userId", "").toString());
        map.put("token", SPUtils.get(context, "token", "").toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.serializeNulls(); //重点
                Gson gson = gsonBuilder.create();
                JSONObject object = null;
                try {
                    object = new JSONObject(gson.toJson(o));
                    SPUtils.put(context, "product", object.isNull("product") ? "" : object.getString("product"));
                    SPUtils.put(context, "province", object.getString("province_name"));
                    SPUtils.put(context, "city", object.getString("city_name"));
                    SPUtils.put(context, "company", object.isNull("company") ? "" : object.getString("company"));
                    SPUtils.put(context, "position", object.isNull("position") ? "" : object.getString("position"));
                    SPUtils.put(context, "industry", object.getString("f_industry_name")+"-"+object.getString("industry_name"));
                    SPUtils.put(context, "sales", object.getString("sales_name"));
                    SPUtils.put(context, "message", object.isNull("introduction") ? "" : object.getString("introduction"));
                    SPUtils.put(context, "focus", Float.valueOf(object.getString("follow_numm")).intValue());
                    SPUtils.put(context, "focused", Float.valueOf(object.getString("be_follow_numm")).intValue());
                    txtName.setText(SPUtils.get(context, "name", "").toString());
                    txtProducts.setText(SPUtils.get(context, "product", "").toString());
                    txtMessage.setText(SPUtils.get(context, "message", "").toString());
                    txtPosition.setText(SPUtils.get(context, "position", "").toString());
                    txtCompany.setText(SPUtils.get(context,"company","").toString());
                    txtArea.setText(SPUtils.get(context, "province", "").toString() +" "+ SPUtils.get(context, "city", "").toString());
                    txtVocation.setText(SPUtils.get(context, "industry", "").toString());
                    txtFocus.setText("关注的人\n" + SPUtils.get(context, "focus", 0));
                    txtFocused.setText("被关注\n" + SPUtils.get(context, "focused", 0));
                    if(txtMessage.getText().toString().equals("")){
                        txtMessage.setHint("待完善");
                    }
                    if(txtPosition.getText().toString().equals("")){
                        txtPosition.setHint("待完善");
                    }
                    if(txtCompany.getText().toString().equals("")){
                        txtCompany.setHint("待完善");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, context, false, false), map);
        HttpManager.getInstance().getUserInfo(postEntity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        txtChange.setText("编辑");
        txtChange.setVisibility(View.VISIBLE);
        txtChange.setTextColor(getResources().getColor(R.color.login));
        txtTitle.setText("我的");
        view.findViewById(R.id.back).setVisibility(View.GONE);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @OnClick({R.id.img_head, R.id.btn_add_help2, R.id.txt_focus, R.id.txt_focused, R.id.lv_question, R.id.lv_answer, R.id.lv_focus, R.id.lv_save, R.id.lv_set})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.img_head:
                Picker.from(this)
                        .count(1)
                        .enableCamera(true)
                        .setEngine(new GlideEngine())
                        .forResult(0);
                break;
            case R.id.btn_add_help2:
                intent = new Intent(context, PersonalActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_focus:
                intent = new Intent(context, MyFocusActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_focused:
                intent = new Intent(context, AttentionActivity.class);
                startActivity(intent);
                break;
            case R.id.lv_question:
                intent = new Intent(context, MyQuestionActivity.class);
                intent.putExtra("uid",SPUtils.get(context,"userId","").toString());
                startActivity(intent);
                break;
            case R.id.lv_answer:
                intent = new Intent(context, MyAnswerActivity.class);
                intent.putExtra("uid",SPUtils.get(context,"userId","").toString());
                startActivity(intent);
                break;
            case R.id.lv_focus:
                intent = new Intent(context, MyFocusActivity.class);
                startActivity(intent);
                break;
            case R.id.lv_save:
                intent = new Intent(context, MySaveActivity.class);
                startActivity(intent);
                break;
            case R.id.lv_set:
                intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void uploadFile() {
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(upFileListener, context, true, false), file);
        HttpManager.getInstance().uploadFile(postEntity, new MyUploadListener(),
                SPUtils.get(context, "userId", "").toString(), SPUtils.get(context, "token", "").toString());
    }

    HttpOnNextListener upFileListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            JsonObject object= JSONUtils.getAsJsonObject(o);
            SPUtils.put(context,"head",HttpManager.BASE_URL2+object.get("path").getAsString());
            onResume();
        }
    };

    class MyUploadListener implements ProgressListener {
        @Override
        public void onProgress(long progress, long total, boolean done) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == 0) {
            List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
            String imgpath= PhotoUtil.saveMyBitmapWH(CommonUtils.getRealPathFromUri(context,mSelected.get(0)), 480,800);
            file = new File(imgpath);
            uploadFile();
        }
    }
}
