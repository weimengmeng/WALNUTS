package com.example.retrofit.welcome.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.njjd.walnuts.R;

/**
 * @author Wim
 * @create time 2017-07-18
 */
public class FirstLauncherFragment extends LauncherBaseFragment{
	private ImageView ivReward;

	private Bitmap goldBitmap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rooView=inflater.inflate(R.layout.fragment_first_launcher, null);
//		ivReward=(ImageView) rooView.findViewById(R.id.iv_reward);
//		goldBitmap=BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.introbg3);
//		startAnimation();
		return rooView;
	}
	
	public void startAnimation(){
//		TranslateAnimation translateAnimation=new TranslateAnimation(0,0,-(goldBitmap.getHeight()*2+80),0);
//		translateAnimation.setDuration(1000);
//		translateAnimation.setFillAfter(true);
//		ivReward.startAnimation(translateAnimation);
	}
	
	@Override
	public void stopAnimation(){
//		ivReward.clearAnimation();
	}
}
