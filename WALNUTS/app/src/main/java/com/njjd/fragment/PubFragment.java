package com.njjd.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.njjd.walnuts.R;

import butterknife.ButterKnife;

/**
 * Created by mrwim on 17/7/10.
 */

public class PubFragment extends Fragment {
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.activity_about, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
