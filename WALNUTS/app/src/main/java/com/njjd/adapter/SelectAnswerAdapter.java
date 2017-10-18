package com.njjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.domain.SelectedAnswerEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.walnuts.PeopleInfoActivity;
import com.njjd.walnuts.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrwim on 17/9/23.
 */

public class SelectAnswerAdapter extends BaseAdapter {
    private Context context;
    private List<SelectedAnswerEntity> list;
    private SelectedAnswerEntity entity;
    private LayoutInflater inflater;
    public SelectAnswerAdapter(Context context,List<SelectedAnswerEntity> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ContentViewHolder hodel=null;
        if(convertView==null){
            hodel=new ContentViewHolder();
            convertView=inflater.inflate(R.layout.item_selected,parent,false);
            hodel.head=(ImageView) convertView.findViewById(R.id.img_head);
            hodel.name=(TextView)convertView.findViewById(R.id.txt_name);
            hodel.title=(TextView)convertView.findViewById(R.id.txt_title);
            hodel.message=(TextView)convertView.findViewById(R.id.txt_message);
            hodel.pic = (ImageView)convertView
                    .findViewById(R.id.img1);
            hodel.reply = (TextView) convertView.findViewById(R.id.txt_reply);
            hodel.focus = (TextView) convertView.findViewById(R.id.txt_focus);
            convertView.setTag(hodel);
        }else{
            hodel=(ContentViewHolder) convertView.getTag();
        }
        entity=list.get(position);
        if (position == 0) {
            convertView.findViewById(R.id.txt_select).setVisibility(View.VISIBLE);
            ((TextView)convertView.findViewById(R.id.txt_select)).setText("本期推荐");
        } else {
            convertView.findViewById(R.id.txt_select).setVisibility(View.GONE);
        }
        hodel.name.setText(entity.getName());
        hodel.message.setText(entity.getMessage());
        hodel.title.setText(entity.getTitle());
        GlideImageLoder.getInstance().displayImage(context, entity.getHead(), hodel.head);
        if ("".equals(entity.getPhoto())) {
            hodel.pic.setVisibility(View.GONE);
        } else {
            hodel.pic.setVisibility(View.VISIBLE);
            String[] strings = entity.getPhoto().split(",");
            GlideImageLoder.getInstance().displayImage(context, HttpManager.BASE_URL2+strings[0].replace("\"", ""), hodel.pic);
        }
        hodel.reply.setText(entity.getReplyContent());
        final ContentViewHolder myhodel=hodel;
        hodel.focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final Map<String, Object> map = new HashMap<>();
                map.put("uid", SPUtils.get(context, "userId", "").toString());
                map.put("token", SPUtils.get(context, "token", "").toString());
                map.put("be_uid", list.get(position).getUid());
                if(list.get(position).getIsFocus().equals("1.0")){
                    map.put("select",0);
                }else{
                    map.put("select",1);
                }
                SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
                    @Override
                    public void onNext(Object o) {
                        if(map.get("select").toString().equals("0")){
                            list.get(position).setIsFocus("0.0");
                            for(int i=0;i<list.size();i++){
                                if(list.get(i).getUid().equals(list.get(position).getUid())){
                                    list.get(i).setIsFocus("0.0");
                                }
                            }
                            myhodel.focus.setText("取消关注");
                            myhodel.focus.setTextColor(context.getResources().getColor(R.color.txt_color));
                            myhodel.focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div_grey));
                            SPUtils.put(context,"focus",Integer.valueOf(SPUtils.get(context,"focus",0).toString())-1);
                        }else{
                            list.get(position).setIsFocus("1.0");
                            for(int i=0;i<list.size();i++){
                                if(list.get(i).getUid().equals(list.get(position).getUid())){
                                    list.get(i).setIsFocus("1.0");
                                }
                            }
                            myhodel.focus.setText("+关注TA");
                            myhodel.focus.setTextColor(context.getResources().getColor(R.color.white));
                            myhodel.focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div));
                            SPUtils.put(context,"focus",Integer.valueOf(SPUtils.get(context,"focus",0).toString())+1);
                        }
                        notifyDataSetChanged();
                    }
                }, context, true, false), map);
                HttpManager.getInstance().followUser(postEntity);
            }
        });
        if (entity.getIsFocus().equals("1.0")) {
            hodel.focus.setText("取消关注");
            hodel.focus.setTextColor(context.getResources().getColor(R.color.txt_color));
            hodel.focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div_grey));
        } else {
            hodel.focus.setText("+关注TA");
            hodel.focus.setTextColor(context.getResources().getColor(R.color.white));
            hodel.focus.setBackground(context.getResources().getDrawable(R.drawable.background_button_div));
        }
        return convertView;
    }
    //内容 ViewHolder
    public class ContentViewHolder{
        ImageView head;
        TextView title;
        TextView name;
        TextView message;
        ImageView pic;
        TextView reply;
        TextView focus;
    }

}
