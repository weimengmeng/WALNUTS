package com.njjd.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.walnuts.AttentionActivity;
import com.njjd.walnuts.MyAnswerActivity;
import com.njjd.walnuts.MyFocusActivity;
import com.njjd.walnuts.MyQuestionActivity;
import com.njjd.walnuts.MySaveActivity;
import com.njjd.walnuts.PersonalActivity;
import com.njjd.walnuts.R;
import com.njjd.walnuts.SettingActivity;

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
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_focus)
    TextView txtFocus;
    @BindView(R.id.txt_focused)
    TextView txtFocused;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.txt_area)
    TextView txtArea;
    @BindView(R.id.txt_position)
    TextView txtPosition;
    @BindView(R.id.txt_company)
    TextView txtCompany;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void lazyInitData() {
        //获取问题
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

    @OnClick({R.id.txt_change,R.id.txt_focus,R.id.txt_focused, R.id.lv_question, R.id.lv_answer, R.id.lv_focus, R.id.lv_save, R.id.lv_set})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.txt_change:
                intent=new Intent(context, PersonalActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_focus:
                intent=new Intent(context, AttentionActivity.class);
                intent.putExtra("type",0);//0是我的关注的人
                startActivity(intent);
                break;
            case R.id.txt_focused:
                intent=new Intent(context, AttentionActivity.class);
                intent.putExtra("type",1);//关注我的人
                startActivity(intent);
                break;
            case R.id.lv_question:
                intent=new Intent(context, MyQuestionActivity.class);
                startActivity(intent);
                break;
            case R.id.lv_answer:
                intent=new Intent(context, MyAnswerActivity.class);
                startActivity(intent);
                break;
            case R.id.lv_focus:
                intent=new Intent(context, MyFocusActivity.class);
                startActivity(intent);
                break;
            case R.id.lv_save:
                intent=new Intent(context, MySaveActivity.class);
                startActivity(intent);
                break;
            case R.id.lv_set:
                intent=new Intent(context, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
