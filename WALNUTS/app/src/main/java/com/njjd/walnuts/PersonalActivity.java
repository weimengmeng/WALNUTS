package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.pickerview.OptionsPickerView;
import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.domain.CommonEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
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

public class PersonalActivity extends BaseActivity implements OnItemClickListener, OnDismissListener{
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_sex)
    TextView txtSex;
    @BindView(R.id.txt_message)
    TextView txtMessage;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.txt_province)
    TextView txtProvince;
    @BindView(R.id.txt_position)
    TextView txtPosition;
    @BindView(R.id.txt_company)
    TextView txtCompany;
    @BindView(R.id.btn_add_help)
    TextView btnAddHelp;
    @BindView(R.id.txt_sale)
    TextView txtSale;
    @BindView(R.id.lv_back)
    SwipeBackLayout lvBack;
    private AlertView mAlertViewExt;//窗口拓展例子
    private EditText etName;//拓展View内容
    private InputMethodManager imm;
    ViewGroup extView;
    private int flag=0;
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
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        txtName.setText(SPUtils.get(this,"name","").toString());
        txtMessage.setText(SPUtils.get(this,"message","未完善").toString());
        txtPosition.setText(SPUtils.get(this,"position","未完善").toString());
        txtProvince.setText(SPUtils.get(this,"province","").toString()+SPUtils.get(this,"city","").toString());
        txtVocation.setText(SPUtils.get(this,"industry","").toString());
        if(SPUtils.get(this,"sex","0").toString().equals("1.0")){
            txtSex.setText("女");
        }else{
            txtSex.setText("男");
        }
        txtCompany.setText(SPUtils.get(this,"company","待完善").toString());
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
        ImmersedStatusbarUtils.initAfterSetContentView(this, top);
        btnAddHelp.setVisibility(View.VISIBLE);
        lvBack.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }

    @OnClick({R.id.txt_position,R.id.back,R.id.btn_add_help,R.id.txt_name,R.id.txt_message,R.id.txt_company,R.id.txt_province, R.id.txt_sale, R.id.txt_vocation, R.id.txt_sex})
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
            case R.id.txt_name:
                flag=0;
                mAlertViewExt = new AlertView("提示", "请填写姓名", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
                extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_et,null);
                etName = (EditText) extView.findViewById(R.id.etName);
                etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focus) {
                        //输入框出来则往上移动
                        boolean isOpen=imm.isActive();
                        mAlertViewExt.setMarginBottom(isOpen&&focus ? 120 :0);
                        System.out.println(isOpen);
                    }
                });
                mAlertViewExt.addExtView(extView);
                mAlertViewExt.show();
                break;
            case R.id.txt_message:
                flag=1;
                mAlertViewExt = new AlertView("提示", "请填写签名(简介)", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
                extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_et,null);
                etName = (EditText) extView.findViewById(R.id.etName);
                etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focus) {
                        //输入框出来则往上移动
                        boolean isOpen=imm.isActive();
                        mAlertViewExt.setMarginBottom(isOpen&&focus ? 120 :0);
                        System.out.println(isOpen);
                    }
                });
                mAlertViewExt.addExtView(extView);
                mAlertViewExt.show();
                break;
            case R.id.txt_company:
                flag=2;
                mAlertViewExt = new AlertView("提示", "请输入公司名称", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
                extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_et,null);
                etName = (EditText) extView.findViewById(R.id.etName);
                etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focus) {
                        //输入框出来则往上移动
                        boolean isOpen=imm.isActive();
                        mAlertViewExt.setMarginBottom(isOpen&&focus ? 120 :0);
                        System.out.println(isOpen);
                    }
                });
                mAlertViewExt.addExtView(extView);
                mAlertViewExt.show();
                break;
            case R.id.txt_position:
                flag=3;
                mAlertViewExt = new AlertView("提示", "请输入z昵称", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
                extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_et,null);
                etName = (EditText) extView.findViewById(R.id.etName);
                etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focus) {
                        //输入框出来则往上移动
                        boolean isOpen=imm.isActive();
                        mAlertViewExt.setMarginBottom(isOpen&&focus ? 120 :0);
                        System.out.println(isOpen);
                    }
                });
                mAlertViewExt.addExtView(extView);
                mAlertViewExt.show();
                break;
            case R.id.btn_add_help:
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
        map.put("sex", txtSex.getText().toString().equals("男") ? "0" : "1");
        map.put("message", txtMessage.getText().toString().trim());
        map.put("position", txtPosition.getText().toString().trim());
        map.put("company", txtCompany.getText().toString().trim());
        map.put("upload_stat",0);
        LogUtils.d(map.toString());
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
    }
    private void closeKeyboard() {
        //关闭软键盘
        imm.hideSoftInputFromWindow(etName.getWindowToken(),0);
        //恢复位置
        mAlertViewExt.setMarginBottom(0);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDismiss(Object o) {
        closeKeyboard();
    }

    @Override
    public void onItemClick(Object o, int position) {
        closeKeyboard();
        //判断是否是拓展窗口View，而且点击的是非取消按钮
        if(o == mAlertViewExt && position != AlertView.CANCELPOSITION){
            String content = etName.getText().toString();
            if(content.isEmpty()){
                ToastUtils.showShortToast(this, "啥都没填呢");
            }
            else{
                switch (flag){
                    case 0:
                        txtName.setText(content);
                        break;
                    case 1:
                        if(content.trim().length()>49){
                            ToastUtils.showShortToast(this,"签名最多50字哦");
                            return;
                        }
                        txtMessage.setText(content);
                        break;
                    case 2:
                        txtCompany.setText(content);
                        break;
                    case 3:
                        txtPosition.setText(content);
                        break;
                }
            }
            return;
        }

    }
}
