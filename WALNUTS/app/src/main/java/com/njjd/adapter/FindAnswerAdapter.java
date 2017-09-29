package com.njjd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.domain.BannerEntity;
import com.njjd.domain.ColumnEntity;
import com.njjd.domain.SelectedAnswerEntity;
import com.njjd.domain.SpecialEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.GlideImageLoder2;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;
import com.njjd.walnuts.WebViewActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrwim on 17/9/14.
 */

public class FindAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context context;
    private List<SpecialEntity> list;
    private SpecialEntity entity;
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ONE_ITEM = 1;//精选回答
    public static final int TWO_ITEM = 2;//专栏类型
    private SelectedAnswerEntity selectedAnswerEntity;
    private ColumnEntity columnEntity;
    private OnItemClickListener mOnItemClickListener = null;
    private int currentPage=1;
    private int mHeaderCount = 1;//头部View个数
    private List<ColumnEntity> columnEntities;
    private Banner banner;
    private LayoutInflater inflater;
    private List<BannerEntity> bannerList= CommonUtils.getInstance().getBannerList();
    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    public FindAnswerAdapter(Context context,List<SpecialEntity> list,List<ColumnEntity> columnEntities){
        this.context=context;
        this.list=list;
        this.columnEntities=columnEntities;
        inflater=LayoutInflater.from(context);
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
        if (viewType == ITEM_TYPE_HEADER) {
            if(banner==null) {
                banner = (Banner) inflater.inflate(R.layout.layout_banner, parent, false);
                for (int i = 0; i < bannerList.size()&&urls.size()<1; i++) {
                    if(bannerList.get(i).getType().equals("4.0")) {
                        images.add(bannerList.get(i).getImg());
                        titles.add(bannerList.get(i).getTitle());
                        urls.add(bannerList.get(i).getUrl());
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
//                        Intent intent = new Intent(context, WebViewActivity.class);
//                        intent.putExtra("title", titles.get(position));
//                        intent.putExtra("url", urls.get(position));
//                        context.startActivity(intent);
                    }
                });
            }
            return new HeaderViewHolder(banner);
        }else if(ONE_ITEM == viewType){
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
        if (mHeaderCount != 0 && position < mHeaderCount) {
            return ITEM_TYPE_HEADER;
        } else  if(list.get(position).getColumnEntity()!=null){
            return TWO_ITEM;
        }else{
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
        if (holder instanceof HeaderViewHolder) {
        }else {
            entity = list.get(position);
            if (holder instanceof ContentViewHolder) {
                holder.itemView.setTag(position);
                selectedAnswerEntity = entity.getAnswerEntity();
                if (position == (columnEntities.size()+1)) {
                    ((ContentViewHolder) holder).itemView.findViewById(R.id.txt_select).setVisibility(View.GONE);
                } else {
                    ((ContentViewHolder) holder).itemView.findViewById(R.id.txt_select).setVisibility(View.GONE);
                }
                ((ContentViewHolder) holder).name.setText(selectedAnswerEntity.getName());
                ((ContentViewHolder) holder).message.setText(selectedAnswerEntity.getMessage());
                ((ContentViewHolder) holder).title.setText(selectedAnswerEntity.getTitle());
                GlideImageLoder.getInstance().displayImage(context, selectedAnswerEntity.getHead(), ((ContentViewHolder) holder).head);
                if ("".equals(selectedAnswerEntity.getPhoto())) {
                    ((ContentViewHolder) holder).pic.setVisibility(View.GONE);
                } else {
                    ((ContentViewHolder) holder).pic.setVisibility(View.VISIBLE);
                    String[] strings = selectedAnswerEntity.getPhoto().split(",");
                    GlideImageLoder.getInstance().displayImage(context, HttpManager.BASE_URL2+strings[0].replace("\"", ""), ((ContentViewHolder) holder).pic);
                }
                ((ContentViewHolder) holder).reply.setText(selectedAnswerEntity.getReplyContent());
                ((ContentViewHolder) holder).focus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Map<String, Object> map = new HashMap<>();
                        map.put("uid", SPUtils.get(context, "userId", "").toString());
                        map.put("token", SPUtils.get(context, "token", "").toString());
                        map.put("be_uid", list.get(position).getAnswerEntity().getUid());
                        if(list.get(position).getAnswerEntity().getIsFocus().equals("1.0")){
                            map.put("select",0);
                        }else{
                            map.put("select",1);
                        }
                        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
                            @Override
                            public void onNext(Object o) {
                                if(map.get("select").toString().equals("0")){
                                    list.get(position).getAnswerEntity().setIsFocus("0.0");
                                    for(int i=0;i<list.size();i++){
                                        if(list.get(i).getAnswerEntity().getUid().equals(list.get(position).getAnswerEntity().getUid())){
                                            list.get(i).getAnswerEntity().setIsFocus("0.0");
                                        }
                                    }
                                    ((ContentViewHolder) holder).focus.setText("取消关注");
                                    ((ContentViewHolder) holder).focus.setTextColor(context.getResources().getColor(R.color.white));
                                    ((ContentViewHolder) holder).focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div));
                                }else{
                                    list.get(position).getAnswerEntity().setIsFocus("1.0");
                                    for(int i=0;i<list.size();i++){
                                        if(list.get(i).getAnswerEntity().getUid().equals(list.get(position).getAnswerEntity().getUid())){
                                            list.get(i).getAnswerEntity().setIsFocus("1.0");
                                        }
                                    }
                                    ((ContentViewHolder) holder).focus.setText("+关注TA");
                                    ((ContentViewHolder) holder).focus.setTextColor(context.getResources().getColor(R.color.txt_color));
                                    ((ContentViewHolder) holder).focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div_grey));
                                }
                                notifyDataSetChanged();
                            }
                        }, context, true, false), map);
                        HttpManager.getInstance().followUser(postEntity);
                    }
                });
                if (selectedAnswerEntity.getIsFocus().equals("1.0")) {
                    ((ContentViewHolder) holder).focus.setText("取消关注");
                    ((ContentViewHolder) holder).focus.setTextColor(context.getResources().getColor(R.color.white));
                    ((ContentViewHolder) holder).focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div));
                } else {
                    ((ContentViewHolder) holder).focus.setText("+关注TA");
                    ((ContentViewHolder) holder).focus.setTextColor(context.getResources().getColor(R.color.txt_color));
                    ((ContentViewHolder) holder).focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div_grey));
                }
            } else {
                holder.itemView.setTag(position);
                if (position != 0) {
                    ((ColumnHolder) holder).itemView.findViewById(R.id.txt_topsale).setVisibility(View.GONE);
                }
                columnEntity = entity.getColumnEntity();
                ((ColumnHolder) holder).name.setText(columnEntity.getName());
                ((ColumnHolder) holder).title.setText(columnEntity.getTitle());
                ((ColumnHolder) holder).content.setText(columnEntity.getContent());
                GlideImageLoder.getInstance().displayImage(context, columnEntity.getHead(), ((ColumnHolder) holder).head);
                GlideImageLoder.getInstance().displayImage(context, columnEntity.getPic(), ((ColumnHolder) holder).pic);
            }
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
        TextView focus;
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
            focus = (TextView) itemView.findViewById(R.id.txt_focus);
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
