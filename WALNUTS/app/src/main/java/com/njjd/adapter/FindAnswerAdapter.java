package com.njjd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.njjd.domain.ColumnEntity;
import com.njjd.domain.SelectedAnswerEntity;
import com.njjd.domain.SpecialEntity;
import com.njjd.utils.GlideImageLoder;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/9/14.
 */

public class FindAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context context;
    private List<SpecialEntity> list;
    private SpecialEntity entity;
    public static final int ONE_ITEM = 1;//精选回答
    public static final int TWO_ITEM = 2;//专栏类型
    private SelectedAnswerEntity selectedAnswerEntity;
    private ColumnEntity columnEntity;
    private OnItemClickListener mOnItemClickListener = null;
    public FindAnswerAdapter(Context context,List<SpecialEntity> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if(ONE_ITEM == viewType){
            View layout = LayoutInflater.from(context).inflate(R.layout.item_selected, parent, false);
            holder = new ContentViewHolder(layout);
            layout.setOnClickListener(this);
        }else{
            View layout = LayoutInflater.from(context).inflate(R.layout.item_column, parent, false);
            holder = new ColumnHolder(layout);
            layout.setOnClickListener(this);
        }
        return holder;
    }
    @Override
    public int getItemViewType(int position) {
        if(position<2){
            return TWO_ITEM;
        }else{
            return ONE_ITEM;
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        entity=list.get(position);
        if(holder instanceof ContentViewHolder){
            holder.itemView.setTag(position);
            selectedAnswerEntity=entity.getAnswerEntity();
            if(position==2){
                ((ContentViewHolder)holder).itemView.findViewById(R.id.txt_select).setVisibility(View.VISIBLE);
            }else{
                ((ContentViewHolder)holder).itemView.findViewById(R.id.txt_select).setVisibility(View.GONE);
            }
            ((ContentViewHolder)holder).name.setText(selectedAnswerEntity.getName());
            ((ContentViewHolder)holder).message.setText(selectedAnswerEntity.getMessage());
            ((ContentViewHolder)holder).title.setText(selectedAnswerEntity.getTitle());
            GlideImageLoder.getInstance().displayImage(context,selectedAnswerEntity.getHead(), ((ContentViewHolder)holder).head);
            if ("".equals(selectedAnswerEntity.getPhoto())) {
                ((ContentViewHolder) holder).pic.setVisibility(View.GONE);
            } else {
                ((ContentViewHolder) holder).pic.setVisibility(View.VISIBLE);
                String[] strings = selectedAnswerEntity.getPhoto().split(",");
                GlideImageLoder.getInstance().displayImage(context, strings[0].replace("\"", ""), ((ContentViewHolder) holder).pic);
            }
            ((ContentViewHolder)holder).reply.setText(selectedAnswerEntity.getReplyContent());
            if(selectedAnswerEntity.getIsFocus().equals("1")){
                ((ContentViewHolder)holder).foucs.setText("已关注");
                ((ContentViewHolder)holder).foucs.setTextColor(context.getResources().getColor(R.color.white));
                ((ContentViewHolder)holder).foucs.setBackground(context.getResources().getDrawable(R.drawable.background_button_div));
            }else{
                ((ContentViewHolder)holder).foucs.setText("+关注TA");
                ((ContentViewHolder)holder).foucs.setTextColor(context.getResources().getColor(R.color.txt_color));
                ((ContentViewHolder)holder).foucs.setBackground(context.getResources().getDrawable(R.drawable.background_button_div_grey));
            }
        }else {
            holder.itemView.setTag(position);
            if(position!=0){
                ((ColumnHolder)holder).itemView.findViewById(R.id.txt_topsale).setVisibility(View.GONE);
            }
            columnEntity=entity.getColumnEntity();
            ((ColumnHolder)holder).name.setText(columnEntity.getName());
            ((ColumnHolder)holder).title.setText(columnEntity.getTitle());
            ((ColumnHolder)holder).content.setText(columnEntity.getContent());
            GlideImageLoder.getInstance().displayImage(context,columnEntity.getHead(), ((ColumnHolder)holder).head);
            GlideImageLoder.getInstance().displayImage(context,columnEntity.getPic(), ((ColumnHolder)holder).pic);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ColumnHolder extends RecyclerView.ViewHolder{
        ImageView head;
        TextView title;
        TextView name;
        TextView content;
        ImageView pic;
        public ColumnHolder(View itemView) {
            super(itemView);
            head = (ImageView) itemView
                    .findViewById(R.id.img_head);
            name= (TextView) itemView
                    .findViewById(R.id.txt_name);
            content = (TextView) itemView.findViewById(R.id.txt_content);
            title = (TextView) itemView.findViewById(R.id.txt_title);
            pic = (ImageView) itemView
                    .findViewById(R.id.img);
        }
    }
    //内容 ViewHolder
    public class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView head;
        TextView title;
        TextView name;
        TextView message;
        ImageView pic;
        TextView reply;
        TextView foucs;
        public ContentViewHolder(View itemView) {
            super(itemView);
            head = (ImageView) itemView
                    .findViewById(R.id.img_head);
            name= (TextView) itemView
                    .findViewById(R.id.txt_name);
            message = (TextView) itemView.findViewById(R.id.txt_message);
            title = (TextView) itemView.findViewById(R.id.txt_title);
            pic = (ImageView) itemView
                    .findViewById(R.id.img1);
            reply = (TextView) itemView.findViewById(R.id.txt_reply);
            foucs = (TextView) itemView.findViewById(R.id.txt_focus);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
