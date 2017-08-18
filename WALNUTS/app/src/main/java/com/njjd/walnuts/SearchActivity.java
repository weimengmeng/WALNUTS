package com.njjd.walnuts;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mrwim on 17/8/18.
 */

public class SearchActivity extends BaseActivity{
    @BindView(R.id.list)
    ListView list;
    private List<String> lastSearches;
    private DrawerLayout drawer;
    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView(View view) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
