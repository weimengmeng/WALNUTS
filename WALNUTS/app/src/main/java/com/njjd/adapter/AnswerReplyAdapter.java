package com.njjd.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.domain.AnswerEntity;
import com.njjd.domain.CommentEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.DateUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;
import com.umeng.socialize.utils.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.http.POST;

/**
 * Created by mrwim on 17/7/31.
 */

public class AnswerReplyAdapter extends BaseExpandableListAdapter implements HttpOnNextListener {
    private List<AnswerEntity> groupArray;
    private AnswerEntity answerEntity;
    private CommentEntity commentEntity;
    private Context mContext;
    public static int CURRENT_PAGE = 1;
    int temp = 1, currentid = 0;
    private AnswerEntity tempEntity;
    private TextView tempView;
    private String article_id;
    private int currentPosition = 0;
    private String comment="";
    public AnswerReplyAdapter(Context context, List<AnswerEntity> groupArray, String article_id) {
        mContext = context;
        this.groupArray = groupArray;
        this.article_id = article_id;
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
            holder.groupSave.setTextColor(mContext.getResources().getColor(R.color.txt_color));

        } else {
            holder.groupSave.setText("收藏");
            holder.groupSave.setTextColor(mContext.getResources().getColor(R.color.login));
        }
        if (answerEntity.getIsPrise().equals("1")) {
            holder.groupAgree.setBackgroundResource(R.drawable.background_button_div_grey);
            holder.groupAgree.setSelected(false);
            holder.groupAgree.setTextColor(mContext.getResources().getColor(R.color.txt_color));
        } else {
            holder.groupAgree.setBackgroundResource(R.drawable.background_button_div);
            holder.groupAgree.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.groupAgree.setSelected(true);
        }
        holder.groupAgree.setText("" + Float.valueOf(answerEntity.getAgree()).intValue());
        GlideImageLoder.getInstance().displayImage(mContext,answerEntity.getHead(),holder.head);
        holder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,PeopleInfoActivity.class);
                intent.putExtra("uid",groupArray.get(groupPosition).getAnswerUId());
                mContext.startActivity(intent);
            }
        });
        holder.groupAgree.setTag("" + Float.valueOf(answerEntity.getAnwerId()).intValue());
        holder.groupSave.setTag("" + Float.valueOf(answerEntity.getAnwerId()).intValue());
        holder.groupReport.setTag("" + Float.valueOf(answerEntity.getAnwerId()).intValue());
        ParsePosition pos = new ParsePosition(0);
        holder.groupTime.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(answerEntity.getTime(), pos)));
        holder.groupContent.setText(answerEntity.getContent());
        holder.groupAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempView = (TextView) v;
                currentid = groupPosition;
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
                tempView = (TextView) v;
                currentid = groupPosition;
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
            if(Float.valueOf(answerEntity.getOpen()).intValue()==0){
                holder.groupOpen.setText("评论");
            }else{
                holder.groupOpen.setText("收起评论 " + Float.valueOf(answerEntity.getOpen()).intValue());
            }
        }else{
            if(Float.valueOf(answerEntity.getOpen()).intValue()==0){
                holder.groupOpen.setText("评论");
            }else{
                holder.groupOpen.setText("展开评论 " + Float.valueOf(answerEntity.getOpen()).intValue());
            }
        }
        holder.groupOpen.setTag(groupPosition);
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ChildHolder holder = null;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_group_child, null);
            holder.editText=(EditText) view.findViewById(R.id.et_comment);
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
            holder.editText.setVisibility(View.GONE);
            view.findViewById(R.id.ll_reply).setVisibility(View.VISIBLE);
        } else {
            holder.editText.setVisibility(View.VISIBLE);
            holder.editText.setTag(groupPosition);
            view.findViewById(R.id.ll_reply).setVisibility(View.GONE);
        }
        if(childPosition!=0) {
            commentEntity = groupArray.get(groupPosition).getCommentEntityList().get(childPosition);
            if(commentEntity.getSec_uid().equals("sec_uid")) {
                GlideImageLoder.getInstance().displayImage(mContext, commentEntity.getHead(), holder.childHead);
                holder.childHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PeopleInfoActivity.class);
                        intent.putExtra("uid", groupArray.get(groupPosition).getCommentEntityList().get(childPosition).getCommentUId());
                        mContext.startActivity(intent);
                    }
                });
                holder.childName.setText(commentEntity.getName());
                holder.childMess.setText(commentEntity.getMessage());
                holder.childContent.setText(commentEntity.getContent());
                ParsePosition pos = new ParsePosition(0);
                holder.childTime.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(commentEntity.getTime(), pos)));
                holder.childReplyNum.setText("回复 " + Float.valueOf(commentEntity.getReplyNum()).intValue());
                view.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
            }else{
                GlideImageLoder.getInstance().displayImage(mContext, commentEntity.getHead(), holder.childHead);
                holder.childHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PeopleInfoActivity.class);
                        intent.putExtra("uid", groupArray.get(groupPosition).getCommentEntityList().get(childPosition).getCommentUId());
                        mContext.startActivity(intent);
                    }
                });
                holder.childName.setText(commentEntity.getName());
                holder.childMess.setText(commentEntity.getMessage());
                holder.childContent.setText(Html.fromHtml(commentEntity.getContent()+" //<font color='#ffb129'>@"+commentEntity.getSec_uname()+"</font>:"+commentEntity.getSec_content()));
                ParsePosition pos = new ParsePosition(0);
                holder.childTime.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(commentEntity.getTime(), pos)));
                holder.childReplyNum.setText("回复 " + Float.valueOf(commentEntity.getReplyNum()).intValue());
                view.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
            }
        }
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
        public EditText editText;
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
        map.put("token",SPUtils.get(mContext,"token","").toString());
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
                tempView.setText((Integer.valueOf(tempView.getText().toString()) + 1) + "");
                tempView.setTextColor(mContext.getResources().getColor(R.color.txt_color));
                tempView.setSelected(false);
                groupArray.get(currentid).setIsPrise("1");
                break;
            case 1:
                ToastUtils.showShortToast(mContext, "认同－1");
                tempView.setBackgroundResource(R.drawable.background_button_div);
                tempView.setTextColor(mContext.getResources().getColor(R.color.white));
                tempView.setSelected(true);
                tempView.setText((Integer.valueOf(tempView.getText().toString())-1)+"");
                groupArray.get(currentid).setIsPrise("0");
                break;
            case 2:
                ToastUtils.showShortToast(mContext, "收藏成功");
                tempView.setText("取消收藏");
                tempView.setTextColor(mContext.getResources().getColor(R.color.txt_color));
                groupArray.get(currentid).setIsSave("1");
                break;
            case 3:
                ToastUtils.showShortToast(mContext, "取消收藏");
                tempView.setText("收藏");
                tempView.setTextColor(mContext.getResources().getColor(R.color.login));
                groupArray.get(currentid).setIsSave("0");
                break;
        }
    }
}
