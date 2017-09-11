package com.njjd.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.retrofit.mywidget.LoadingDialog;
import com.njjd.walnuts.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	private Context context;
	private LoadingDialog loadingDialog;
	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getActivity();
		loadingDialog = new LoadingDialog(context);
		mImageUrl = getArguments() != null ? getArguments().getString("url")
				: null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment,
				container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);
		mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});
		mAttacher.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
//				alert();
				return false;
			}
		});
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		GlideImageLoder.getInstance().displayImage(context,mImageUrl,
				mImageView);
	}
//	private void alert() {
//		new ActionSheetDialog(context)
//				.builder()
//				.addSheetItem("保存到本地", SheetItemColor.Red,
//						new OnSheetItemClickListener() {
//
//							@Override
//							public void onClick(int which) {
//								saveMyAlbum();
//							}
//						})
//				.addSheetItem("分享到朋友圈", SheetItemColor.Blue,
//						new OnSheetItemClickListener() {
//
//							@Override
//							public void onClick(int which) {
//								share();
//							}
//						}).show();
//	}
	public void readAsFile(InputStream inSream, File file) throws Exception {
		FileOutputStream outStream = outStream = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inSream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inSream.close();
	}

	// 创建Handler
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				loadingDialog.cancel();
			} else {
				loadingDialog.cancel();
			}
		};
	};
}
