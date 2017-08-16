package com.njjd.walnuts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.http.HttpManager;
import com.njjd.utils.GradationScrollView;
import com.njjd.utils.MyListView;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/28.
 */

public class TagActivity extends BaseActivity implements GradationScrollView.ScrollViewListener {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.listview)
    MyListView listview;
    @BindView(R.id.scrollview)
    GradationScrollView scrollview;
    @BindView(R.id.lv_top)
    LinearLayout lvTop;
    @BindView(R.id.txt_tag)
    TextView txtTag;
    @BindView(R.id.top_view)
    RelativeLayout topView;
    @BindView(R.id.txt_focus)
    TextView txtFocus;
    private int height = 0;

    @Override
    public int bindLayout() {
        return R.layout.activity_tag;
    }

    @Override
    public void initView(View view) {
        back.setText("");
        txtTitle.setText(getIntent().getStringExtra("name"));
        initListeners();
        txtTag.setText(getIntent().getStringExtra("name"));
        txtTitle.setTextColor(Color.argb(0, 255, 255, 255));
        topView.setBackgroundColor(getResources().getColor(R.color.login));
        getTagQuestion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void getTagQuestion(){
    }
    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = lvTop.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                txtTitle.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = lvTop.getHeight();
                scrollview.setScrollViewListener(TagActivity.this);
            }
        });
    }

    /**
     * 滑动监听
     *
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (y <= 0) {   //设置标题的背景颜色
            txtTitle.setBackgroundColor(Color.argb(0, 144, 151, 166));
        } else if (y > 0 && y <= height) {
            float scale = (float) y / height;
            float alpha = (255 * scale);
            txtTitle.setTextColor(Color.argb((int) alpha, 255, 255, 255));
        } else {
        }
    }

    @OnClick({R.id.back,R.id.txt_focus})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.txt_focus:
                addFocus();
                break;
        }
    }
    private void addFocus(){
        Map<String,Object> map=new HashMap<>();
        map.put("label_id",getIntent().getStringExtra("tag_id"));
        map.put("token", SPUtils.get(this,"token","").toString());
        map.put("uid",SPUtils.get(this,"userId","").toString());
        map.put("select","1");
        SubjectPost postEntity=new SubjectPost(new ProgressSubscriber(focusListener,this,false,false),map);
        HttpManager.getInstance().focusLabel(postEntity);
    }
    HttpOnNextListener focusListener=new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            ToastUtils.showShortToast(TagActivity.this," 关注成功");
        }
    };
}
