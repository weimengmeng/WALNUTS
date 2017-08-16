package com.njjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njjd.domain.FocusEntity;
import com.njjd.utils.GlideImageLoder;
import com.njjd.walnuts.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/8/15.
 */

public class FocusQuesAdapter extends BaseAdapter {
    private List<FocusEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private FocusEntity focusEntity;
    public FocusQuesAdapter(List<FocusEntity> list,Context context){
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
            convertView=inflater.inflate(R.layout.item_attention_ques,parent,false);
            hodel.title=(TextView)convertView.findViewById(R.id.txt_title);
            hodel.tag=(TextView)convertView.findViewById(R.id.txt_tag);
            hodel.date=(TextView)convertView.findViewById(R.id.txt_time);
            convertView.setTag(hodel);
        }else{
            hodel=(ViewHodel) convertView.getTag();
        }
        focusEntity=list.get(position);
        hodel.title.setText(focusEntity.getName());
        hodel.tag.setText(focusEntity.getIntroduction());
        hodel.date.setText(focusEntity.getData());
        return convertView;
    }
    private class ViewHodel{
        TextView title;
        TextView tag;
        TextView date;
    }
}
