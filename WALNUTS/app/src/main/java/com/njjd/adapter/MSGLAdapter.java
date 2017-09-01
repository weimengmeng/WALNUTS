package com.njjd.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import com.njjd.walnuts.R;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
	public View getView(int position, View convertView, ViewGroup parent) {
		EMMessage vo = list.get(position);
		if (vo.direct() == EMMessage.Direct.RECEIVE) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_chat_receive, null);
			ImageView img_avatar = (ImageView) convertView
					.findViewById(R.id.img_head);
				GlideImageLoder.getInstance().displayImage(context,avatar,img_avatar);
		} else {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_chat_send, null);
			ImageView img_avatar = (ImageView) convertView
					.findViewById(R.id.img_head);
				GlideImageLoder.getInstance().displayImage(context, SPUtils.get(context,"head",""),img_avatar);
		}
		TextView text_msg = (TextView) convertView.findViewById(R.id.chat_content);
		TextView text_time = (TextView) convertView.findViewById(R.id.txt_time);
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=format.format(new Date(vo.getMsgTime()));
		ParsePosition pos = new ParsePosition(0);
		text_time.setText(DateUtils.formationDate(format.parse(time, pos)));
		ImageView img_msg = (ImageView) convertView.findViewById(R.id.chat_img_pic);
		TextView voice_msg = (TextView) convertView
				.findViewById(R.id.chat_voice);
		text_msg.setVisibility(View.GONE);
		img_msg.setVisibility(View.GONE);
		voice_msg.setVisibility(View.GONE);
		if (vo.getType() == EMMessage.Type.TXT) {
			text_msg.setText(((EMTextMessageBody) vo.getBody()).getMessage());
			text_msg.setVisibility(View.VISIBLE);
		} else if (vo.getType() == EMMessage.Type.IMAGE) {
			if (vo.direct() == EMMessage.Direct.RECEIVE)
					GlideImageLoder.getInstance().displayImage(context,((EMImageMessageBody)vo.getBody()).getThumbnailUrl(),img_msg);
			else if (vo.direct() == EMMessage.Direct.SEND)
				GlideImageLoder.getInstance().displayImage(context,((EMImageMessageBody)vo.getBody()).thumbnailLocalPath(),img_msg);
			img_msg.setVisibility(View.VISIBLE);
		} else if (vo.getType() == EMMessage.Type.VOICE) {
			voice_msg.setVisibility(View.VISIBLE);
			voice_msg.setText(((EMVoiceMessageBody) vo.getBody()).getLength()
					+ "''");
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
				play(((EMVoiceMessageBody) message.getBody()).getRemoteUrl());
			else
				play(((EMVoiceMessageBody) message.getBody()).getLocalUrl());
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

	private void play(String url) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
			mediaPlayer.start();
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

}
