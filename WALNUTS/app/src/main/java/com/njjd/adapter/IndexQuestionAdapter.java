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

import com.njjd.domain.QuestionEntity;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;
    //模拟数据
    private List<QuestionEntity> mList;
    private Context mContext;
    private Typeface typeface;
    private ImageView head;
    private LayoutInflater inflater;
    private int index = 0;
    public static int currentPage = 1;
    private Banner banner;
    private List<String> images = new ArrayList<>(Arrays.asList("file:///android_asset/banner.png",
            "http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg",
            "http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg",
            "http://img.zcool.cn/community/0114a856640b6d32f87545731c076a.jpg"));
    private List<String> titles = new ArrayList<>(Arrays.asList("12趁现在", "嗨购5折不要停，12.12趁现在", "实打实大顶顶顶顶"));
    private IndexQuestionAdapter.OnItemClickListener mOnItemClickListener = null;
    private int mHeaderCount = 1;//头部View个数
    private int mBottomCount = 1;//底部View个数

    public IndexQuestionAdapter(Context context, List<QuestionEntity> list) {
        this.mContext = context;
        this.mList = list;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansHans-Medium.ttf");
        inflater = LayoutInflater.from(context);

    }

    //内容长度
    public int getContentItemCount() {
        return mList.size();
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
    }

    //判断当前item是否是FooterView
    public boolean isBottomView(int position) {
        return mBottomCount != 0 && position >= (mHeaderCount + getContentItemCount());
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
        int dataItemCount = getContentItemCount();
        if (mHeaderCount != 0 && position < mHeaderCount) {
            return ITEM_TYPE_HEADER;
        } else if (mBottomCount != 0 && position >= (mHeaderCount + dataItemCount)) {
            return ITEM_TYPE_BOTTOM;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    //内容 ViewHolder
    public class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;
        LinearLayout lvHead;
        TextView total;
        TextView createTime;

        public ContentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_title);
            title.setTypeface(typeface);
            imageView = (ImageView) itemView
                    .findViewById(R.id.img1);
            lvHead = (LinearLayout) itemView
                    .findViewById(R.id.lv_head);
            total = (TextView) itemView.findViewById(R.id.txt_total);
            createTime = (TextView) itemView
                    .findViewById(R.id.txt_time);
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
        if (viewType == ITEM_TYPE_HEADER) {
            banner = (Banner) inflater.inflate(R.layout.layout_banner, parent, false);
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            banner.setIndicatorGravity(BannerConfig.RIGHT);
            banner.isAutoPlay(true);
            banner.setDelayTime(3000);
            banner.setImages(images).setImageLoader(GlideImageLoder.getInstance()).start();
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    ToastUtils.showShortToast(mContext, "你点击了：" + position);
                }
            });
            return new HeaderViewHolder(banner);
        } else if (viewType == mHeaderCount) {
            View layout = LayoutInflater.from(mContext).inflate(R.layout.item_index, parent, false);
            ContentViewHolder vh = new ContentViewHolder(layout);
            layout.setOnClickListener(this);
            return vh;
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_foot, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
        } else if (holder instanceof ContentViewHolder) {
            holder.itemView.setTag(position - mHeaderCount);
            final QuestionEntity temp = mList.get(position - mHeaderCount);
            ((ContentViewHolder) holder).title.setText(temp.getTitle());
            ((ContentViewHolder) holder).total.setText("等  " + (Float.valueOf(temp.getAnswerNum()).intValue() + Float.valueOf(temp.getFocusNum()).intValue()) + "  人参与");
            ((ContentViewHolder) holder).createTime.setText(temp.getDateTime());
            if ("".equals(temp.getPhoto())) {
                ((ContentViewHolder) holder).imageView.setVisibility(View.GONE);
            } else {
                ((ContentViewHolder) holder).imageView.setVisibility(View.VISIBLE);
                String[] strings = temp.getPhoto().split(",");
                GlideImageLoder.getInstance().displayImage(mContext, strings[0].replace("\"", ""), ((ContentViewHolder) holder).imageView);
            }
            String[] strs = temp.getPic().split(",");
            if ("".equals(temp.getPic())) {
                ((ContentViewHolder) holder).lvHead.removeAllViews();
            } else {
                ((ContentViewHolder) holder).lvHead.removeAllViews();
                ((ContentViewHolder) holder).lvHead.setVisibility(View.VISIBLE);
                LinearLayout layout;
                for (int i = 0; i < strs.length && i < 3; i++) {
                    layout = (LinearLayout) inflater.inflate(R.layout.layout_head, null);
                    head = (ImageView) layout.findViewById(R.id.head);
                    head.setId(i);
                    GlideImageLoder.getInstance().displayImage(mContext, strs[i], head);
                    ((ContentViewHolder) holder).lvHead.addView(layout);
                    head.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mContext.startActivity(new Intent(mContext, PeopleInfoActivity.class));
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
        return mHeaderCount + getContentItemCount() + mBottomCount;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(IndexQuestionAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}