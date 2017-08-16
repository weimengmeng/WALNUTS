package com.njjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njjd.domain.FocusEntity;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/8/15.
 */

public class FocusTagAdapter extends BaseAdapter{
    private List<FocusEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private FocusEntity focusEntity;
    public static int CURRENT_PAGE = 1;
    public FocusTagAdapter(List<FocusEntity> list,Context context){
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
    public static int getCurrentPage() {
        return CURRENT_PAGE;
    }

    public static void setCurrentPage(int currentPage) {
        CURRENT_PAGE = currentPage;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHodel hodel=null;
        if(convertView==null){
            hodel=new ViewHodel();
            convertView=inflater.inflate(R.layout.item_attention_tag,parent,false);
            hodel.uname=(TextView)convertView.findViewById(R.id.txt_name);
            convertView.setTag(hodel);
        }else{
            hodel=(ViewHodel) convertView.getTag();
        }
        focusEntity=list.get(position);
        hodel.uname.setText(focusEntity.getName());
        return convertView;
    }
    private class ViewHodel{
        TextView uname;
    }
}
