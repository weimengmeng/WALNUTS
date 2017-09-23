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
import com.njjd.utils.GlideImageLoder2;
import com.njjd.utils.LogUtils;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;
import com.njjd.walnuts.SelectQuestionActivity;
import com.njjd.walnuts.WebViewActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexQuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    //模拟数据
    private List<QuestionEntity> mList;
    private Context mContext;
    private ImageView head;
    private LayoutInflater inflater;
    public static int currentPage = 1;
    private Banner banner;
    private List<BannerEntity> bannerList= CommonUtils.getInstance().getBannerList();
    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private List<String> ids = new ArrayList<>();
    private IndexQuestionAdapter.OnItemClickListener mOnItemClickListener = null;
    private int mHeaderCount = 1;//头部View个数
    private String kind="0";
    public IndexQuestionAdapter(Context context, List<QuestionEntity> list,String kind) {
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(context);
        this.kind=kind;

    }
    //内容长度
    public int getContentItemCount() {
        return mList.size();
    }
    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
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
        if (mHeaderCount != 0 && position < mHeaderCount) {
            bannerList= CommonUtils.getInstance().getBannerList();
            return ITEM_TYPE_HEADER;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    //内容 ViewHolder
    public class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;
        LinearLayout lvHead;
        TextView content;
        TextView total;
        TextView createTime;
        TextView focusNum;
        TextView answerNum;
        public ContentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_title);
            imageView = (ImageView) itemView
                    .findViewById(R.id.img1);
            lvHead = (LinearLayout) itemView
                    .findViewById(R.id.lv_head);
            content= (TextView) itemView
                    .findViewById(R.id.txt_content);
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
        if (viewType == ITEM_TYPE_HEADER) {
                banner = (Banner) inflater.inflate(R.layout.layout_banner, parent, false);
                images.clear();
                titles.clear();
                urls.clear();
                ids.clear();
                for(int i=0;i<bannerList.size();i++){
                    if(bannerList.get(i).getType().equals(this.kind)&&!images.contains(bannerList.get(i).getImg())) {
                        images.add(bannerList.get(i).getImg());
                        titles.add(bannerList.get(i).getTitle());
                        urls.add(bannerList.get(i).getUrl());
                        ids.add(bannerList.get(i).getType());
                    }
                }
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                banner.setIndicatorGravity(BannerConfig.RIGHT);
                banner.isAutoPlay(true);
                banner.setDelayTime(3000);
                banner.setImages(images).setImageLoader(GlideImageLoder2.getInstance()).start();
                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Intent intent;
                        if(!"".equals(urls.get(position))){
                            intent = new Intent(mContext, WebViewActivity.class);
                            intent.putExtra("title", titles.get(position));
                            intent.putExtra("url", urls.get(position));
                            mContext.startActivity(intent);
                        }else{
                            intent = new Intent(mContext, SelectQuestionActivity.class);
                            intent.putExtra("title", titles.get(position));
                            intent.putExtra("id",ids.get(position));
                            mContext.startActivity(intent);
                        }
                    }
                });
            return new HeaderViewHolder(banner);
        } else if (viewType == mHeaderCount) {
            View layout = LayoutInflater.from(mContext).inflate(R.layout.item_index, parent, false);
            ContentViewHolder vh = new ContentViewHolder(layout);
            layout.setOnClickListener(this);
            return vh;
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
            ((ContentViewHolder) holder).focusNum.setText("关注 "+Float.valueOf(temp.getFocusNum()).intValue());
            ((ContentViewHolder) holder).answerNum.setText("回答 "+Float.valueOf(temp.getAnswerNum()).intValue());
            if(Float.valueOf(temp.getAnswerNum()).intValue() + Float.valueOf(temp.getFocusNum()).intValue()==0){
                ((ContentViewHolder) holder).total.setText("提出了该问题");
            }else{

                ((ContentViewHolder) holder).total.setText("等" + (Float.valueOf(temp.getPart_num()).intValue()) + "人参与");
            }
            ParsePosition pos = new ParsePosition(0);
            ((ContentViewHolder) holder).createTime.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(temp.getDateTime(), pos)));
            if ("".equals(temp.getPhoto())) {
                ((ContentViewHolder) holder).imageView.setVisibility(View.GONE);
                ((ContentViewHolder) holder).content.setVisibility(View.VISIBLE);
                ((ContentViewHolder) holder).content.setText(temp.getContent());
            } else {
                ((ContentViewHolder) holder).content.setVisibility(View.GONE);
                ((ContentViewHolder) holder).imageView.setVisibility(View.VISIBLE);
                String[] strings = temp.getPhoto().split(",");
                GlideImageLoder2.getInstance().displayImage(mContext, strings[0].replace("\"", ""), ((ContentViewHolder) holder).imageView);
            }
            String[] strs = temp.getPic().split(",");
            final String[] uids=temp.getUids().split(",");
            if ("".equals(temp.getPic())) {
                ((ContentViewHolder) holder).lvHead.removeAllViews();
            } else {
                ((ContentViewHolder) holder).lvHead.removeAllViews();
                ((ContentViewHolder) holder).lvHead.setVisibility(View.VISIBLE);
                LinearLayout layout;
                for (int i = 0; i < strs.length && i <1; i++) {
                    final int temp1=i;
                    layout = (LinearLayout) inflater.inflate(R.layout.layout_head, null);
                    head = (ImageView) layout.findViewById(R.id.head);
                    GlideImageLoder.getInstance().displayImage(mContext, strs[i], head);
                    ((ContentViewHolder) holder).lvHead.addView(layout);
                    head.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(mContext,PeopleInfoActivity.class);
                            intent.putExtra("uid",uids[temp1]);
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
        return mHeaderCount + getContentItemCount();
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(IndexQuestionAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}