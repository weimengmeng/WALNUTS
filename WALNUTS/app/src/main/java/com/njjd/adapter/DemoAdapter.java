package com.njjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.njjd.domain.Demo;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/7/19.
 */

public class DemoAdapter extends BaseAdapter {
    private List<Demo> list;
    private Context context;
    private LayoutInflater inflater;
    public DemoAdapter(List<Demo> list,Context context){
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
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_mesg,parent,false);
        }
        return convertView;
    }
}
