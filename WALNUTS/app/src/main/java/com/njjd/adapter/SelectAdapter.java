package com.njjd.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.njjd.domain.ColumnArticleEntity;
import com.njjd.domain.ColumnEntity;
import com.njjd.domain.LiveRoom;
import com.njjd.http.HttpManager;
import com.njjd.utils.CustomImageView;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.GlideImageLoder2;
import com.njjd.walnuts.ColumnActivity;
import com.njjd.walnuts.LiveActivity;
import com.njjd.walnuts.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrwim on 17/9/14.
 */

public class SelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<ColumnArticleEntity> list;
    public static final int ONE_ITEM = 1;//精选回答
    public static final int TWO_ITEM = 2;//专栏类型
    private ColumnArticleEntity columnArticleEntity;
    private OnItemClickListener mOnItemClickListener = null;
    private int currentPage = 1;
    private int mHeaderCount = 1;//头部View个数
    private List<ColumnEntity> columnEntities;
    private LayoutInflater inflater;
    private List<String> images = new ArrayList<>();
    private List<LiveRoom> liveRooms;
    public SelectAdapter(Context context, List<ColumnArticleEntity> list, List<ColumnEntity> columnEntities,List<LiveRoom> liveRooms) {
        this.context = context;
        this.list = list;
        this.columnEntities = columnEntities;
        this.liveRooms=liveRooms;
        inflater = LayoutInflater.from(context);
    }

    public void setColumnEntities(List<ColumnEntity> columnEntities) {
        this.columnEntities = columnEntities;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (ONE_ITEM == viewType) {
            View layout = inflater.inflate(R.layout.layout_column2, parent, false);
            holder = new ContentViewHolder(layout);
            layout.setOnClickListener(this);
        } else {
            View layout = inflater.inflate(R.layout.item_column, parent, false);
            LinearLayout linearLayout=layout.findViewById(R.id.lv_hsc);
            View view;
            CustomImageView imageView;
            for(int i=0;i<columnEntities.size();i++){
               view = inflater.inflate(R.layout.layout_column,
                        parent, false);
               imageView=view.findViewById(R.id.img);
                GlideImageLoder2.getInstance().displayImage(context,columnEntities.get(i).getCrousel_img().split(",")[0].replace("\"", ""), imageView);
                final int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ColumnActivity.class);
                        intent.putExtra("column_id", Float.valueOf(columnEntities.get(finalI).getId()).intValue() + "");
                        context.startActivity(intent);
                    }
                });
                linearLayout.addView(view);
            }
            if(liveRooms.size()>0){
                layout.findViewById(R.id.txt_live).setVisibility(View.VISIBLE);
                Banner banner = layout.findViewById(R.id.banner);
                banner.setVisibility(View.VISIBLE);
                images.clear();
                for (int i = 0; i < liveRooms.size(); i++) {
                    images.add(liveRooms.get(i).getCoverImg());
                }
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                banner.setIndicatorGravity(BannerConfig.RIGHT);
                banner.isAutoPlay(true);
                banner.setDelayTime(3000);
                banner.setImages(images).setImageLoader(GlideImageLoder2.getInstance()).start();
                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Intent intent=new Intent(context,LiveActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("liveRoom",liveRooms.get(position));
                        intent.putExtra("liveRoom",bundle);
                        context.startActivity(intent);
                    }
                });
            }
            return new HeaderViewHolder(layout);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderCount != 0 && position < mHeaderCount) {
            return TWO_ITEM;
        } else {
            return ONE_ITEM;
        }
    }

    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContentViewHolder) {
            holder.itemView.setTag(position - 1);
            columnArticleEntity = list.get(position - 1);
            if ((position - 1 == 0)) {
                holder.itemView.findViewById(R.id.txt_topsale).setVisibility(View.VISIBLE);
            } else {
                holder.itemView.findViewById(R.id.txt_topsale).setVisibility(View.GONE);

            }
            if (columnArticleEntity.getIs_select().equals("1.0")) {
                holder.itemView.findViewById(R.id.label_select).setVisibility(View.VISIBLE);
            } else {
                holder.itemView.findViewById(R.id.label_select).setVisibility(View.GONE);
            }
            GlideImageLoder.getInstance().displayImage(context, columnArticleEntity.getHead(), ((ContentViewHolder) holder).head);
            ((ContentViewHolder) holder).name.setText(columnArticleEntity.getName() + " • " + columnArticleEntity.getColumnName());
            ((ContentViewHolder) holder).title.setText(columnArticleEntity.getTitle());
            ((ContentViewHolder) holder).content.setText(columnArticleEntity.getDesci());
            GlideImageLoder.getInstance().displayImage(context, HttpManager.BASE_URL2 + columnArticleEntity.getPic().split(",")[0].replace("\"", ""), ((ContentViewHolder) holder).pic);
        } else if (holder instanceof HeaderViewHolder) {
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + mHeaderCount;
    }

    //内容 ViewHolder
    public class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView head;
        TextView title;
        TextView content;
        TextView name;
        ImageView pic;

        public ContentViewHolder(View itemView) {
            super(itemView);
            head = itemView
                    .findViewById(R.id.img_head);
            name = itemView
                    .findViewById(R.id.txt_name);
            content = itemView
                    .findViewById(R.id.txt_content);
            title = itemView.findViewById(R.id.txt_title);
            pic = itemView
                    .findViewById(R.id.img);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
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
