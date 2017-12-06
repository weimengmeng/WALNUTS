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
import com.njjd.domain.SearchQuesEntity;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.TextUtil;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/12/6.
 */

public class SearchArticleAdapter extends BaseAdapter {
    public static int CURRENTPAGE=1;
    private List<SearchArticleEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private SearchArticleEntity articleEntity;
    private String text="";
    public SearchArticleAdapter(Context context,List<SearchArticleEntity> list){
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
            convertView=inflater.inflate(R.layout.item_search_article,null);
            holder.title=(TextUtil) convertView.findViewById(R.id.search_title);
            holder.content=(TextUtil) convertView.findViewById(R.id.search_content);
            holder.img=(ImageView) convertView.findViewById(R.id.item_img);
            holder.answerNum=(TextView) convertView.findViewById(R.id.search_answer);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        articleEntity=list.get(position);
        holder.title.setSpecifiedTextsColor(articleEntity.getTitle(), text, Color.parseColor("#ffb129"));
        holder.content.setSpecifiedTextsColor(articleEntity.getDesc(), text, Color.parseColor("#ffb129"));
        GlideImageLoder.getInstance().displayImage(context,articleEntity.getImg(),holder.img);
        holder.answerNum.setText(articleEntity.getUname()+" • "+articleEntity.getColumn_name());
        return convertView;
    }
    private class ViewHolder{
        TextUtil title;
        TextUtil content;
        ImageView img;
        TextView answerNum;
    }
}
