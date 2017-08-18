package com.njjd.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.SPUtils;
import com.njjd.walnuts.AttentionActivity;
import com.njjd.walnuts.MyAnswerActivity;
import com.njjd.walnuts.MyFocusActivity;
import com.njjd.walnuts.MyQuestionActivity;
import com.njjd.walnuts.MySaveActivity;
import com.njjd.walnuts.PersonalActivity;
import com.njjd.walnuts.R;
import com.njjd.walnuts.SettingActivity;
import com.yongchun.library.view.ImageSelectorActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/7/10.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.txt_change)
    TextView txtChange;
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
    @BindView(R.id.ll_bg)
    LinearLayout llbg;
    private Context context;
    private File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        getUserInfo();
        return view;
    }

    @Override
    public void lazyInitData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        txtName.setText(SPUtils.get(context, "name", "").toString());
        txtMessage.setText(SPUtils.get(context, "message", "").toString());
        txtPosition.setText(SPUtils.get(context, "position", "").toString());
        txtArea.setText(SPUtils.get(context, "province", "").toString() + SPUtils.get(context, "city", "").toString());
        txtVocation.setText(SPUtils.get(context, "industry", "").toString());
        txtFocus.setText("关注的人\n" + SPUtils.get(context, "focus", 0));
        txtFocused.setText("被关注\n" + SPUtils.get(context, "focused", 0));
        txtCompany.setText(SPUtils.get(context,"company","").toString());
        GlideImageLoder.getInstance().displayImage(context, SPUtils.get(context, "head", "").toString(), imgHead);
        if (SPUtils.get(context, "sex", "0").toString().equals("1.0")) {
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
                    SPUtils.put(context, "province", object.getString("province_name"));
                    SPUtils.put(context, "city", object.getString("city_name"));
                    SPUtils.put(context, "company", object.isNull("company") ? "未填写" : object.getString("company"));
                    SPUtils.put(context, "position", object.isNull("position") ? "未填写" : object.getString("position"));
                    SPUtils.put(context, "industry", object.getString("industry_name"));
                    SPUtils.put(context, "sales", object.getString("sales_name"));
                    SPUtils.put(context, "message", object.isNull("introduction") ? "未填写" : object.getString("introduction"));
                    SPUtils.put(context, "focus", Float.valueOf(object.getString("follow_numm")).intValue());
                    SPUtils.put(context, "focused", Float.valueOf(object.getString("be_follow_numm")).intValue());
                    txtName.setText(SPUtils.get(context, "name", "").toString());
                    txtMessage.setText(SPUtils.get(context, "message", "").toString());
                    txtPosition.setText(SPUtils.get(context, "position", "").toString());
                    txtCompany.setText(SPUtils.get(context,"company","").toString());
                    txtArea.setText(SPUtils.get(context, "province", "").toString() + SPUtils.get(context, "city", "").toString());
                    txtVocation.setText(SPUtils.get(context, "industry", "").toString());
                    txtFocus.setText("关注的人\n" + SPUtils.get(context, "focus", 0));
                    txtFocused.setText("被关注\n" + SPUtils.get(context, "focused", 0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, context, false, false), map);
        HttpManager.getInstance().getUserInfo(postEntity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), txtChange);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.txt_change)
    public void onViewClicked() {
    }

    @OnClick({R.id.img_head, R.id.txt_change, R.id.txt_focus, R.id.txt_focused, R.id.lv_question, R.id.lv_answer, R.id.lv_focus, R.id.lv_save, R.id.lv_set})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.img_head:
                //1多选 2 单选 单选才有裁剪功能
                ImageSelectorActivity.start(getActivity(), 1, 2, true, true, true);
                break;
            case R.id.txt_change:
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
                startActivity(intent);
                break;
            case R.id.lv_answer:
                intent = new Intent(context, MyAnswerActivity.class);
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
            SPUtils.put(context,"head",object.get("path").getAsString());
        }
    };

    class MyUploadListener implements ProgressListener {
        @Override
        public void onProgress(long progress, long total, boolean done) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            file = new File(images.get(0));
            GlideImageLoder.getInstance().displayImage(context, images.get(0), imgHead);
            uploadFile();
        }
    }
}
