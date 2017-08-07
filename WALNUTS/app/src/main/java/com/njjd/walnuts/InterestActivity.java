package com.njjd.walnuts;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.njjd.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/8/6.
 */

public class InterestActivity extends BaseActivity {
    @BindView(R.id.btn_one)
    RadioButton btnOne;
    @BindView(R.id.btn_two)
    RadioButton btnTwo;
    @BindView(R.id.btn_three)
    RadioButton btnThree;
    @BindView(R.id.btn_four)
    RadioButton btnFour;
    @BindView(R.id.btn_five)
    RadioButton btnFive;
    @BindView(R.id.btn_six)
    RadioButton btnSix;
    @BindView(R.id.btn_seven)
    RadioButton btnSeven;
    @BindView(R.id.btn_eight)
    RadioButton btnEight;
    @BindView(R.id.btn_skip)
    Button btnSkip;

    @Override
    public int bindLayout() {
        return R.layout.activity_interest;
    }

    @Override
    public void initView(View view) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.btn_one, R.id.btn_two, R.id.btn_three, R.id.btn_four, R.id.btn_five, R.id.btn_six, R.id.btn_seven, R.id.btn_eight, R.id.btn_skip})
    public void onViewClicked(View view) {
        if(view instanceof RadioButton){
            if(view.getTag().toString().equals("0")){
                ((RadioButton) view).setChecked(true);
                view.setTag("1");
            }else{
                ((RadioButton) view).setChecked(false);
                view.setTag("0");
            }
        }
        switch (view.getId()) {
            case R.id.btn_one:
                break;
            case R.id.btn_two:
                break;
            case R.id.btn_three:
                break;
            case R.id.btn_four:
                break;
            case R.id.btn_five:
                break;
            case R.id.btn_six:
                break;
            case R.id.btn_seven:
                break;
            case R.id.btn_eight:
                break;
            case R.id.btn_skip:
                break;
        }
    }
}
