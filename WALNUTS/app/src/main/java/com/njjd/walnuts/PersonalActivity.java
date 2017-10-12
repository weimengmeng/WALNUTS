package com.njjd.walnuts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.njjd.domain.CommonEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/13.
 */

public class PersonalActivity extends BaseActivity{
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.txt_name)
    EditText txtName;
    @BindView(R.id.txt_sex)
    TextView txtSex;
    @BindView(R.id.txt_message)
    EditText txtMessage;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.txt_province)
    TextView txtProvince;
    @BindView(R.id.txt_position)
    EditText txtPosition;
    @BindView(R.id.txt_company)
    EditText txtCompany;
    @BindView(R.id.btn_add_help2)
    TextView btnAddHelp;
    @BindView(R.id.txt_sale)
    TextView txtSale;
    //省份一级菜单
    private List<String> provinces;
    private List<CommonEntity> provinceEntities;
    //城市二级菜单
    private List<List<CommonEntity>> cityList;
    private List<List<String>> cityEntities;
    //行业一级菜单
    private List<String> industrys1;
    private List<CommonEntity> industryList1;
    //行业二级菜单
    private List<List<String>> industrys2;
    private List<List<CommonEntity>> industryList2;
    //销售模式一级菜单
    private List<String> sales;
    private List<CommonEntity> saleList;
    @Override
    public int bindLayout() {
        return R.layout.activity_personal;
    }
    private OptionsPickerView<String> provincePickview, salePickview, industryPickview, sexPickview;
    private String provinceId = "", cityId = "", industryId = "", modelId = "";
    private String provinceName="",cityName="";
    @Override
    public void initView(View view) {
        back.setText("我的");
        txtTitle.setText("个人资料");
        txtName.setText(SPUtils.get(this,"name","").toString());
        txtName.setSelection(txtName.length());
        txtMessage.setText(SPUtils.get(this,"message","").toString());
        txtPosition.setText(SPUtils.get(this,"position","").toString());
        txtProvince.setText(SPUtils.get(this,"province","").toString()+" "+SPUtils.get(this,"city","").toString());
        txtVocation.setText(SPUtils.get(this,"industry","").toString());
        if(SPUtils.get(this,"sex","0").toString().equals("0.0")){
            txtSex.setText("女");
        }else{
            txtSex.setText("男");
        }
        txtCompany.setText(SPUtils.get(this,"company","").toString());
        if(txtMessage.getText().toString().equals("")){
            txtMessage.setHint("完善自我介绍");
        }
        if(txtPosition.getText().toString().equals("")){
            txtPosition.setHint("完善具体工作职位");
        }
        if(txtCompany.getText().toString().equals("")){
            txtCompany.setHint("完善公司详细名称");
        }
        txtSale.setText(SPUtils.get(this,"sales","").toString());
        provinceName=SPUtils.get(this,"province","").toString();
        cityName=SPUtils.get(this,"city","").toString();
        setPickView();
    }

    private void setPickView() {
        provinces = CommonUtils.getInstance().getProvincesList();
        provinceEntities = CommonUtils.getInstance().getProvinceEntities();
        cityEntities = CommonUtils.getInstance().getCityEntities();
        cityList = CommonUtils.getInstance().getCityList();
        industrys1 = CommonUtils.getInstance().getIndustrys1();
        industrys2 = CommonUtils.getInstance().getIndustrys2();
        industryList1 = CommonUtils.getInstance().getIndustryList1();
        industryList2 = CommonUtils.getInstance().getIndustryList2();
        sales = CommonUtils.getInstance().getSales();
        saleList = CommonUtils.getInstance().getSaleList();
        provincePickview = new OptionsPickerView.Builder(PersonalActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                provinceId = provinceEntities.get(options1).getId();
                cityId = cityList.get(options1).get(options2).getId();
                provinceName=provinceEntities.get(options1).getName();
                cityName=cityEntities.get(options1).get(options2);
                txtProvince.setText(provinceEntities.get(options1).getName() + cityEntities.get(options1).get(options2));
            }
        }).build();
        provincePickview.setPicker(provinces, cityEntities);
        salePickview = new OptionsPickerView.Builder(PersonalActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                modelId = saleList.get(options1).getId();
                txtSale.setText(sales.get(options1));
            }
        }).build();
        salePickview.setPicker(sales);
        industryPickview = new OptionsPickerView.Builder(PersonalActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                txtVocation.setText(industrys1.get(options1) + industrys2.get(options1).get(options2));
                industryId = industryList2.get(options1).get(options2).getId();
            }
        }).build();
        industryPickview.setPicker(industrys1, industrys2);
        final ArrayList<String> sex = new ArrayList<>();
        sex.add("男");
        sex.add("女");
        sexPickview = new OptionsPickerView.Builder(PersonalActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                txtSex.setText(sex.get(options1));
            }
        }).build();
        sexPickview.setPicker(sex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnAddHelp.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.txt_position,R.id.back,R.id.btn_add_help2,R.id.txt_name,R.id.txt_message,R.id.txt_company,R.id.txt_province, R.id.txt_sale, R.id.txt_vocation, R.id.txt_sex})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.txt_province:
                if (provincePickview != null) {
                    provincePickview.show();
                }
                break;
            case R.id.txt_sale:
                salePickview.show();
                break;
            case R.id.txt_vocation:
                industryPickview.show();
                break;
            case R.id.txt_sex:
                sexPickview.show();
                break;
            case R.id.btn_add_help2:
                if(TextUtils.isEmpty(txtName.getText().toString())){
                    ToastUtils.showShortToast(this,"昵称不能为空");
                    return;
                }
                if(txtMessage.getText().length()>35){
                    ToastUtils.showShortToast(this,"简介最多35个字哦");
                    return;
                }
                completeInfo();
                break;
        }
    }
    private void completeInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("name", txtName.getText().toString().trim());
        if(!"".equals(provinceId))
            map.put("province_id", provinceId + "");
        if(!"".equals(cityId))
            map.put("city_id", cityId + "");
        if(!"".equals(industryId))
            map.put("industry_id", industryId);
        if(!"".equals(modelId))
            map.put("sales_id", modelId);
        map.put("sex", txtSex.getText().toString().equals("男") ? "1" : "0");
        map.put("message", txtMessage.getText().toString().trim());
        map.put("position", txtPosition.getText().toString().trim());
        map.put("company", txtCompany.getText().toString().trim());
        map.put("upload_stat",1);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), map);
        HttpManager.getInstance().completeInfo(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        ToastUtils.showShortToast(PersonalActivity.this, "完善成功");
        SPUtils.put(this, "name", txtName.getText().toString().trim());
        SPUtils.put(this, "sex",txtSex.getText().toString().equals("女")?"1.0":"0");
        SPUtils.put(this, "province", provinceName);
        SPUtils.put(this, "city", cityName);
        SPUtils.put(this, "company", txtCompany.getText().toString().trim());
        SPUtils.put(this, "position",txtPosition.getText().toString().trim());
        SPUtils.put(this, "industry", txtVocation.getText().toString().trim());
        SPUtils.put(this, "sales", txtSale.getText().toString().trim());
        SPUtils.put(this, "message",txtMessage.getText().toString().trim());
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
