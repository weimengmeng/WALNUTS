package com.njjd.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njjd.domain.SearchQuesEntity;
import com.njjd.utils.TextUtil;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/12/6.
 */

public class SearchQuesAdapter extends BaseAdapter {
    public static int CURRENTPAGE=1;
    private List<SearchQuesEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private SearchQuesEntity quesEntity;
    private String text="";
    public SearchQuesAdapter(Context context,List<SearchQuesEntity> list){
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
            convertView=inflater.inflate(R.layout.item_search_ques,null);
            holder.title=(TextUtil) convertView.findViewById(R.id.search_title);
            holder.content=(TextUtil) convertView.findViewById(R.id.search_content);
            holder.answerNum=(TextView) convertView.findViewById(R.id.search_answer);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        quesEntity=list.get(position);
        holder.title.setSpecifiedTextsColor(quesEntity.getTitle(), text, Color.parseColor("#ffb129"));
        holder.content.setSpecifiedTextsColor(quesEntity.getContent(), text, Color.parseColor("#ffb129"));
        holder.answerNum.setText(Float.valueOf(quesEntity.getAnswerNum()).intValue()+" 回答");
        return convertView;
    }
    private class ViewHolder{
        TextUtil title;
        TextUtil content;
        TextView answerNum;
    }
}
