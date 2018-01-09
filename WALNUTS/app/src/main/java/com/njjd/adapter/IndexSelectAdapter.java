package com.njjd.adapter;

import android.content.Context;
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
import com.njjd.domain.SelectedAnswerEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.GlideImageLoder2;
import com.njjd.utils.SPUtils;
import com.njjd.walnuts.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrwim on 17/9/14.
 */

public class IndexSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<SelectedAnswerEntity> list;
    public static final int ONE_ITEM = 1;//精选回答
    public static final int TWO_ITEM = 2;//专栏类型
    private SelectedAnswerEntity selectedAnswerEntity;
    private OnItemClickListener mOnItemClickListener = null;
    private int currentPage = 1;
    private int mHeaderCount = 1;//头部View个数
    private LayoutInflater inflater;
    private List<BannerEntity> bannerList = CommonUtils.getInstance().getBannerList();
    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private Banner banner;
    public IndexSelectAdapter(Context context, List<SelectedAnswerEntity> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TWO_ITEM == viewType) {
            banner = (Banner) inflater.inflate(R.layout.layout_banner, parent, false);
            for (int i = 0; i < bannerList.size()&&images.size()<1; i++) {
                if (bannerList.get(i).getType().equals("4.0")&&images.size()<1) {
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
            return new HeaderViewHolder(banner);
        } else {
            View layout = LayoutInflater.from(context).inflate(R.layout.item_selected, parent, false);
            ContentViewHolder holder = new ContentViewHolder(layout);
            layout.setOnClickListener(this);
            return holder;
        }
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
            holder.itemView.setTag(position-1);
            selectedAnswerEntity = list.get(position-1);
            if(position==1){
               holder.itemView.findViewById(R.id.txt_select).setVisibility(View.VISIBLE);
            }else{
                holder.itemView.findViewById(R.id.txt_select).setVisibility(View.GONE);
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
                GlideImageLoder.getInstance().displayImage(context, HttpManager.BASE_URL2 + strings[0].replace("\"", ""), ((ContentViewHolder) holder).pic);
            }
            ((ContentViewHolder) holder).reply.setText(selectedAnswerEntity.getReplyContent());
            ((ContentViewHolder) holder).focus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Map<String, Object> map = new HashMap<>();
                    map.put("uid", SPUtils.get(context, "userId", "").toString());
                    map.put("token", SPUtils.get(context, "token", "").toString());
                    map.put("be_uid", list.get(position-1).getUid());
                    if (list.get(position-1).getIsFocus().equals("1.0")) {
                        map.put("select", 0);
                    } else {
                        map.put("select", 1);
                    }
                    SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
                        @Override
                        public void onNext(Object o) {
                            if (map.get("select").toString().equals("0")) {
                                list.get(position-1).setIsFocus("0.0");
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getUid().equals(list.get(position-1).getUid())) {
                                        list.get(i).setIsFocus("0.0");
                                    }
                                }
                                ((ContentViewHolder) holder).focus.setText("取消关注");
                                ((ContentViewHolder) holder).focus.setTextColor(context.getResources().getColor(R.color.txt_color));
                                ((ContentViewHolder) holder).focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div_grey));
                                SPUtils.put(context, "focus", Integer.valueOf(SPUtils.get(context, "focus", 0).toString()) - 1);
                            } else {
                                list.get(position-1).setIsFocus("1.0");
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getUid().equals(list.get(position-1).getUid())) {
                                        list.get(i).setIsFocus("1.0");
                                    }
                                }
                                ((ContentViewHolder) holder).focus.setText("+关注TA");
                                ((ContentViewHolder) holder).focus.setTextColor(context.getResources().getColor(R.color.white));
                                ((ContentViewHolder) holder).focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div));
                                SPUtils.put(context, "focus", Integer.valueOf(SPUtils.get(context, "focus", 0).toString()) + 1);
                            }
                            notifyDataSetChanged();
                        }
                    }, context, true, false), map);
                    HttpManager.getInstance().followUser(postEntity);
                }
            });
            if (selectedAnswerEntity.getIsFocus().equals("1.0")) {
                ((ContentViewHolder) holder).focus.setText("取消关注");
                ((ContentViewHolder) holder).focus.setTextColor(context.getResources().getColor(R.color.txt_color));
                ((ContentViewHolder) holder).focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div_grey));
            } else {
                ((ContentViewHolder) holder).focus.setText("+关注TA");
                ((ContentViewHolder) holder).focus.setTextColor(context.getResources().getColor(R.color.white));
                ((ContentViewHolder) holder).focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div));
            }
        } else {
//            for (int i = 0; i < bannerList.size()&&images.size()<1; i++) {
//                if (bannerList.get(i).getType().equals("4.0")&&images.size()<1) {
//                    images.add(bannerList.get(i).getImg());
//                    titles.add(bannerList.get(i).getTitle());
//                    urls.add(bannerList.get(i).getUrl());
//                }
//            }
//            ((ColumnHolder) holder).banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
//            ((ColumnHolder) holder).banner.setIndicatorGravity(BannerConfig.RIGHT);
//            ((ColumnHolder) holder).banner.isAutoPlay(true);
//            ((ColumnHolder) holder).banner.setDelayTime(3000);
//            ((ColumnHolder) holder).banner.setImages(images).setImageLoader(GlideImageLoder2.getInstance()).start();
//            ((ColumnHolder) holder).banner.setOnBannerListener(new OnBannerListener() {
//                @Override
//                public void OnBannerClick(int position) {
////                        Intent intent = new Intent(context, WebViewActivity.class);
////                        intent.putExtra("title", titles.get(position));
////                        intent.putExtra("url", urls.get(position));
////                        context.startActivity(intent);
//                }
//            });
        }
    }
    @Override
    public int getItemCount() {
        return list.size()+mHeaderCount;
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
            head = itemView
                    .findViewById(R.id.img_head);
            name = itemView
                    .findViewById(R.id.txt_name);
            message =  itemView.findViewById(R.id.txt_message);
            title = itemView.findViewById(R.id.txt_title);
            pic =  itemView
                    .findViewById(R.id.img1);
            reply =  itemView.findViewById(R.id.txt_reply);
            focus =  itemView.findViewById(R.id.txt_focus);
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
