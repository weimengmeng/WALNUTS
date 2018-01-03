package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.njjd.http.HttpManager;
import com.njjd.utils.IconCenterEditText;
import com.njjd.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lankton.flowlayout.FlowLayout;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by mrwim on 17/12/21.
 */

public class ProductActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.flowlayout)
    FlowLayout flowlayout;
    @BindView(R.id.et_search)
    IconCenterEditText etSearch;
    @BindView(R.id.flow_product)
    FlowLayout productLayout;
    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.txt_result)
    TextView txtResult;
    @BindView(R.id.lv_nodata)
    LinearLayout lvNodata;
    @BindView(R.id.txt_add)
    TextView txtAdd;
    private  ViewGroup.MarginLayoutParams lp;
    JsonObject o;
    @Override
    public int bindLayout() {
        return R.layout.activity_product;
    }
    @Override
    public void initView(View view) {
        txtContent.setText("没有找到相关产品服务");
        doSearch();
        txtAdd.setVisibility(View.VISIBLE);
        etSearch.setOnSearchClickListener(new IconCenterEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                //搜索数据库中已有的产品服务
                doSearch();
                if(etSearch.getText().toString().equals("")){
                    txtResult.setText("热门产品服务");
                }else{
                    txtResult.setText("搜索结果");
                }
                productLayout.removeAllViews();
                productLayout.setVisibility(View.GONE);
                lvNodata.setVisibility(View.VISIBLE);
            }
        });
        lp = new ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.setMargins(30, 40, 20, 0);
        if(getIntent().getStringExtra("type").equals("2")){
            lvNodata.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.txt_ok, R.id.txt_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.txt_add:
                addNewProduct();
                break;
            case R.id.txt_ok:
                doFinish();
                break;
        }
    }
    private void doSearch(){
        Map<String,Object> map=new HashMap<>();
        map.put("product_name",etSearch.getText().toString());
        map.put("stat",0);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                productLayout.setVisibility(View.VISIBLE);
                lvNodata.setVisibility(View.GONE);
                JsonArray array=JSONUtils.getAsJsonArray(o);
                initProduct(array);
            }
        }, this, false, false), map);
        HttpManager.getInstance().getProductOrAdd(postEntity);
    }
    private void initProduct(JsonArray array){
        for (int i=0;i<array.size();i++) {
            o=array.get(i).getAsJsonObject();
            TextView tv = new TextView(this);
            tv.setText(o.get("product_name").getAsString());
            tv.setTextColor(getResources().getColor(R.color.tag));
            tv.setTextSize(14);
            tv.setTag(o.get("id").getAsString().replace(".0",""));
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setBackgroundResource(R.drawable.round_textview);
            tv.setPadding(25, 10, 25, 10);
            tv.setOnClickListener(new MyListener());
            productLayout.addView(tv,lp);
        }
    }
    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            addSelected(((TextView)view).getText().toString(),view.getTag().toString());
            productLayout.removeView(view);
        }
    }

    //添加为新产品
    private void addNewProduct(){
        if(etSearch.getText().toString().trim().equals("")){
            ToastUtils.showShortToast(this,"请输入产品服务");
            return;
        }
        int n=flowlayout.getChildCount();
        for(int i=0;i<n;i++){
            if(((TextView)flowlayout.getChildAt(i)).getText().toString().equals(etSearch.getText().toString().trim())){
                ToastUtils.showShortToast(this,"该产品服务已经存在");
                return;
            }
        }
        if(flowlayout.getChildCount()>4){
            ToastUtils.showShortToast(this,"最多可以填写5个");
            return;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("product_name",etSearch.getText().toString());
        map.put("stat",1);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                JsonObject object= JSONUtils.getAsJsonObject(o);
                addSelected(etSearch.getText().toString(),object.get("id").getAsString().replace(".0",""));
            }
        }, this, false, false), map);
        HttpManager.getInstance().getProductOrAdd(postEntity);
    }
    private void addSelected(String s,String id){
        TextView tv = new TextView(this);
        tv.setText(s);
        tv.setTextColor(getResources().getColor(R.color.tag));
        tv.setTextSize(14);
        tv.setTag(id);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setBackgroundResource(R.drawable.round_textview);
        tv.setPadding(25, 10, 25, 10);
        tv.setOnClickListener(this);
        flowlayout.addView(tv,lp);
    }
    private void doFinish(){
        String temp="";
        String ids="";
        int n=flowlayout.getChildCount();
        for(int i=0;i<n;i++){
            if(i==(n-1)){
                temp+=((TextView)flowlayout.getChildAt(i)).getText().toString();
                ids+=flowlayout.getChildAt(i).getTag().toString();
            }else{
                temp+=((TextView)flowlayout.getChildAt(i)).getText().toString()+",";
                ids+=flowlayout.getChildAt(i).getTag().toString()+",";
            }
        }
        Intent intent=new Intent();
        intent.putExtra("result",temp);
        intent.putExtra("productIds",ids);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        flowlayout.removeView(view);
    }
}
