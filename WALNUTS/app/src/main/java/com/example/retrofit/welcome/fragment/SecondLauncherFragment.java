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
 */
public class SecondLauncherFragment extends LauncherBaseFragment {
	private ImageView imgCase;

	private Bitmap goldBitmap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rooView = inflater.inflate(R.layout.fragment_second_launcher, null);
//		imgCase = (ImageView) rooView.findViewById(R.id.img_case);
//		goldBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.introbg5);
		return rooView;
	}

	public void startAnimation(){
//		TranslateAnimation translateAnimation=new TranslateAnimation(0,0,goldBitmap.getHeight()*2+80,0);
//		translateAnimation.setDuration(1000);
//		translateAnimation.setFillAfter(true);
//		imgCase.startAnimation(translateAnimation);
	}
	public void stopAnimation() {
//		imgCase.clearAnimation();
	}
}
