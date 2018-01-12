package com.njjd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.njjd.domain.BannerEntity;
import com.njjd.domain.ColumnArticleEntity;
import com.njjd.domain.ColumnEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.BetterRecyclerView;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.GlideImageLoder2;
import com.njjd.walnuts.ColumnActivity;
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
    private ColumnEntity columnEntity;
    private OnItemClickListener mOnItemClickListener = null;
    private int currentPage = 1;
    private int mHeaderCount = 1;//头部View个数
    private boolean isfirst=true;
    private List<ColumnEntity> columnEntities;
    private LayoutInflater inflater;
    private List<BannerEntity> bannerList = CommonUtils.getInstance().getBannerList();
    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> urls = new ArrayList<>();

    public SelectAdapter(Context context, List<ColumnArticleEntity> list, List<ColumnEntity> columnEntities) {
        this.context = context;
        this.list = list;
        this.columnEntities = columnEntities;
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
            View layout = LayoutInflater.from(context).inflate(R.layout.layout_column2, parent, false);
            holder = new ContentViewHolder(layout);
            layout.setOnClickListener(this);
        } else {
            View layout = LayoutInflater.from(context).inflate(R.layout.item_column, parent, false);
            holder = new ColumnHolder(layout);
            layout.setOnClickListener(this);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
//        if (mHeaderCount != 0 && position < mHeaderCount) {
//            return ITEM_TYPE_HEADER;
//        }else
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
            if((position-1==0)){
                holder.itemView.findViewById(R.id.txt_topsale).setVisibility(View.VISIBLE);
            }else{
                holder.itemView.findViewById(R.id.txt_topsale).setVisibility(View.GONE);

            }
            if (columnArticleEntity.getIs_select().equals("1.0")) {
                holder.itemView.findViewById(R.id.label_select).setVisibility(View.VISIBLE);
            } else {
                holder.itemView.findViewById(R.id.label_select).setVisibility(View.GONE);
            }
            GlideImageLoder.getInstance().displayImage(context, columnArticleEntity.getHead(), ((ContentViewHolder) holder).head);
            ((ContentViewHolder) holder).name.setText(columnArticleEntity.getName()+" • "+columnArticleEntity.getColumnName());
            ((ContentViewHolder) holder).title.setText(columnArticleEntity.getTitle());
            ((ContentViewHolder) holder).content.setText(columnArticleEntity.getDesci());
            GlideImageLoder.getInstance().displayImage(context, HttpManager.BASE_URL2 + columnArticleEntity.getPic().split(",")[0].replace("\"", ""), ((ContentViewHolder) holder).pic);
        } else {
            images.clear();
            titles.clear();
            for (int i = 0; i < columnEntities.size(); i++) {
                images.add(columnEntities.get(i).getCrousel_img());
                titles.add(columnEntities.get(i).getName());
//                    urls.add(bannerList.get(i).get);
            }
            ((ColumnHolder) holder).banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            ((ColumnHolder) holder).banner.setIndicatorGravity(BannerConfig.RIGHT);
            ((ColumnHolder) holder).banner.isAutoPlay(true);
            ((ColumnHolder) holder).banner.setDelayTime(3000);
            ((ColumnHolder) holder).banner.setBannerTitles(titles);
            ((ColumnHolder) holder).banner.setImages(images).setImageLoader(GlideImageLoder2.getInstance()).start();
            ((ColumnHolder) holder).banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Intent intent = new Intent(context, ColumnActivity.class);
                    intent.putExtra("column_id", Float.valueOf(columnEntities.get(position).getId()).intValue() + "");
                    context.startActivity(intent);
//                        Intent intent = new Intent(context, WebViewActivity.class);
//                        intent.putExtra("title", titles.get(position));
//                        intent.putExtra("url", urls.get(position));
//                        context.startActivity(intent);
                }
            });
            //设置布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ((ColumnHolder) holder).recyclerView.setLayoutManager(linearLayoutManager);
            //设置适配器
            GalleryAdapter mAdapter = new GalleryAdapter(context, columnEntities);
            ((ColumnHolder) holder).recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + mHeaderCount;
    }

    class ColumnHolder extends RecyclerView.ViewHolder {
        Banner banner;
        BetterRecyclerView recyclerView;

        public ColumnHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
            recyclerView = itemView.findViewById(R.id.rec_column);
        }
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

    public class GalleryAdapter extends
            RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
        private LayoutInflater mInflater;
        private List<ColumnEntity> mDatas;

        public GalleryAdapter(Context context, List<ColumnEntity> datats) {
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView head;
            TextView title;
            TextView content;
            TextView name;
            ImageView pic;

            public ViewHolder(View itemView) {
                super(itemView);
                head = itemView
                        .findViewById(R.id.img_head);
                name = itemView
                        .findViewById(R.id.txt_name);
                content = itemView
                        .findViewById(R.id.txt_content);
                title =  itemView.findViewById(R.id.txt_title);
                pic =  itemView
                        .findViewById(R.id.img);
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.layout_column,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            columnEntity = mDatas.get(i);
            viewHolder.name.setText(columnEntity.getName());
            viewHolder.title.setText(columnEntity.getDesc());
            viewHolder.content.setText(columnEntity.getUname());
            GlideImageLoder.getInstance().displayImage(context, columnEntity.getUhead(), viewHolder.head);
            GlideImageLoder2.getInstance().displayImage(context, columnEntity.getCrousel_img().split(",")[0].replace("\"", ""), viewHolder.pic);
//            viewHolder.pic.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//            viewHolder.pic.loadUrl(HttpManager.BASE_URL2+columnEntity.getPic().split(",")[0].replace("\"",""));
            viewHolder.pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ColumnActivity.class);
                    intent.putExtra("column_id", Float.valueOf(mDatas.get(i).getId()).intValue() + "");
                    context.startActivity(intent);
                }
            });
        }

    }
}
