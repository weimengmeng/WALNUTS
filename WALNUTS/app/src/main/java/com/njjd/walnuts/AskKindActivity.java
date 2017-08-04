package com.njjd.walnuts;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lankton.flowlayout.FlowLayout;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by mrwim on 17/7/21.
 */

public class AskKindActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.flowlayout)
    FlowLayout flowlayout;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.lv_back)
    SwipeBackLayout lvBack;
    private Bundle bundle;
    @Override
    public int bindLayout() {
        return R.layout.activity_askkind;
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void initView(View view) {
        back.setText("提问");
        txtTitle.setText("标签");
        bundle=getIntent().getBundleExtra("question");
        getFlags();
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.setMargins(25, 0, 15, 0);
        for (int i = 0; i < 10; i++) {
            TextView tv = new TextView(this);
            tv.setText("社会" + i);
            tv.setTextColor(getResources().getColor(R.color.login));
            tv.setTextSize(12);
            tv.setTag(i + "");
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setBackgroundResource(R.drawable.round_textview);
            tv.setOnClickListener(this);
            flowlayout.addView(tv, lp);
        }
        lvBack.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }
    private void getFlags(){
        Map<String,String> map=new HashMap<>();

    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_submit:
                ToastUtils.showShortToast(this, "发布问题");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        for(int i=0;i<flowlayout.getChildCount();i++){
            flowlayout.getChildAt(i).setBackgroundResource(R.drawable.round_textview);
        }
        v.setBackgroundResource(R.drawable.round_textview1);
        ToastUtils.showShortToast(AskKindActivity.this, v.getTag().toString());
    }
}
