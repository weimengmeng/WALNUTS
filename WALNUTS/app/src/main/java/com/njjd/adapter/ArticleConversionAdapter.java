package com.njjd.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.njjd.domain.CommentEntity;
import com.njjd.utils.DateUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by mrwim on 17/9/21.
 */

public class ArticleConversionAdapter extends BaseAdapter {
    private List<CommentEntity> list;
    private CommentEntity commentEntity;
    private Context context;
    private LayoutInflater inflater;

    public ArticleConversionAdapter(Context context, List<CommentEntity> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
    public View getView(final int childPosition, View view, ViewGroup parent) {
        ChildHolder holder = null;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_group_child2, null);
            holder.childHead = (ImageView) view.findViewById(R.id.img_answer_head);
            holder.childName = (TextView) view.findViewById(R.id.txt_answer_name);
            holder.childTime = (TextView) view.findViewById(R.id.txt_time);
            holder.childMess = (TextView) view.findViewById(R.id.txt_answer_message);
            holder.childContent = (TextView) view.findViewById(R.id.txt_answer_content);
            holder.childReplyNum = (TextView) view.findViewById(R.id.txt_replyNum);
            holder.childReport = (TextView) view.findViewById(R.id.txt_report);
            holder.childReply = (TextView) view.findViewById(R.id.txt_reply);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
        commentEntity = list.get(childPosition);
        if (commentEntity.getSec_uid().equals("sec_uid")) {
            GlideImageLoder.getInstance().displayImage(context, commentEntity.getHead(), holder.childHead);
            holder.childHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PeopleInfoActivity.class);
                    intent.putExtra("uid", list.get(childPosition).getCommentUId());
                    context.startActivity(intent);
                }
            });
            holder.childName.setText(commentEntity.getName());
            holder.childMess.setText(commentEntity.getMessage());
            holder.childContent.setText(commentEntity.getContent());
            ParsePosition pos = new ParsePosition(0);
            holder.childTime.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(commentEntity.getTime(), pos)));
            holder.childReplyNum.setText("回复 " + Float.valueOf(commentEntity.getReplyNum()).intValue());
            view.setBackgroundColor(context.getResources().getColor(R.color.grey));
        } else {
            GlideImageLoder.getInstance().displayImage(context, commentEntity.getHead(), holder.childHead);
            holder.childHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PeopleInfoActivity.class);
                    intent.putExtra("uid", commentEntity.getCommentUId());
                    context.startActivity(intent);
                }
            });
            holder.childName.setText(commentEntity.getName());
            holder.childMess.setText(commentEntity.getMessage());
            holder.childContent.setText(Html.fromHtml(commentEntity.getContent() + " //<font color='#ffb129'>@" + commentEntity.getSec_uname() + "</font>:" + commentEntity.getSec_content()));
            ParsePosition pos = new ParsePosition(0);
            holder.childTime.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(commentEntity.getTime(), pos)));
            holder.childReplyNum.setText("回复 " + Float.valueOf(commentEntity.getReplyNum()).intValue());
            view.setBackgroundColor(context.getResources().getColor(R.color.grey));
        }
        return view;
    }

    class ChildHolder {
        public ImageView childHead;
        public TextView childName;
        public TextView childTime;
        public TextView childMess;
        public TextView childContent;
        public TextView childReplyNum;
        public TextView childReport;
        public TextView childReply;
    }
}
