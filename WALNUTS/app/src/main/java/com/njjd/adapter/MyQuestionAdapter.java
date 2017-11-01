package com.njjd.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njjd.domain.QuestionEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.DateUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.LogUtils;
import com.njjd.walnuts.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by mrwim on 17/8/16.
 */

public class MyQuestionAdapter extends BaseAdapter{
    private List<QuestionEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private QuestionEntity focusEntity;
    public static int CURRENT_PAGE = 1;
    public MyQuestionAdapter(List<QuestionEntity> list,Context context){
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
            convertView=inflater.inflate(R.layout.item_ques,parent,false);
            hodel.title=(TextView)convertView.findViewById(R.id.txt_title);
            hodel.content=(TextView)convertView.findViewById(R.id.txt_content);
            hodel.image=(ImageView) convertView.findViewById(R.id.img);
            hodel.date=(TextView)convertView.findViewById(R.id.txt_date);
            convertView.setTag(hodel);
        }else{
            hodel=(ViewHodel) convertView.getTag();
        }
        focusEntity=list.get(position);
        if(focusEntity.getIsVisiable().equals("1.0")){
            hodel.title.setText("问题包含违规内容,审核未通过");
            hodel.title.setTextColor(Color.RED);
        }else{
            hodel.title.setText(focusEntity.getTitle());
        }
        ParsePosition pos = new ParsePosition(0);
        hodel.date.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(focusEntity.getDateTime(), pos)));
        if ("".equals(focusEntity.getPhoto())) {
            hodel.image.setVisibility(View.GONE);
            hodel.content.setText(focusEntity.getContent());
            hodel.content.setVisibility(View.VISIBLE);
        } else {
            hodel.image.setVisibility(View.VISIBLE);
            hodel.content.setVisibility(View.GONE);
            String[] strings = focusEntity.getPhoto().split(",");
            GlideImageLoder.getInstance().displayImage(context, HttpManager.BASE_URL2+strings[0].replace("\"", ""), hodel.image);
        }
        return convertView;
    }
    private class ViewHodel{
        TextView title;
        TextView date;
        TextView content;
        ImageView image;
    }
}
