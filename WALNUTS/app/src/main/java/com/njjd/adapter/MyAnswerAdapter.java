package com.njjd.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njjd.domain.MyAnswerEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.LogUtils;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/8/16.
 */

public class MyAnswerAdapter extends BaseAdapter{
    private List<MyAnswerEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private MyAnswerEntity myAnswerEntity;
    public static int CURRENT_PAGE = 1;
    public MyAnswerAdapter(List<MyAnswerEntity> list,Context context){
        this.list=list;
        this.context=context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodel hodel=null;
        if(convertView==null){
            hodel=new ViewHodel();
            convertView=inflater.inflate(R.layout.item_my_answer,parent,false);
            hodel.title=(TextView)convertView.findViewById(R.id.txt_title);
            hodel.image_photo=(ImageView) convertView.findViewById(R.id.img);
            hodel.reply_content=(TextView)convertView.findViewById(R.id.txt_reply);
            hodel.time=(TextView)convertView.findViewById(R.id.txt_time);
            convertView.setTag(hodel);
        }else{
            hodel=(ViewHodel) convertView.getTag();
        }

        myAnswerEntity=list.get(position);
        if(myAnswerEntity.getStat().equals("0.0")){
            hodel.title.setText("评论包含违规内容,已被删除");
            hodel.title.setTextColor(Color.RED);
        }else{
            hodel.title.setText(myAnswerEntity.getTitle());
        }
        hodel.reply_content.setText(myAnswerEntity.getComment_content());
        hodel.time.setText(myAnswerEntity.getAdd_time());
        if ("".equals(myAnswerEntity.getArticle_imgs())) {
            hodel.image_photo.setVisibility(View.GONE);
        } else {
            hodel.image_photo.setVisibility(View.VISIBLE);
            String[] strings = myAnswerEntity.getArticle_imgs().split(",");
            GlideImageLoder.getInstance().displayImage(context, HttpManager.BASE_URL2+strings[0].replace("\"", ""), hodel.image_photo);
        }
        return convertView;
    }
    private class ViewHodel{
        TextView title;
        ImageView image_photo;
        TextView reply_content;
        TextView time;
    }
}
