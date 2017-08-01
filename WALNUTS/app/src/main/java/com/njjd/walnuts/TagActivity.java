package com.njjd.walnuts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.njjd.utils.GradationScrollView;
import com.njjd.utils.MyListView;

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
    private int height = 0;

    @Override
    public int bindLayout() {
        return R.layout.activity_tag;
    }

    @Override
    public void initView(View view) {
        back.setText("");
        txtTitle.setText("商务谈判");
        initListeners();
        txtTitle.setTextColor(Color.argb(0, 255, 255, 255));
        topView.setBackgroundColor(getResources().getColor(R.color.login));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
