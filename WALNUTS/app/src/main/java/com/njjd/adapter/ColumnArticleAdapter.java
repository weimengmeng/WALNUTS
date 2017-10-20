package com.njjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njjd.domain.ColumnArticleEntity;
import com.njjd.utils.GlideImageLoder;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/9/15.
 */

public class ColumnArticleAdapter extends BaseAdapter {
    private Context context;
    private List<ColumnArticleEntity> list;
    private LayoutInflater inflater;
    private ColumnArticleEntity entity;
    private int CurrentPage=1;

    public int getCurrentPage() {
        return CurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }

    public ColumnArticleAdapter(Context context, List<ColumnArticleEntity> list){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodel hodel=null;
        if(convertView==null){
            hodel=new ViewHodel();
            convertView=inflater.inflate(R.layout.layout_column_article,parent,false);
            hodel.head=(ImageView) convertView.findViewById(R.id.img_head);
            hodel.name=(TextView)convertView.findViewById(R.id.txt_name);
            hodel.title=(TextView)convertView.findViewById(R.id.txt_title);
            hodel.content=(TextView)convertView.findViewById(R.id.txt_content);
            hodel.num=(TextView)convertView.findViewById(R.id.txt_answerNum);
            hodel.pic=(ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(hodel);
        }else{
            hodel=(ViewHodel)convertView.getTag();
        }
        entity=list.get(position);
        GlideImageLoder.getInstance().displayImage(context, entity.getHead(), hodel.head);
        hodel.name.setText(entity.getName());
        hodel.title.setText(entity.getTitle());
        hodel.content.setText(entity.getContent());
        hodel.num.setText(Float.valueOf(entity.getCommentNum()).intValue()+"评论  ·  "+Float.valueOf(entity.getPointNum()).intValue()+"赞同");
        GlideImageLoder.getInstance().displayImage(context, entity.getPic(), hodel.pic);
        return convertView;
    }
    private class ViewHodel{
        ImageView head;
        TextView title;
        TextView name;
        TextView content;
        ImageView pic;
        TextView num;
    }
}
