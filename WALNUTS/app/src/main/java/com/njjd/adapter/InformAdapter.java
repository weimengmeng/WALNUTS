package com.njjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njjd.domain.InformEntity;
import com.njjd.utils.DateUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.walnuts.R;

import org.json.JSONException;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/8/22.
 */

public class InformAdapter extends BaseAdapter {
    public static int CURRENT_PAGE=1;
    private List<InformEntity> list;
    private Context context;
    private InformEntity tempEntity;
    private LayoutInflater inflater;
    public InformAdapter(Context context,List<InformEntity> list){
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
            convertView=inflater.inflate(R.layout.item_inform,parent,false);
            hodel=new ViewHodel();
            hodel.image_head=(CircleImageView) convertView.findViewById(R.id.img_head);
            hodel.title=(TextView) convertView.findViewById(R.id.txt_name);
            hodel.content=(TextView) convertView.findViewById(R.id.txt_content);
            hodel.time=(TextView) convertView.findViewById(R.id.txt_time);
            convertView.setTag(hodel);
        }else{
            hodel=(ViewHodel) convertView.getTag();
        }
        tempEntity=list.get(position);
        GlideImageLoder.getInstance().displayImage(context,tempEntity.getHeadimg(),hodel.image_head);
        switch (tempEntity.getType()){
//            0 系统通知 1 关注用户 2 关注问题 3回答问题 4 评论回答 5 收藏回答 6 点赞",
            case "0.0":
                hodel.title.setText("系统通知");
                hodel.content.setText(tempEntity.getContents());
                break;
            case "1.0":
                hodel.title.setText(tempEntity.getUname()+" 关注了你");
                hodel.content.setText("恭喜你被关注啦");
                break;
            case "2.0":
                hodel.title.setText(tempEntity.getUname()+" 关注了你的问题");
                break;
            case "3.0":
                try {
                    hodel.title.setText(tempEntity.getUname()+" 等"+Float.valueOf(tempEntity.getContent().getString("answer_num")).intValue()+"人回答了你的问题");
                    hodel.content.setText(tempEntity.getContent().getString("contents"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "4.0":
                try {
                    hodel.title.setText(tempEntity.getUname()+" 等"+Float.valueOf(tempEntity.getContent().getString("answer_num")).intValue()+"人评论了你的回答");
                    hodel.content.setText(tempEntity.getContent().getString("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "5.0":
                    hodel.title.setText(tempEntity.getUname()+" 收藏了你的回答");
                try {
                    hodel.content.setText(tempEntity.getContent().getString("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "6.0":
                hodel.title.setText(tempEntity.getUname()+" 认同了你的回答");
                try {
                    hodel.content.setText(tempEntity.getContent().getString("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
        ParsePosition pos = new ParsePosition(0);
        hodel.time.setText(DateUtils.formationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tempEntity.getAdd_time(), pos)));
        return convertView;
    }
    private class ViewHodel{
        CircleImageView image_head;
        TextView title;
        TextView content;
        TextView time;
    }
}
