package com.njjd.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.njjd.domain.QuestionEntity;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.LogUtils;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/7/26.
 */

public class IndexQuestionAdapter extends BaseAdapter {
    private List<QuestionEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private int index = 0;
    private QuestionEntity temp;
    private ImageView head;
    private Typeface typeface;
    public IndexQuestionAdapter(Context context, List<QuestionEntity> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansHans-Medium.ttf");
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
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_index, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.txt_title);
            viewHolder.title.setTypeface(typeface);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.img);
            viewHolder.lvHead = (LinearLayout) convertView
                    .findViewById(R.id.lv_head);
            viewHolder.total = (TextView) convertView.findViewById(R.id.txt_total);
            viewHolder.createTime = (TextView) convertView
                    .findViewById(R.id.txt_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        temp = list.get(position);
        viewHolder.title.setText(temp.getTitle());
        if ("".equals(temp.getPhoto())) {
            viewHolder.imageView.setVisibility(View.GONE);
        } else {
            String[] imgs=temp.getPhoto().split(",");
            GlideImageLoder.getInstance().displayImage(context,imgs[0],viewHolder.imageView);
            viewHolder.imageView.setVisibility(View.VISIBLE);
        }
        viewHolder.total.setText("等  " + (Integer.valueOf(temp.getAnswerNum()) + Integer.valueOf(temp.getFocusNum())) + "  人参与");
        viewHolder.createTime.setText(temp.getDateTime());
        String[] strs = temp.getPic().split(",");
        if ("".equals(temp.getPic())) {
            viewHolder.lvHead.removeAllViews();
        } else {
            viewHolder.lvHead.removeAllViews();
            viewHolder.lvHead.setVisibility(View.VISIBLE);
            for (int i = 0; i < strs.length && i < 3; i++) {
                head = (ImageView) inflater.inflate(R.layout.layout_head, null);
                head.setId(i);
                head.setLayoutParams(new ViewGroup.LayoutParams(90,90));
                GlideImageLoder.getInstance().displayImage(context,strs[i],head);
                viewHolder.lvHead.addView(head);
                head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShortToast(context, "头像" + v.getId());
                    }
                });
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        ImageView imageView;
        LinearLayout lvHead;
        TextView total;
        TextView createTime;
    }
}
