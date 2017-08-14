package com.njjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.domain.AnswerEntity;
import com.njjd.domain.CommentEntity;
import com.njjd.domain.ReplyEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.DateUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrwim on 17/7/31.
 */

public class AnswerReplyAdapter extends BaseExpandableListAdapter implements HttpOnNextListener {
    private List<AnswerEntity> groupArray;
    private AnswerEntity answerEntity;
    private CommentEntity commentEntity;
    private ReplyEntity replyEntity;
    private Context mContext;
    public static int CURRENT_PAGE = 1;
    int temp = 1,currentid=0;
    private AnswerEntity tempEntity;
    private TextView tempView;

    public AnswerReplyAdapter(Context context, List<AnswerEntity> groupArray) {
        mContext = context;
        this.groupArray = groupArray;
    }

    public static int getCurrentPage() {
        return CURRENT_PAGE;
    }

    public static void setCurrentPage(int currentPage) {
        CURRENT_PAGE = currentPage;
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        GroupHolder holder = null;
        if (view == null) {
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_group, null);
            holder.head = (ImageView) view.findViewById(R.id.img_head);
            holder.groupName = (TextView) view.findViewById(R.id.txt_name);
            holder.groupMess = (TextView) view.findViewById(R.id.txt_message);
            holder.groupAgree = (TextView) view.findViewById(R.id.txt_agree);
            holder.groupTime = (TextView) view.findViewById(R.id.txt_time);
            holder.groupContent = (TextView) view.findViewById(R.id.txt_content);
            holder.groupSave = (TextView) view.findViewById(R.id.txt_save);
            holder.groupReport = (TextView) view.findViewById(R.id.txt_report);
            holder.groupOpen = (TextView) view.findViewById(R.id.txt_open);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }
        answerEntity = groupArray.get(groupPosition);
        holder.groupName.setText(answerEntity.getName());
        holder.groupMess.setText(answerEntity.getMessage());
        if (answerEntity.getIsSave().equals("1")) {
            holder.groupSave.setText("取消收藏");
        } else {
            holder.groupSave.setText("收藏");
        }
        if (answerEntity.getIsPrise().equals("1")) {
            holder.groupAgree.setBackgroundResource(R.drawable.background_button_div_grey);
        } else {
            holder.groupAgree.setBackgroundResource(R.drawable.background_button_div);
        }
        holder.groupAgree.setText("" + Float.valueOf(answerEntity.getAgree()).intValue());
        holder.groupAgree.setTag("" + Float.valueOf(answerEntity.getAnwerId()).intValue());
        holder.groupSave.setTag("" + Float.valueOf(answerEntity.getAnwerId()).intValue());
        holder.groupReport.setTag("" + Float.valueOf(answerEntity.getAnwerId()).intValue());
        ParsePosition pos = new ParsePosition(0);
        holder.groupTime.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(answerEntity.getTime(), pos)));
        holder.groupContent.setText(answerEntity.getContent());
        holder.groupAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempView=(TextView) v;
                currentid=groupPosition;
                if (groupArray.get(groupPosition).getIsPrise().equals("0")) {
                    //认同
                    temp = 0;
                    dealClickListener("point_comment_id", v.getTag().toString());
                } else {
                    //取消认同
                    temp = 1;
                    dealClickListener("point_comment_id_not", v.getTag().toString());
                }
            }
        });
        holder.groupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempView=(TextView) v;
                if (groupArray.get(groupPosition).getIsSave().equals("0")) {
                    temp = 2;
                    //收藏
                    dealClickListener("collect_comment_id", v.getTag().toString());
                } else {
                    temp = 3;
                    //取消收藏
                    dealClickListener("collect_comment_id_not", v.getTag().toString());
                }
            }
        });
        holder.groupReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast(mContext, "举报回答" + v.getTag().toString());
            }
        });
        if (isExpanded) {
            holder.groupOpen.setText("收起评论 " + Float.valueOf(answerEntity.getOpen()).intValue());
        } else {
            holder.groupOpen.setText("展开评论 " + Float.valueOf(answerEntity.getOpen()).intValue());
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ChildHolder holder = null;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_group_child, null);
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
        if (childPosition != 0) {
            view.findViewById(R.id.lv_comment).setVisibility(View.GONE);
            view.findViewById(R.id.ll_reply).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.lv_comment).setVisibility(View.VISIBLE);
            view.findViewById(R.id.ll_reply).setVisibility(View.GONE);
        }
        commentEntity = groupArray.get(groupPosition).getCommentEntityList().get(childPosition);
        holder.childName.setText(commentEntity.getName());
        holder.childMess.setText(commentEntity.getMessage());
        holder.childContent.setText(commentEntity.getContent());
        holder.childTime.setText(commentEntity.getTime());
        holder.childReplyNum.setText("回复 " + commentEntity.getReplyNum());
        view.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
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

    private void dealClickListener(String params, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(mContext, "userId", ""));
        map.put(params, id);
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, mContext, true, false), map);
        HttpManager.getInstance().agreeOrPraise(postEntity);
    }

    @Override
    public void onNext(Object o) {
        switch (temp) {
            case 0:
                ToastUtils.showShortToast(mContext, "认同＋1");
                tempView.setBackgroundResource(R.drawable.background_button_div_grey);
                tempView.setText((Integer.valueOf(tempView.getText().toString())+1)+"");
                groupArray.get(currentid).setIsPrise("1");
                break;
            case 1:
                ToastUtils.showShortToast(mContext, "认同－1");
                tempView.setBackgroundResource(R.drawable.background_button_div);
                tempView.setText((Integer.valueOf(tempView.getText().toString())-1)+"");
                groupArray.get(currentid).setIsPrise("0");
                break;
            case 2:
                ToastUtils.showShortToast(mContext, "收藏成功");
                tempView.setText("取消收藏");
                groupArray.get(currentid).setIsSave("1");
                break;
            case 3:
                ToastUtils.showShortToast(mContext, "取消收藏");
                tempView.setText("收藏");
                groupArray.get(currentid).setIsSave("0");
                break;
        }
    }
}
