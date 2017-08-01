package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.MyActivityManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author MrWim
 * @deprecated Created  on 17/7/12.
 */
public class MainActivity extends FragmentActivity {

    private FragmentManager fm;
    private Fragment indexFragment,findFragment,messFragment,mineFragment,pubFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        MyActivityManager.getInstance().pushOneActivity(this);
    }
    private void initView(){
        fm = getSupportFragmentManager();
        indexFragment = fm.findFragmentById(R.id.index_fragment);
        findFragment = fm.findFragmentById(R.id.find_fragment);
        messFragment = fm.findFragmentById(R.id.mess_fragment);
        pubFragment = fm.findFragmentById(R.id.pub_fragment);
        mineFragment = fm.findFragmentById(R.id.my_fragment);
        fm.beginTransaction().hide(findFragment).hide(messFragment).hide(mineFragment).hide(pubFragment)
                .show(indexFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyActivityManager.getInstance().popOneActivity(this);
    }

    @OnClick({R.id.radio1, R.id.radio2, R.id.radio3, R.id.radio4, R.id.radio5})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.radio1:
                fm.beginTransaction().hide(findFragment).hide(messFragment).hide(mineFragment).hide(pubFragment)
                        .show(indexFragment)
                        .commitAllowingStateLoss();
                break;
            case R.id.radio2:
                fm.beginTransaction().hide(indexFragment).hide(messFragment).hide(mineFragment).hide(pubFragment)
                        .show(findFragment)
                        .commitAllowingStateLoss();
                break;
            case R.id.radio3:
                intent=new Intent(this,AskActivity.class);
                startActivity(intent);
                break;
            case R.id.radio4:
                fm.beginTransaction().hide(findFragment).hide(indexFragment).hide(mineFragment).hide(pubFragment)
                        .show(messFragment)
                        .commitAllowingStateLoss();
                break;
            case R.id.radio5:
                fm.beginTransaction().hide(findFragment).hide(messFragment).hide(indexFragment).hide(pubFragment)
                        .show(mineFragment)
                        .commitAllowingStateLoss();
                break;
        }
    }
}
