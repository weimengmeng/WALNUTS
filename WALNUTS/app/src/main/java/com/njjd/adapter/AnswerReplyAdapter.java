package com.njjd.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.njjd.domain.AnswerEntity;
import com.njjd.domain.CommentEntity;
import com.njjd.domain.ReplyEntity;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/7/31.
 */

public class AnswerReplyAdapter extends BaseExpandableListAdapter {
    private List<AnswerEntity> groupArray;
    private AnswerEntity answerEntity;
    private CommentEntity commentEntity;
    private ReplyEntity replyEntity;
    private Context mContext;
    public AnswerReplyAdapter(Context context, List<AnswerEntity> groupArray){
        mContext = context;
        this.groupArray = groupArray;
    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupArray.get(groupPosition).getCommentEntityList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupArray.get(groupPosition).getCommentEntityList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        GroupHolder holder = null;
        if(view == null){
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_group, null);
            holder.head = (ImageView)view.findViewById(R.id.img_head);
            holder.groupName = (TextView)view.findViewById(R.id.txt_name);
            holder.groupMess = (TextView)view.findViewById(R.id.txt_message);
            holder.groupAgree = (TextView)view.findViewById(R.id.txt_agree);
            holder.groupTime = (TextView)view.findViewById(R.id.txt_time);
            holder.groupContent = (TextView)view.findViewById(R.id.txt_content);
            holder.groupSave = (TextView)view.findViewById(R.id.txt_save);
            holder.groupReport = (TextView)view.findViewById(R.id.txt_report);
            holder.groupOpen = (TextView)view.findViewById(R.id.txt_open);
            view.setTag(holder);
        }else{
            holder = (GroupHolder)view.getTag();
        }
        answerEntity=groupArray.get(groupPosition);
        holder.groupName.setText(answerEntity.getName());
        holder.groupMess.setText(answerEntity.getMessage());
        holder.groupAgree.setTag(""+answerEntity.getAnwerId());
        holder.groupSave.setTag(""+answerEntity.getAnwerId());
        holder.groupReport.setTag(""+answerEntity.getAnwerId());
        holder.groupTime.setText(answerEntity.getTime());
        holder.groupContent.setText(answerEntity.getContent());
        holder.groupAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast(mContext,"认同回答"+v.getTag().toString());
            }
        });
        holder.groupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast(mContext,"收藏回答"+v.getTag().toString());
            }
        });
        holder.groupReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast(mContext,"举报回答"+v.getTag().toString());
            }
        });
        if(isExpanded){
            holder.groupOpen.setText("收起评论 "+answerEntity.getOpen());
        }else{
            holder.groupOpen.setText("展开评论 "+answerEntity.getOpen());
        }
        return view;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ChildHolder holder = null;
        if(view == null){
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_group_child, null);
            holder.childHead = (ImageView)view.findViewById(R.id.img_answer_head);
            holder.childName = (TextView)view.findViewById(R.id.txt_answer_name);
            holder.childTime = (TextView)view.findViewById(R.id.txt_time);
            holder.childMess = (TextView)view.findViewById(R.id.txt_answer_message);
            holder.childContent = (TextView)view.findViewById(R.id.txt_answer_content);
            holder.childReplyNum = (TextView)view.findViewById(R.id.txt_replyNum);
            holder.childReport = (TextView)view.findViewById(R.id.txt_report);
            holder.childReply = (TextView)view.findViewById(R.id.txt_reply);
            view.setTag(holder);
        }else{
            holder = (ChildHolder)view.getTag();
        }
        if(childPosition!=0){
            view.findViewById(R.id.lv_comment).setVisibility(View.GONE);
        }else{
            view.findViewById(R.id.lv_comment).setVisibility(View.VISIBLE);
        }
        commentEntity=groupArray.get(groupPosition).getCommentEntityList().get(childPosition);
        holder.childName.setText(commentEntity.getName());
        holder.childMess.setText(commentEntity.getMessage());
        holder.childContent.setText(commentEntity.getContent());
        holder.childTime.setText(commentEntity.getTime());
        holder.childReplyNum.setText("回复 "+commentEntity.getReplyNum());
        view.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder{
        public ImageView head;
        public TextView groupName;
        public TextView groupMess;
        public TextView groupTime;
        public TextView groupAgree;
        public TextView groupContent;
        public TextView groupSave;
        public TextView groupReport;
        public TextView groupOpen;
    }

    class ChildHolder{
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
