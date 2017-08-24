package com.njjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.njjd.utils.DateUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.walnuts.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mrwim on 17/8/22.
 */

public class ConversationAdapter extends BaseAdapter {
    Context context;
    List<EMConversation> list;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public ConversationAdapter(Context context, List<EMConversation> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_mesg, null);
        EMConversation conversation = list.get(position);
        if (conversation.getAllMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            EMMessage lastMessage = conversation.getLastMessage();
            if (lastMessage.getType() == EMMessage.Type.TXT) {
                ((TextView) convertView.findViewById(R.id.txt_content))
                        .setText(((EMTextMessageBody) lastMessage.getBody())
                                .getMessage());
            } else if (lastMessage.getType() == EMMessage.Type.IMAGE) {
                ((TextView) convertView.findViewById(R.id.txt_content))
                        .setText("[图片]");
            } else if (lastMessage.getType() == EMMessage.Type.VOICE) {
                ((TextView) convertView.findViewById(R.id.txt_content))
                        .setText("[语音]");
            }
//            if (lastMessage.direct == EMMessage.Direct.SEND
//                    && lastMessage.status == EMMessage.Status.FAIL) {
//                convertView.findViewById(R.id.msg_state).setVisibility(
//                        View.VISIBLE);
//            } else {
//                convertView.findViewById(R.id.msg_state).setVisibility(
//                        View.GONE);
//            }
        }
//        if (conversation.getUnreadMsgCount() > 0) {
//            // 显示与此用户的消息未读数
//            ((TextView)convertView.findViewById(R.id.unread_msg_number)).setText(String.valueOf(conversation
//                    .getUnreadMsgCount()));
//            convertView.findViewById(R.id.unread_msg_number).setVisibility(View.VISIBLE);
//        } else {
//            convertView.findViewById(R.id.unread_msg_number).setVisibility(View.INVISIBLE);
//        }
        try {
            ((TextView) convertView.findViewById(R.id.txt_name)).setText(conversation.getLastMessage().getTo());
            GlideImageLoder.getInstance().displayImage(context,conversation.getLastMessage().getStringAttribute("head"),(ImageView) convertView.findViewById(R.id.img_head));
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        ParsePosition pos = new ParsePosition(0);
        ((TextView) convertView.findViewById(R.id.txt_date)).setText(DateUtils.formationDate(sdf.parse(sdf.format(new Date(conversation.getLastMessage().getMsgTime())), pos)));
        return convertView;
    }

}
