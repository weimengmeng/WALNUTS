package com.njjd.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njjd.db.DBHelper;
import com.njjd.domain.SearchHistory;
import com.njjd.walnuts.R;

import java.util.List;

/**
 * Created by mrwim on 17/12/5.
 */

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<SearchHistory> list;
    private LayoutInflater inflater;
    private SearchHistory history;
    public HistoryAdapter(Context context,List<SearchHistory> list){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodel hodel=null;
        if(convertView==null){
            hodel=new ViewHodel();
            convertView=inflater.inflate(R.layout.item_history,parent,false);
            hodel.text=convertView.findViewById(R.id.item_text);
            hodel.delete= convertView.findViewById(R.id.item_delete);
            convertView.setTag(hodel);
        }else{
            hodel=(ViewHodel) convertView.getTag();
        }
        history=list.get(position);
        hodel.text.setText(history.getKeywords());
        hodel.delete.setOnClickListener(new DeleteListener(position));
        return convertView;
    }
    private class ViewHodel {
        TextView text;
        ImageView delete;
    }
    private class DeleteListener implements View.OnClickListener{
        int position;
        public DeleteListener(int position){
         this.position=position;
        }
        @Override
        public void onClick(View v) {
            DBHelper.getInstance().getmDaoSession().getSearchHistoryDao().delete(list.get(position));
            list.remove(position);
            notifyDataSetChanged();
        }
    }
}
