package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.utils.ImmersedStatusbarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class InformActivity extends BaseActivity {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.list_mes)
    SwipeMenuListView listMes;
    @BindView(R.id.lv_back)
    SwipeBackLayout lvBack;

    @Override
    public int bindLayout() {
        return R.layout.activity_inform;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lvBack.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        ImmersedStatusbarUtils.initAfterSetContentView(this,topView);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
