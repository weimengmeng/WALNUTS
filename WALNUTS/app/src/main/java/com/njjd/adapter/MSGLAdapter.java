package com.njjd.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.njjd.utils.DateUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.SPUtils;
import com.njjd.utils.SpaceImageDetailActivity;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wmm
 * @description
 * @date 2017-8-22
 */
public class MSGLAdapter extends BaseAdapter implements OnClickListener {
	Context context;
	List<EMMessage> list;
	MediaPlayer mediaPlayer;
	String avatar;
	ArrayList<String> datas;
	//语音动画控制器
	Timer mTimer=null;
	//语音动画控制任务
	TimerTask mTimerTask=null;
	//记录语音动画图片
	int index=1;
	AudioAnimationHandler audioAnimationHandler=null;
	public MSGLAdapter(Context context, List<EMMessage> list) {
		this.context = context;
		this.list = list;
		this.mediaPlayer = new MediaPlayer();
		this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final EMMessage vo = list.get(position);
		if (vo.direct() == EMMessage.Direct.RECEIVE) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_chat_receive, null);
			ImageView img_avatar = convertView
					.findViewById(R.id.img_head);
				GlideImageLoder.getInstance().displayImage(context,avatar,img_avatar);
			img_avatar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, PeopleInfoActivity.class);
					intent.putExtra("uid", list.get(position).getFrom());
					context.startActivity(intent);
				}
			});
		} else {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_chat_send, null);
			ImageView img_avatar = convertView
					.findViewById(R.id.img_head);
				GlideImageLoder.getInstance().displayImage(context, SPUtils.get(context,"head",""),img_avatar);
		}
		TextView text_msg =  convertView.findViewById(R.id.chat_content);
		TextView text_time =  convertView.findViewById(R.id.txt_time);
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=format.format(new Date(vo.getMsgTime()));
		ParsePosition pos = new ParsePosition(0);
		text_time.setText(DateUtils.formationDate(format.parse(time, pos)));
		final ImageView img_msg = convertView.findViewById(R.id.chat_img_pic);
		TextView voice_msg =  convertView
				.findViewById(R.id.chat_voice);
		text_msg.setVisibility(View.GONE);
		img_msg.setVisibility(View.GONE);
		voice_msg.setVisibility(View.GONE);
		if (vo.getType() == EMMessage.Type.TXT) {
			text_msg.setText(((EMTextMessageBody) vo.getBody()).getMessage());
			text_msg.setVisibility(View.VISIBLE);
		} else if (vo.getType() == EMMessage.Type.IMAGE) {
			if (vo.direct() == EMMessage.Direct.RECEIVE) {
				GlideImageLoder.getInstance().displayImage(context, ((EMImageMessageBody) vo.getBody()).getThumbnailUrl(), img_msg);
				img_msg.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, SpaceImageDetailActivity.class);
						datas=new ArrayList<>();
						datas.add(((EMImageMessageBody) vo.getBody()).getThumbnailUrl());
						intent.putExtra("images", datas);
						intent.putExtra("position", 0);
						int[] location = new int[2];
						img_msg.getLocationOnScreen(location);
						intent.putExtra("locationX", location[0]);
						intent.putExtra("locationY", location[1]);
						intent.putExtra("width", img_msg.getWidth());
						intent.putExtra("height", img_msg.getHeight());
						context.startActivity(intent);
						((Activity)context).overridePendingTransition(0, 0);
					}
				});
			}else if (vo.direct() == EMMessage.Direct.SEND) {
				GlideImageLoder.getInstance().displayImage(context, ((EMImageMessageBody) vo.getBody()).thumbnailLocalPath(), img_msg);
				img_msg.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, SpaceImageDetailActivity.class);
						datas=new ArrayList<>();
						datas.add(((EMImageMessageBody) vo.getBody()).thumbnailLocalPath());
						intent.putExtra("images", datas);
						intent.putExtra("position", 0);
						int[] location = new int[2];
						img_msg.getLocationOnScreen(location);
						intent.putExtra("locationX", location[0]);
						intent.putExtra("locationY", location[1]);
						intent.putExtra("width", img_msg.getWidth());
						intent.putExtra("height", img_msg.getHeight());
						context.startActivity(intent);
						((Activity)context).overridePendingTransition(0, 0);
					}
				});
			}
				img_msg.setVisibility(View.VISIBLE);
		} else if (vo.getType() == EMMessage.Type.VOICE) {
			voice_msg.setVisibility(View.VISIBLE);
			voice_msg.setText(((EMVoiceMessageBody) vo.getBody()).getLength()
					+ "''");
		}else{
			text_msg.setText("未知类型消息");
			text_msg.setVisibility(View.VISIBLE);
		}
		convertView.findViewById(R.id.msgView).setTag(list.get(position));
		convertView.findViewById(R.id.msgView).setOnClickListener(this);
		return convertView;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
		this.notifyDataSetChanged();
	}

	public EMMessage getMessage(int position) {
		if (list != null && position < list.size())
			return list.get(position);
		else
			return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		EMMessage message = (EMMessage) v.getTag();
		if (message.getType() == EMMessage.Type.VOICE) {
			if (message.direct() == EMMessage.Direct.RECEIVE)
				play(((EMVoiceMessageBody) message.getBody()).getRemoteUrl(),(TextView) v.findViewById(R.id.chat_voice),true);
			else
				play(((EMVoiceMessageBody) message.getBody()).getLocalUrl(),(TextView)v.findViewById(R.id.chat_voice),false);
		} else if (message.getType() == EMMessage.Type.IMAGE) {
//			File file = new File(
//					((EMImageMessageBody) message.getBody()).getLocalUrl());
//			if (file.exists()) {
//				Uri uri = Uri.fromFile(file);
//				ArrayList<String> strings=new ArrayList<>();
//				strings.add(CommonUtils.getRealPathFromUri(context,uri));
//				new PhotoPagerConfig.Builder((Activity)context)
//						.setBigImageUrls(strings) //图片url,可以是sd卡res，asset，网络图片.
//						.setSavaImage(false)                         //开启保存图片，默认false
//						.setPosition(1)                             //默认展示第2张图片
//						.build();
//			} else {
//				// The local full size pic does not exist yet.
//				// ShowBigImage needs to download it from the server
//				// first
//				// intent.putExtra("", message.get);
//				ArrayList<String> strings=new ArrayList<>();
//				strings.add(((EMImageMessageBody) message.getBody()).getRemoteUrl());
//				EMImageMessageBody body = (EMImageMessageBody) message.getBody();
//				new PhotoPagerConfig.Builder((Activity)context)
//						.setBigImageUrls(strings) //图片url,可以是sd卡res，asset，网络图片.
//						.setSavaImage(false)                         //开启保存图片，默认false
//						.setPosition(1)                             //默认展示第2张图片
//						.build();
//			}
		}
	}

	private void play(String url, final TextView textView, final boolean isLeft) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mediaPlayer.start();
					playAudioAnimation(textView,isLeft);
				}
			});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 播放语音图标动画
	 */
	private void playAudioAnimation(final TextView textView,boolean isleft) {
		//定时器检查播放状态
		stopTimer();
		mTimer=new Timer();
		//将要关闭的语音图片归位
		if(audioAnimationHandler!=null)
		{
			Message msg=new Message();
			msg.what=3;
			audioAnimationHandler.sendMessage(msg);
		}

		audioAnimationHandler=new AudioAnimationHandler(textView,isleft);
		mTimerTask = new TimerTask() {
			public boolean hasPlayed=false;
			@Override
			public void run() {
				if(mediaPlayer.isPlaying()) {
					hasPlayed=true;
					index=(index+1)%3;
					Message msg=new Message();
					msg.what=index;
					audioAnimationHandler.sendMessage(msg);
				}else
				{
					//当播放完时
					Message msg=new Message();
					msg.what=3;
					audioAnimationHandler.sendMessage(msg);
					//播放完毕时需要关闭Timer等
					if(hasPlayed)
					{
						stopTimer();
					}
				}
			}
		};
		//调用频率为500毫秒一次
		mTimer.schedule(mTimerTask, 0, 500);
	}
	class AudioAnimationHandler extends Handler
	{
		TextView textView;
		//判断是左对话框还是右对话框
		boolean isleft;
		public AudioAnimationHandler(TextView textView,boolean isleft)
		{
			this.textView=textView;
			//判断是左对话框还是右对话框 我这里是在前面设置ScaleType来表示的
			this.isleft=isleft;
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Drawable drawable=null;
			//根据msg.what来替换图片，达到动画效果
			switch (msg.what) {
				case 0 :
					drawable= isleft?context.getResources().getDrawable(R.drawable.pic_voice_right_01):context.getResources().getDrawable(R.drawable.pic_voice_left_03);
					break;
				case 1 :
					drawable= isleft?context.getResources().getDrawable(R.drawable.pic_voice_right_02):context.getResources().getDrawable(R.drawable.pic_voice_left_02);
					break;
				case 2 :
					drawable= isleft?context.getResources().getDrawable(R.drawable.pic_voice_right_03):context.getResources().getDrawable(R.drawable.pic_voice_left_01);
					break;
				default :
					drawable= isleft?context.getResources().getDrawable(R.drawable.pic_voice_right_03):context.getResources().getDrawable(R.drawable.pic_voice_left_01);
					break;
			}
			if(isleft){
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				textView.setCompoundDrawables(null,null,drawable,null);
			}else{
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				textView.setCompoundDrawables(drawable,null,null,null);
			}
		}

	}
	/**
	 * 停止
	 */
	private void stopTimer(){
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}

	}
}
