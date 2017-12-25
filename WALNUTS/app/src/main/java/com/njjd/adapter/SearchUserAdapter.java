package com.njjd.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njjd.domain.SearchArticleEntity;
import com.njjd.domain.SearchUserEntity;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.LogUtils;
import com.njjd.utils.TextUtil;
import com.njjd.walnuts.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/12/6.
 */

public class SearchUserAdapter extends BaseAdapter {
    public static int CURRENTPAGE=1;
    private List<SearchUserEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private SearchUserEntity userEntity;
    private String text="";
    public SearchUserAdapter(Context context,List<SearchUserEntity> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        ViewHolder holder=null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView=inflater.inflate(R.layout.item_search_user,null);
            holder.uname= convertView.findViewById(R.id.search_name);
            holder.area=convertView.findViewById(R.id.search_area);
            holder.head= convertView.findViewById(R.id.img_head);
            holder.industry= convertView.findViewById(R.id.search_industry);
            holder.product= convertView.findViewById(R.id.search_product);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        userEntity=list.get(position);
        holder.uname.setText(userEntity.getUname());
        holder.area.setText(userEntity.getArea());
//        holder.uname.setSpecifiedTextsColor(userEntity.getUname(), text, Color.parseColor("#ffb129"));
//        holder.area.setSpecifiedTextsColor(userEntity.getArea(), text, Color.parseColor("#ffb129"));
        GlideImageLoder.getInstance().displayImage(context,userEntity.getHeadimg(),holder.head);
//        holder.industry.setSpecifiedTextsColor(userEntity.getIndustry1()+"-"+userEntity.getIndustry2(), text, Color.parseColor("#ffb129"));
//        holder.product.setSpecifiedTextsColor(userEntity.getProduct(), text, Color.parseColor("#ffb129"));
        holder.industry.setText(userEntity.getIndustry1()+"-"+userEntity.getIndustry2());
        holder.product.setText(userEntity.getProduct());
        return convertView;
    }
    private class ViewHolder{
        TextUtil uname;
        TextUtil area;
        CircleImageView head;
        TextUtil industry;
        TextUtil product;
    }
}
