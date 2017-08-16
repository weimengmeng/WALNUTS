package com.njjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njjd.domain.SaveEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.GlideImageLoder;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/8/16.
 */

public class MySaveAdapter extends BaseAdapter{
    private List<SaveEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private SaveEntity saveEntity;
    public static int CURRENT_PAGE = 1;
    public MySaveAdapter(List<SaveEntity> list,Context context){
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
            convertView=inflater.inflate(R.layout.item_save,parent,false);
            hodel.image_head=(ImageView) convertView.findViewById(R.id.img_head);
            hodel.name=(TextView)convertView.findViewById(R.id.txt_name);
            hodel.introduction=(TextView)convertView.findViewById(R.id.txt_message);
            hodel.title=(TextView)convertView.findViewById(R.id.txt_title);
            hodel.image_photo=(ImageView) convertView.findViewById(R.id.img);
            hodel.reply_content=(TextView)convertView.findViewById(R.id.txt_reply);
            convertView.setTag(hodel);
        }else{
            hodel=(ViewHodel) convertView.getTag();
        }
        saveEntity=list.get(position);
        GlideImageLoder.getInstance().displayImage(context, saveEntity.getComment_uid_headimg(), hodel.image_head);
        hodel.name.setText(saveEntity.getComment_uid_name());
        hodel.introduction.setText(saveEntity.getComment_uid_introduction());
        hodel.title.setText(saveEntity.getTitle());
        hodel.reply_content.setText(saveEntity.getComment_uid_name()+": "+saveEntity.getComment_content());
        if ("".equals(saveEntity.getArticle_imgs())) {
            hodel.image_photo.setVisibility(View.GONE);
        } else {
            hodel.image_photo.setVisibility(View.VISIBLE);
            String[] strings = saveEntity.getArticle_imgs().split(",");
            GlideImageLoder.getInstance().displayImage(context, HttpManager.BASE_URL2+strings[0].replace("\"", ""), hodel.image_photo);
        }
        return convertView;
    }
    private class ViewHodel{
        ImageView image_head;
        TextView name;
        TextView introduction;
        TextView title;
        ImageView image_photo;
        TextView reply_content;
    }
}
