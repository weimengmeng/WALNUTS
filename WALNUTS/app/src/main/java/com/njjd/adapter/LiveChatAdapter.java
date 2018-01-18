package com.njjd.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.njjd.utils.DateUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mrwim on 18/1/15.
 */

public class LiveChatAdapter extends BaseAdapter {
    Context context;
    List<EMMessage> list;
    public LiveChatAdapter(Context context, List<EMMessage> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
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
            try {
                GlideImageLoder.getInstance().displayImage(context,vo.getStringAttribute("avatar"),img_avatar);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            img_avatar.setOnClickListener(new View.OnClickListener() {
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
            try {
                GlideImageLoder.getInstance().displayImage(context,vo.getStringAttribute("avatar"),img_avatar);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
        TextView text_msg =  convertView.findViewById(R.id.chat_content);
        TextView text_time =  convertView.findViewById(R.id.txt_time);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(new Date(vo.getMsgTime()));
        ParsePosition pos = new ParsePosition(0);
        text_time.setText(DateUtils.formationDate(format.parse(time, pos)));
        if (vo.getType() == EMMessage.Type.TXT) {
            text_msg.setText(((EMTextMessageBody) vo.getBody()).getMessage());
            text_msg.setVisibility(View.VISIBLE);
        }
        convertView.findViewById(R.id.msgView).setTag(list.get(position));
        return convertView;
    }
}
