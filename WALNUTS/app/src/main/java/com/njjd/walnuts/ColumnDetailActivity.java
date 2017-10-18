package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.njjd.adapter.ColumnAdapter;
import com.njjd.domain.ColumnEntity;
import com.njjd.utils.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/9/15.
 */
public class ColumnDetailActivity extends BaseActivity {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_add_help)
    TextView btnAddHelp;
    @BindView(R.id.lv_root)
    ScrollView root;
    @BindView(R.id.list_select)
    ListViewForScrollView listSelect;
    private ColumnAdapter adapter;
    private List<ColumnEntity> entities=new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_column_detail;
    }

    @Override
    public void initView(View view) {
        txtTitle.setText("专栏");
        btnAddHelp.setVisibility(View.VISIBLE);
        entities.add(new ColumnEntity("1","http://p.3761.com/pic/231432169575.jpg","核桃小编","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"));
        entities.add(new ColumnEntity("1","http://img3.imgtn.bdimg.com/it/u=3553261757,602330486&fm=214&gp=0.jpg","核桃小编","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"));
        adapter=new ColumnAdapter(this,entities);
        listSelect.setAdapter(adapter);
        root.smoothScrollTo(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back,R.id.txt_name})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.txt_name:
                startActivity(new Intent(this,ColumnActivity.class));
                break;
        }
    }
}
