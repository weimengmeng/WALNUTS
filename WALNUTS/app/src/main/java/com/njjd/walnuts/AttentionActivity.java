package com.njjd.walnuts;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.MyActivityManager;
import com.njjd.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class AttentionActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.list_mes)
    SwipeMenuListView listMes;
    @BindView(R.id.top)
    LinearLayout topView;
    private SwipeMenuCreator creator;

    @Override
    public int bindLayout() {
        return R.layout.activity_attention;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView(this,topView);
        back.setText("关注");
        txtTitle.setText("我的关注");
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem messItem = new SwipeMenuItem(
                        getApplicationContext());
                messItem.setBackground(new ColorDrawable(Color.RED));
                messItem.setWidth(90);
                messItem.setTitle("私信");
                messItem.setTitleSize(18);
                messItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(messItem);

                SwipeMenuItem focusItem = new SwipeMenuItem(
                        getApplicationContext());
                focusItem.setBackground(new ColorDrawable(Color.YELLOW));
                focusItem.setWidth(90);
                messItem.setTitle("取消关注");
                messItem.setTitleSize(18);
                messItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(focusItem);
            }
        };
        listMes.setMenuCreator(creator);
        listMes.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listMes.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ToastUtils.showShortToast(AttentionActivity.this, "私信他");
                        break;
                    case 1:
                        ToastUtils.showShortToast(AttentionActivity.this, "关注他");
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
