package com.njjd.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.njjd.adapter.FindAnswerAdapter;
import com.njjd.application.ConstantsVal;
import com.njjd.domain.ColumnEntity;
import com.njjd.domain.SelectedAnswerEntity;
import com.njjd.domain.SpecialEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.MyXRecyclerView;
import com.njjd.utils.SPUtils;
import com.njjd.walnuts.ColumnDetailActivity;
import com.njjd.walnuts.R;
import com.njjd.walnuts.SelectAnswerDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mrwim on 17/9/14.
 */

public class FindFragment2 extends BaseFragment implements HttpOnNextListener{
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.list_find)
    MyXRecyclerView listFind;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.top)
    LinearLayout top;
    private Context context;
    private List<SpecialEntity> specialEntities=new ArrayList<>();
    private FindAnswerAdapter adapter;
    private List<ColumnEntity> columnEntities=new ArrayList<>();
    private List<SelectedAnswerEntity> selectedAnswerEntities=new ArrayList<>();
    private SpecialEntity entity;
    private MyReceiver receiver;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_find2, container, false);
        ButterKnife.bind(this, view);
        ImmersedStatusbarUtils.initAfterSetContentView(getActivity(),top);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantsVal.REFRESH_FIND);
        context.registerReceiver(receiver, filter);
        back.setVisibility(View.GONE);
        txtTitle.setText("精选回答");
        adapter=new FindAnswerAdapter(context,specialEntities,columnEntities);
//        specialEntities.add(new SpecialEntity(new ColumnEntity("1","http://p.3761.com/pic/231432169575.jpg","核桃小编","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"),null));
//        specialEntities.add(new SpecialEntity(new ColumnEntity("1","http://img3.imgtn.bdimg.com/it/u=3553261757,602330486&fm=214&gp=0.jpg","核桃小编","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"),null));
//        specialEntities.add(new SpecialEntity(null,new SelectedAnswerEntity("1","1","1","http://up.qqjia.com/z/16/tu17317_45.png","丽丽","超级大美女","我被客户说服了怎么办？","0","http://up.qqjia.com/z/16/tu17317_45.png","想办法改变自己的说话方式，让客户相信你")));
//        specialEntities.add(new SpecialEntity(null,new SelectedAnswerEntity("1","1","1","http://img2.touxiang.cn/file/20160310/0bf65797064bd8990e2438664347c3de.jpg","小美","超级大美女","我被客户说服了怎么办？","1","","想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你")));
//        specialEntities.add(new SpecialEntity(null,new SelectedAnswerEntity("1","1","1","http://www.qqpk.cn/Article/UploadFiles/201011/20101128035132850.jpg","丽丽","超级大美女","我被客户说服了怎么办？","0","http://up.qqjia.com/z/16/tu17317_45.png","想办法改变自己的说话方式，让客户相信你")));
//        specialEntities.add(new SpecialEntity(null,new SelectedAnswerEntity("1","1","1","http://www.qq745.com/uploads/allimg/141015/1-1410150T344.jpg","丽丽","超级大美女","我被客户说服了怎么办？","1","","想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你")));
        listFind.setLayoutManager(new LinearLayoutManager(context));
        listFind.setAdapter(adapter);
        listFind.setEmptyView(view.findViewById(R.id.empty));
        listFind.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        listFind.setRefreshProgressStyle(ProgressStyle.BallPulse);
        adapter.setOnItemClickListener(new FindAnswerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent;
                if(specialEntities.get(position).getColumnEntity()!=null){
                    intent=new Intent(context, ColumnDetailActivity.class);
                    startActivity(intent);
                }else{
                    entity=specialEntities.get(position);
                    intent=new Intent(context, SelectAnswerDetailActivity.class);
                    intent.putExtra("questionId",entity.getAnswerEntity().getArticle_id());
                    intent.putExtra("questionTitle",entity.getAnswerEntity().getTitle());
                    intent.putExtra("answer_id",entity.getAnswerEntity().getAnswer_id());
                    startActivity(intent);
                }
            }
        });
        getSelectedAnswerList();
        listFind.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                adapter.setCurrentPage(1);
                getSelectedAnswerList();
            }

            @Override
            public void onLoadMore() {
                adapter.setCurrentPage(adapter.getCurrentPage()+1);
                getSelectedAnswerList();
            }
        });
    }
    private void getSelectedAnswerList(){
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(context, "userId", "").toString());
        map.put("token", SPUtils.get(context, "token", "").toString());
        map.put("page", adapter.getCurrentPage());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, context, false, false), map);
        HttpManager.getInstance().getHotComment(postEntity);
    }

    @Override
    public void onNext(Object o) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        JSONObject object = null;
        JSONArray array = null;
        SpecialEntity entity;
        SelectedAnswerEntity answerEntity;
        try {
            object=new JSONObject(gson.toJson(o));
            array=object.getJSONArray("comment");
            if(adapter.getCurrentPage()==1){
                specialEntities.clear();
                listFind.refreshComplete();
            }else{
                listFind.loadMoreComplete();
            }
            if(array.length()<20){
                listFind.setNoMore(true);
            }
            for(int i=0;i<array.length();i++){
                object=array.getJSONObject(i);
                answerEntity=new SelectedAnswerEntity(object);
                entity=new SpecialEntity(null,answerEntity);
                specialEntities.add(entity);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
                listFind.smoothScrollToPosition(0);
                listFind.setPullRefreshEnabled(true);
                listFind.refresh();
                adapter.setCurrentPage(1);
            getSelectedAnswerList();
        }
    }
    @Override
    public void lazyInitData() {

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        context.unregisterReceiver(receiver);
    }
}
