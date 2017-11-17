package com.njjd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.njjd.domain.InformEntity;
import com.njjd.domain.MyConversation;
import com.njjd.utils.DateUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.LogUtils;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeTextView;

/**
 * Created by mrwim on 17/8/22.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder>  implements View.OnClickListener {
    Context context;
    List<MyConversation> list;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private OnItemClickListener mOnItemClickListener = null;
    public ConversationAdapter(Context context, List<MyConversation> list) {
        this.context = context;
        this.list = list;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mesg, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        EMConversation conversation = list.get(position).getConversation();
        viewHolder.itemView.setTag(position);
        if (conversation.getAllMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
            EMMessage lastMessage = conversation.getLastMessage();
            if (lastMessage.getType() == EMMessage.Type.TXT) {
                viewHolder.content
                        .setText(((EMTextMessageBody) lastMessage.getBody())
                                .getMessage());
            } else if (lastMessage.getType() == EMMessage.Type.IMAGE) {
                viewHolder.content
                        .setText("[图片]");
            } else if (lastMessage.getType() == EMMessage.Type.VOICE) {
                viewHolder.content
                        .setText("[语音]");
            }else{
                viewHolder.content
                        .setText("未知类型消息");
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
        viewHolder.name.setText(list.get(position).getName());
        GlideImageLoder.getInstance().displayImage(context, list.get(position).getAvatar(), viewHolder.head);
        viewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, PeopleInfoActivity.class);
                intent.putExtra("uid", list.get(position).getOpenId());
                context.startActivity(intent);
            }
        });
        ParsePosition pos = new ParsePosition(0);
        viewHolder.date.setText(DateUtils.formationDate(sdf.parse(sdf.format(new Date(conversation.getLastMessage().getMsgTime())), pos)));
        if (conversation.getUnreadMsgCount() > 0) {
            viewHolder.badge.showTextBadge(conversation.getUnreadMsgCount()+"");
        } else {
           viewHolder.badge.hiddenBadge();
        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView content;
        public ImageView head;
        public TextView date;
        public BGABadgeTextView badge;
        public TextView delete;
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txt_name);
            head = (ImageView) view.findViewById(R.id.img_head);
            content = (TextView) view.findViewById(R.id.txt_content);
            date =(TextView) view.findViewById(R.id.txt_date);
            badge =(BGABadgeTextView) view.findViewById(R.id.txt_badge);
            delete =(TextView) view.findViewById(R.id.item_delete);
        }
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }
    public void remove(int position){
        list.remove(position);
        notifyDataSetChanged();
    }
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
