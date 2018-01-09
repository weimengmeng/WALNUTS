package com.njjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.czy1121.view.CornerLabelView;
import com.njjd.domain.ColumnArticleEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.GlideImageLoder;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/10/23.
 */

public class RecommendArticleAdapter extends BaseAdapter {
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

    public RecommendArticleAdapter(Context context, List<ColumnArticleEntity> list){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodel hodel=null;
        if(convertView==null){
            hodel=new ViewHodel();
            convertView=inflater.inflate(R.layout.layout_column2,parent,false);
            hodel.head = convertView
                    .findViewById(R.id.img_head);
            hodel.name =  convertView
                    .findViewById(R.id.txt_name);
            hodel.content =convertView
                    .findViewById(R.id.txt_content);
            hodel.title =  convertView.findViewById(R.id.txt_title);
            hodel.pic =  convertView
                    .findViewById(R.id.img);
            hodel.label_select =  convertView
                    .findViewById(R.id.label_select);
            convertView.setTag(hodel);
        }else{
            hodel=(ViewHodel)convertView.getTag();
        }
        entity=list.get(position);
        if(entity.getIs_select().equals("1.0")){
            hodel.label_select.findViewById(R.id.label_select).setVisibility(View.VISIBLE);
        }else{
            hodel.label_select.findViewById(R.id.label_select).setVisibility(View.GONE);
        }
        GlideImageLoder.getInstance().displayImage(context, entity.getHead(), hodel.head);
        hodel.name.setText(entity.getName());
        hodel.title.setText(entity.getTitle());
        hodel.content.setText(entity.getDesci());
        GlideImageLoder.getInstance().displayImage(context, HttpManager.BASE_URL2+entity.getPic().split(",")[0].replace("\"",""), hodel.pic);
//        hodel.pic.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        hodel.pic.loadUrl(HttpManager.BASE_URL2+entity.getPic().split(",")[0].replace("\"",""));
        return convertView;
    }
    private class ViewHodel{
        ImageView head;
        TextView title;
        TextView content;
        TextView name;
        ImageView pic;
        CornerLabelView label_select;
    }
}
