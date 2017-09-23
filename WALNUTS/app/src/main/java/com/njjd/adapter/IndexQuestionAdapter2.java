package com.njjd.adapter;

/**
 * Created by mrwim on 17/8/11.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.njjd.domain.BannerEntity;
import com.njjd.domain.QuestionEntity;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.DateUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;
import com.youth.banner.Banner;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class IndexQuestionAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    //item类型
    public static final int ITEM_TYPE_CONTENT = 1;
    //模拟数据
    private List<QuestionEntity> mList;
    private Context mContext;
    private ImageView head;
    private LayoutInflater inflater;
    public static int currentPage = 1;
    private OnItemClickListener mOnItemClickListener = null;
    public IndexQuestionAdapter2(Context context, List<QuestionEntity> list) {
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(context);

    }
    //内容长度
    public int getContentItemCount() {
        return mList.size();
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
            return ITEM_TYPE_CONTENT;
    }

    //内容 ViewHolder
    public class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;
        LinearLayout lvHead;
        TextView total;
        TextView createTime;
        TextView content;
        TextView focusNum;
        TextView answerNum;
        public ContentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_title);
            content = (TextView) itemView.findViewById(R.id.txt_content);
            imageView = (ImageView) itemView
                    .findViewById(R.id.img1);
            lvHead = (LinearLayout) itemView
                    .findViewById(R.id.lv_head);
            total = (TextView) itemView.findViewById(R.id.txt_total);
            createTime = (TextView) itemView
                    .findViewById(R.id.txt_time);
            focusNum = (TextView) itemView
                    .findViewById(R.id.txt_focusNum);
            answerNum = (TextView) itemView
                    .findViewById(R.id.txt_answerNum);
        }
    }

    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(mContext).inflate(R.layout.item_index, parent, false);
            ContentViewHolder vh = new ContentViewHolder(layout);
            layout.setOnClickListener(this);
            return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       if (holder instanceof ContentViewHolder) {
            holder.itemView.setTag(position);
            final QuestionEntity temp = mList.get(position);
            ((ContentViewHolder) holder).title.setText(temp.getTitle());
           ((ContentViewHolder) holder).focusNum.setText("关注 "+Float.valueOf(temp.getFocusNum()).intValue());
           ((ContentViewHolder) holder).answerNum.setText("回答 "+Float.valueOf(temp.getAnswerNum()).intValue());
           if(Float.valueOf(temp.getAnswerNum()).intValue() + Float.valueOf(temp.getFocusNum()).intValue()==0){
               ((ContentViewHolder) holder).total.setText("提出了该问题");
           }else{
               ((ContentViewHolder) holder).total.setText("等  " + (Float.valueOf(temp.getPart_num()).intValue()) + "  人参与");
           }
           ParsePosition pos = new ParsePosition(0);
            ((ContentViewHolder) holder).createTime.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(temp.getDateTime(), pos)));
            if ("".equals(temp.getPhoto())) {
                ((ContentViewHolder) holder).imageView.setVisibility(View.GONE);
                ((ContentViewHolder) holder).content.setVisibility(View.VISIBLE);
                ((ContentViewHolder) holder).content.setText("问题描述: "+temp.getContent());
            } else {
                ((ContentViewHolder) holder).content.setVisibility(View.GONE);
                ((ContentViewHolder) holder).imageView.setVisibility(View.VISIBLE);
                String[] strings = temp.getPhoto().split(",");
                GlideImageLoder.getInstance().displayImage(mContext, strings[0].replace("\"", ""), ((ContentViewHolder) holder).imageView);
            }
            String[] strs = temp.getPic().split(",");
            String[] uids=temp.getUids().split(",");
            if ("".equals(temp.getPic())) {
                ((ContentViewHolder) holder).lvHead.removeAllViews();
            } else {
                ((ContentViewHolder) holder).lvHead.removeAllViews();
                ((ContentViewHolder) holder).lvHead.setVisibility(View.VISIBLE);
                LinearLayout layout;
                for (int i = 0; i < strs.length && i < 1; i++) {
                    layout = (LinearLayout) inflater.inflate(R.layout.layout_head, null);
                    head = (ImageView) layout.findViewById(R.id.head);
                    GlideImageLoder.getInstance().displayImage(mContext, strs[i], head);
                    head.setTag(uids[i]);
                    ((ContentViewHolder) holder).lvHead.addView(layout);
                    head.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(mContext,PeopleInfoActivity.class);
                            intent.putExtra("uid",head.getTag().toString());
                            mContext.startActivity(intent);
                        }
                    });
                }
            }
        } else if (holder instanceof FootViewHolder) {

        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return getContentItemCount();
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(IndexQuestionAdapter2.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}