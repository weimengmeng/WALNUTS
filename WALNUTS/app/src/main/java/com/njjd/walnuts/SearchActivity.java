package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.njjd.utils.IconCenterEditText;
import com.njjd.utils.KeybordS;
import com.njjd.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/8/18.
 */

public class SearchActivity extends BaseActivity {
    @BindView(R.id.et_search)
    IconCenterEditText etSearch;
    @BindView(R.id.txt_cancel)
    TextView txtCancel;

    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView(View view) {
        KeybordS.openKeybord(etSearch, this);
        etSearch.setOnSearchClickListener(new IconCenterEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                etSearch.onFocusChange(etSearch,false);
                txtCancel.requestFocusFromTouch();
                ToastUtils.showShortToast(SearchActivity.this,etSearch.getText().toString());
            }
        });
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etSearch.onFocusChange(etSearch,hasFocus);
                if(hasFocus){
                    txtCancel.setVisibility(View.VISIBLE);
                }else{
                    txtCancel.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.radio1, R.id.radio2, R.id.radio3, R.id.txt_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                KeybordS.closeBoard(this);
                finish();
                break;
            case R.id.radio1:
                break;
            case R.id.radio2:
                break;
            case R.id.radio3:
                break;
            case R.id.txt_cancel:
                KeybordS.closeBoard(this);
                txtCancel.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        KeybordS.closeBoard(this);
    }
}
