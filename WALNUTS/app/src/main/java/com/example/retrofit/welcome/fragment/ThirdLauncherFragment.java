package com.example.retrofit.welcome.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.njjd.walnuts.R;

/**
 * @author Wim
 * @create time 2015-08-07
 */
public class ThirdLauncherFragment extends LauncherBaseFragment{
	private ImageView ivReward;

	private Bitmap goldBitmap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rooView=inflater.inflate(R.layout.fragment_third_launcher, null);
//		ivReward=(ImageView) rooView.findViewById(R.id.iv_reward);
//		goldBitmap=BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.introbg9);
		return rooView;
	}

	public void startAnimation(){
//		Animation scaleAnim=AnimationUtils.loadAnimation(getActivity(),R.anim.reward_launcher);
//		scaleAnim.setDuration(1000);
//		scaleAnim.setFillAfter(true);
//		ivReward.startAnimation(scaleAnim);
	}

	@Override
	public void stopAnimation(){
//		ivReward.clearAnimation();
	}
}
