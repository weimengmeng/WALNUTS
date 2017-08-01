package com.example.retrofit.welcome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.njjd.walnuts.LoginActivity;
import com.njjd.walnuts.R;

/**
 * 最后一个
 *
 * @author apple
 */
public class GoLauncherFragment extends LauncherBaseFragment implements OnClickListener {
    private ImageView imgView_immediate_experience;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_go_launcher, null);
        imgView_immediate_experience = (ImageView) rooView.findViewById(R.id.imgView_immediate_experience);
        imgView_immediate_experience.setOnClickListener(this);
        return rooView;
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void startAnimation() {

    }

    @Override
    public void stopAnimation() {

    }
}
