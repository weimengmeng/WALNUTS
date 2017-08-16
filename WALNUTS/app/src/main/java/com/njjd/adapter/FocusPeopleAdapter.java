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

public class FocusPeopleAdapter extends BaseAdapter {
    private List<FocusEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private FocusEntity focusEntity;
    public static int CURRENT_PAGE = 1;
    public FocusPeopleAdapter(List<FocusEntity> list,Context context){
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
            convertView=inflater.inflate(R.layout.item_attention_people,parent,false);
            hodel.head=(CircleImageView)convertView.findViewById(R.id.img_head);
            hodel.uname=(TextView)convertView.findViewById(R.id.txt_name);
            hodel.introduction=(TextView)convertView.findViewById(R.id.txt_intro);
            convertView.setTag(hodel);
        }else{
            hodel=(ViewHodel) convertView.getTag();
        }
        focusEntity=list.get(position);
        hodel.uname.setText(focusEntity.getName());
        hodel.introduction.setText(focusEntity.getIntroduction().equals("")?"TA好神秘，签名都没有":focusEntity.getIntroduction());
        GlideImageLoder.getInstance().displayImage(context, focusEntity.getHead(), hodel.head);
        return convertView;
    }
    private class ViewHodel{
        CircleImageView head;
        TextView uname;
        TextView introduction;
    }
}
