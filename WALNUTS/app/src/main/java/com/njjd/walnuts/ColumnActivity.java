package com.njjd.walnuts;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.njjd.adapter.ColumnAdapter;
import com.njjd.domain.ColumnEntity;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.MyListView;
import com.njjd.utils.ObservableScrollView;
import com.njjd.utils.ToastUtils;
import com.njjd.utils.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    private ColumnAdapter adapter;
    private List<ColumnEntity> list=new ArrayList<>();
    @Override
    public int bindLayout() {
        return R.layout.activity_column;
    }

    @Override
    public void initView(View view) {
        initListeners();
        txtTitle.setTextColor(Color.argb(0, 255, 255, 255));
        ImmersedStatusbarUtils.initAfterSetContentView(this, reTop);
        list.add(new ColumnEntity("1","http://p.3761.com/pic/231432169575.jpg","核桃小编","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"));
        list.add(new ColumnEntity("1","http://img3.imgtn.bdimg.com/it/u=3553261757,602330486&fm=214&gp=0.jpg","核桃小编","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"));
        adapter=new ColumnAdapter(this,list);
        listColumn.setAdapter(adapter);
        scrollView.smoothScrollTo(0, 0);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        list.add(new ColumnEntity("1","http://p.3761.com/pic/231432169575.jpg","核桃小编3","超级大美女","作为一名销售人员，已经从事6年了，但还是不能喝酒，其他同事都说我不能干这一行？","http://up.qqjia.com/z/16/tu17317_45.png"));
                        list.add(new ColumnEntity("1","http://img3.imgtn.bdimg.com/it/u=3553261757,602330486&fm=214&gp=0.jpg","核桃小编4","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"));
                        list.add(new ColumnEntity("1","http://p.3761.com/pic/231432169575.jpg","核桃小编5","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"));
                        list.add(new ColumnEntity("1","http://img3.imgtn.bdimg.com/it/u=3553261757,602330486&fm=214&gp=0.jpg","核桃小编6","超级大美女","作为一名销售人员，已经从事6年了，但还是不能喝酒，其他同事都说我不能干这一行？","http://up.qqjia.com/z/16/tu17317_45.png"));
                        list.add(new ColumnEntity("1","http://p.3761.com/pic/231432169575.jpg","核桃小编7","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"));
                        list.add(new ColumnEntity("1","http://img3.imgtn.bdimg.com/it/u=3553261757,602330486&fm=214&gp=0.jpg","核桃小编8","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"));
                        list.add(new ColumnEntity("1","http://p.3761.com/pic/231432169575.jpg","核桃小编9","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"));
                        list.add(new ColumnEntity("1","http://img3.imgtn.bdimg.com/it/u=3553261757,602330486&fm=214&gp=0.jpg","核桃小编10","超级大美女","作为一名销售人员，已经从事6年了，但还是不能喝酒，其他同事都说我不能干这一行？","http://up.qqjia.com/z/16/tu17317_45.png"));
                        adapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                    }
                },2000);
            }
        });
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

    @OnClick({R.id.back, R.id.btn_add_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_add_help:
                ToastUtils.showShortToast(this, "分享");
                break;
        }
    }

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
