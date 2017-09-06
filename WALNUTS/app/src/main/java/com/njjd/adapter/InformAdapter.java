package com.njjd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.njjd.domain.InformEntity;
import com.njjd.utils.DateUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;

import org.json.JSONException;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/8/22.
 */

public class InformAdapter extends RecyclerView.Adapter<InformAdapter.ViewHolder>  implements View.OnClickListener{
    public static int CURRENT_PAGE=1;
    private List<InformEntity> list;
    private Context context;
    private OnItemClickListener mOnItemClickListener = null;
    private InformEntity tempEntity;
    private LayoutInflater inflater;
    public InformAdapter(Context context,List<InformEntity> list){
        this.list=list;
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_inform, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        tempEntity=list.get(position);
        viewHolder.itemView.setTag(position);
        GlideImageLoder.getInstance().displayImage(context,tempEntity.getHeadimg(),viewHolder.image_head);
        viewHolder.image_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PeopleInfoActivity.class);
                intent.putExtra("uid",list.get(position).getUid());
                context.startActivity(intent);
            }
        });
        switch (tempEntity.getType()){
//            0 系统通知 1 关注用户 2 关注问题 3回答问题 4 评论回答 5 收藏回答 6 点赞",
            case "0.0":
                viewHolder.title.setText("系统通知");
                viewHolder.content.setText(tempEntity.getContents());
                break;
            case "1.0":
                viewHolder.title.setText(tempEntity.getUname()+" 关注了你");
                viewHolder.content.setText("被关注了？快去看看");
                break;
            case "2.0":
                viewHolder.title.setText(tempEntity.getUname()+" 关注了你的问题");
                try {
                    viewHolder.content.setText(tempEntity.getContent().getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case "3.0":
                try {
                    viewHolder.title.setText(tempEntity.getUname()+" 等"+Float.valueOf(tempEntity.getContent().getString("answer_num")).intValue()+"人回答了你的问题");
                    viewHolder.content.setText(tempEntity.getContent().getString("contents"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "4.0":
                try {
                    if(tempEntity.getContent().getString("level").equals("1.0")){
                        viewHolder.title.setText(tempEntity.getUname()+" 等"+Float.valueOf(tempEntity.getContent().getString("answer_num")).intValue()+"人回复了你的回答");
                    }else{
                        viewHolder.title.setText(tempEntity.getUname()+" 等"+Float.valueOf(tempEntity.getContent().getString("answer_num")).intValue()+"人回复了你的评论");
                    }
                    viewHolder.content.setText(tempEntity.getContent().getString("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "5.0":
                viewHolder.title.setText(tempEntity.getUname()+" 收藏了你的回答");
                try {
                    viewHolder.content.setText(tempEntity.getContent().getString("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "6.0":
                viewHolder.title.setText(tempEntity.getUname()+" 认同了你的回答");
                try {
                    viewHolder.content.setText(tempEntity.getContent().getString("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
        ParsePosition pos = new ParsePosition(0);
        viewHolder.time.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tempEntity.getAdd_time(), pos)));
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image_head;
        TextView title;
        TextView content;
        TextView time;

        public ViewHolder(View view) {
            super(view);
            image_head=(CircleImageView) view.findViewById(R.id.img_head);
            title=(TextView) view.findViewById(R.id.txt_name);
            content=(TextView) view.findViewById(R.id.txt_content);
            time=(TextView) view.findViewById(R.id.txt_time);
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
