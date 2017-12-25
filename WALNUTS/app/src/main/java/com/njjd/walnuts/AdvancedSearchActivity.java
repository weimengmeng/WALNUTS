package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.domain.CommonEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.IconCenterEditText;
import com.njjd.utils.KeybordS;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/12/22.
 */

public class AdvancedSearchActivity extends BaseActivity {
    @BindView(R.id.et_search)
    IconCenterEditText etSearch;
    @BindView(R.id.txt_products)
    TextView txtProducts;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.txt_province)
    TextView txtProvince;
    @BindView(R.id.txt_company)
    EditText txtCompany;
    @BindView(R.id.txt_position)
    EditText txtPosition;
    //省份一级菜单
    private List<String> provinces;
    private List<CommonEntity> provinceEntities;
    //城市二级菜单
    private List<List<CommonEntity>> cityList;
    private List<List<String>> cityEntities;
    //行业一级菜单
    private List<String> industrys1;
    //行业二级菜单
    private List<List<String>> industrys2;
    private List<List<CommonEntity>> industryList2;
    private OptionsPickerView<String> provincePickview, industryPickview;
    private String cityId = "0", industryId = "0";
    @Override
    public int bindLayout() {
        return R.layout.activity_advanced_search;
    }

    @Override
    public void initView(View view) {
        setPickView();
    }

    private void setPickView() {
        provinces = CommonUtils.getInstance().getProvincesList();
        provinceEntities = CommonUtils.getInstance().getProvinceEntities();
        cityEntities = CommonUtils.getInstance().getCityEntities();
        cityList = CommonUtils.getInstance().getCityList();
        industrys1 = CommonUtils.getInstance().getIndustrys1();
        industrys2 = CommonUtils.getInstance().getIndustrys2();
        industryList2 = CommonUtils.getInstance().getIndustryList2();
        provincePickview = new OptionsPickerView.Builder(AdvancedSearchActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                cityId = cityList.get(options1).get(options2).getId();
                txtProvince.setText(provinceEntities.get(options1).getName() + "-"+cityEntities.get(options1).get(options2));
            }
        }).build();
        provincePickview.setPicker(provinces, cityEntities);
        industryPickview = new OptionsPickerView.Builder(AdvancedSearchActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                txtVocation.setText(industrys1.get(options1) + "-"+ industrys2.get(options1).get(options2));
                industryId = industryList2.get(options1).get(options2).getId();
            }
        }).build();
        industryPickview.setPicker(industrys1, industrys2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.txt_products, R.id.txt_vocation, R.id.txt_province,R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.txt_products:
                startActivityForResult(new Intent(this,ProductActivity.class).putExtra("type","2"),1);
                break;
            case R.id.txt_vocation:
                KeybordS.closeBoard(this);
                if(industryPickview!=null) {
                    industryPickview.show();
                }
                break;
            case R.id.txt_province:
                KeybordS.closeBoard(this);
                if (provincePickview != null) {
                    provincePickview.show();
                }
                break;
            case R.id.btn_search:
                Intent intent=new Intent(this,AdvancedSearchResultActivity.class);
                intent.putExtra("keyword",etSearch.getText().toString());
                intent.putExtra("city_id",cityId);
                intent.putExtra("industry_id",industryId);
                intent.putExtra("company",txtCompany.getText().toString());
                intent.putExtra("position",txtPosition.getText().toString());
                intent.putExtra("product",txtProducts.getText().toString());
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                txtProducts.setText(data.getExtras().getString("result").replace(","," "));
            }
        }
    }
}
