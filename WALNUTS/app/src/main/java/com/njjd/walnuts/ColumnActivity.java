package com.njjd.walnuts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.njjd.adapter.ColumnArticleAdapter;
import com.njjd.adapter.FindAnswerAdapter;
import com.njjd.db.DBHelper;
import com.njjd.domain.ColumnArticleEntity;
import com.njjd.domain.ColumnEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.MyListView;
import com.njjd.utils.ObservableScrollView;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.njjd.utils.VpSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/10/17.
 */

public class ColumnActivity extends BaseActivity implements ObservableScrollView.ScrollViewListener {
    @BindView(R.id.lv_header)
    RelativeLayout lvHeader;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.re_top)
    LinearLayout reTop;
    @BindView(R.id.scrollview)
    ObservableScrollView scrollView;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.refresh)
    VpSwipeRefreshLayout refresh;
    @BindView(R.id.list_column)
    MyListView listColumn;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.txt_intro)
    TextView txtIntro;
    @BindView(R.id.txt_focusNum)
    TextView txtFocusNum;
    @BindView(R.id.txt_focus)
    TextView txtFocus;
    @BindView(R.id.img_bg)
    ImageView imgBg;
    private ColumnArticleAdapter adapter;
    private List<ColumnArticleEntity> list = new ArrayList<>();
    private ColumnEntity entity;

    @Override
    public int bindLayout() {
        return R.layout.activity_column;
    }

    @Override
    public void initView(View view) {
        initListeners();
        adapter = new ColumnArticleAdapter(this, list);
        getColumnDetail();
        getColumnArticles();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置头部控件ViewGroup的PaddingTop,防止界面与状态栏重叠
            int statusBarHeight = ImmersedStatusbarUtils.getStatusBarHeight(this);
            LogUtils.d("huan"+statusBarHeight);
            findViewById(R.id.re_top).setPadding(0, statusBarHeight, 0,0);
        }
        txtTitle.setTextColor(Color.argb(0, 255, 255, 255));
        listColumn.setAdapter(adapter);
        scrollView.smoothScrollTo(0, 0);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setCurrentPage(1);
                getColumnArticles();
            }
        });
        listColumn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ColumnActivity.this, ColumnDetailActivity.class);
                intent.putExtra("article_id",Float.valueOf(list.get(position).getArticle_id()).intValue()+"");
                startActivity(intent);
            }
        });
    }

    private void getColumnDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("column_id", getIntent().getStringExtra("column_id"));
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", ""));
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(getColumnDetail, this, false, false), map);
        HttpManager.getInstance().getColumnDetail(postEntity);
    }

    HttpOnNextListener getColumnDetail = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls(); //重点
            Gson gson = gsonBuilder.create();
            try {
                JSONObject object = new JSONObject(gson.toJson(o));
                entity = new ColumnEntity(object);
                txtIntro.setText(entity.getDesc());
                txtTitle.setText(entity.getName());
                txtName.setText(entity.getName());
                txtFocusNum.setText(Float.valueOf(entity.getFollow_num()).intValue() + "  人关注");
                GlideImageLoder.getInstance().displayImage(ColumnActivity.this,entity.getPic(),imgBg);
                GlideImageLoder.getInstance().displayImage(ColumnActivity.this,entity.getUhead(),imgHead);
                if (entity.getIs_follow().equals("1.0")) {
                   txtFocus.setText("取消关注");
                    txtFocus.setTextColor(getResources().getColor(R.color.txt_color));
                    txtFocus.setBackground(getResources().getDrawable(R.drawable.background_button_div_grey));
                } else {
                    txtFocus.setText("+ 关注");
                    txtFocus.setTextColor(getResources().getColor(R.color.white));
                    txtFocus.setBackground(getResources().getDrawable(R.drawable.background_button_div));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void getColumnArticles() {
        Map<String, Object> map = new HashMap<>();
        map.put("column_id",getIntent().getStringExtra("column_id"));
        map.put("page", adapter.getCurrentPage());
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", ""));
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, false, false), map);
        HttpManager.getInstance().getColumnArticles(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        try {
            JSONArray array = new JSONArray(gson.toJson(o));
            ColumnArticleEntity entity;
            if (adapter.getCurrentPage() == 1) {
                list.clear();
                refresh.setRefreshing(false);
            }
            for (int i = 0; i < array.length(); i++) {
                entity = new ColumnArticleEntity(array.getJSONObject(i));
                list.add(entity);
            }
            adapter.notifyDataSetChanged();
            if(array.length()==0||array.length()<10){
                findViewById(R.id.nomore).setVisibility(View.VISIBLE);
                return;
            }else{
                findViewById(R.id.nomore).setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        ViewTreeObserver vto = lvHeader.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                lvHeader.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                scrollView.setScrollViewListener(ColumnActivity.this);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.btn_add_help,R.id.txt_focus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.txt_focus:
                addFocus();
                break;
            case R.id.btn_add_help:
                ToastUtils.showShortToast(this, "分享");
                break;
        }
    }
    private void addFocus() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", ""));
        map.put("column_id", Float.valueOf(entity.getId()).intValue());
        map.put("select", entity.getIs_follow().equals("1.0")? 0 : 1);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(focusListener, this, true, false), map);
        HttpManager.getInstance().followColumn(postEntity);
    }

    HttpOnNextListener focusListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            if(entity.getIs_follow().equals("0.0")){
                ToastUtils.showShortToast(ColumnActivity.this,"关注成功");
            }
            if (entity.getIs_follow().equals("0.0")) {
                txtFocus.setText("取消关注");
                txtFocus.setTextColor(getResources().getColor(R.color.txt_color));
                txtFocus.setBackgroundResource(R.drawable.txt_shape);
                entity.setIs_follow("1.0");
                entity.setFollow_num((Float.valueOf(entity.getFollow_num()).intValue()+1)+"");
            } else {
                txtFocus.setText("+ 关注");
                txtFocus.setTextColor(getResources().getColor(R.color.white));
                txtFocus.setBackgroundResource(R.drawable.txt_shape_login);
                entity.setIs_follow("0.0");
                entity.setFollow_num((Float.valueOf(entity.getFollow_num()).intValue()-1)+"");
            }
            txtFocusNum.setText(Float.valueOf(entity.getFollow_num()).intValue()+" 人关注");
        }
    };


    @Override
    public void onScrollChanged(final ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            reTop.setBackgroundColor(Color.argb(0, 255, 177, 41));//AGB由相关工具获得，或者美工提供
            txtTitle.setTextColor(Color.argb(0, 255, 255, 255));
            txtName.setTextColor(Color.argb(255, 255, 255, 255));
        } else if (y > 0 && y <= 400) {
            float scale = (float) y / 400;
            float alpha = (255 * scale);
            reTop.setBackgroundColor(Color.argb((int) alpha, 255, 177, 41));
            txtTitle.setTextColor(Color.argb((int) alpha, 255, 255, 255));
            txtName.setTextColor(Color.argb((255 - (int) alpha) / 100, 255, 255, 255));
        } else {
            reTop.setBackgroundColor(Color.argb(255, 255, 177, 41));
            txtTitle.setTextColor(Color.argb(255, 255, 255, 255));
            txtName.setTextColor(Color.argb(0, 255, 255, 255));
        }
    }

}
