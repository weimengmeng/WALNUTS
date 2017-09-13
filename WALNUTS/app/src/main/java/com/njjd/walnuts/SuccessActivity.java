package com.njjd.walnuts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.listener.ProgressListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.example.retrofit.util.JSONUtils;
import com.google.gson.JsonObject;
import com.njjd.application.ConstantsVal;
import com.njjd.domain.CommonEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.PhotoUtil;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;

/**
 * Created by mrwim on 17/7/12.
 */

public class SuccessActivity extends BaseActivity {
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.txt_province)
    TextView txtProvince;
    @BindView(R.id.txt_sale)
    TextView txtSale;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.txt_sex)
    TextView txtSex;
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

    private OptionsPickerView<String> provincePickview, salePickview, industryPickview, sexPickview;
    private String provinceId = "", cityId = "", industryId = "", modelId = "";
    @BindView(R.id.img_back)
    LinearLayout imgBack;
    private String path = "";//头像地址
    private File file;

    @Override
    public int bindLayout() {
        return R.layout.activity_success;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView2(this, imgBack);
        if (getIntent().getIntExtra("bind", 0) == 1) {
            //绑定账号情况，预先设置第三方的头像性别等
            GlideImageLoder.getInstance().displayImage(this, SPUtils.get(this, "thirdHead", "").toString(), imgHead);
            etName.setText(SPUtils.get(this, "thirdName", "").toString());
            txtSex.setText(SPUtils.get(this, "thirdSex", "").toString());
            path = SPUtils.get(this, "thirdHead", "").toString();
        }
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
        provincePickview = new OptionsPickerView.Builder(SuccessActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                provinceId = provinceEntities.get(options1).getId();
                cityId = cityList.get(options1).get(options2).getId();
                txtProvince.setText(provinceEntities.get(options1).getName() + cityEntities.get(options1).get(options2));
            }
        }).build();
        provincePickview.setPicker(provinces, cityEntities);
        salePickview = new OptionsPickerView.Builder(SuccessActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                modelId = saleList.get(options1).getId();
                txtSale.setText(sales.get(options1));
            }
        }).build();
        salePickview.setPicker(sales);
        industryPickview = new OptionsPickerView.Builder(SuccessActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                txtVocation.setText(industrys1.get(options1)  + industrys2.get(options1).get(options2));
                industryId = industryList2.get(options1).get(options2).getId();
            }
        }).build();
        industryPickview.setPicker(industrys1, industrys2);
        final ArrayList<String> sex = new ArrayList<>();
        sex.add("男");
        sex.add("女");
        sexPickview = new OptionsPickerView.Builder(SuccessActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
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
    }

    @OnClick({R.id.img_back, R.id.img_head, R.id.txt_province, R.id.txt_sale, R.id.txt_vocation, R.id.txt_sex, R.id.txt_agreement, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_head:
                Picker.from(this)
                        .count(1)
                        .enableCamera(true)
                        .setEngine(new GlideEngine())
                        .forResult(REQUEST_CODE);
                //1多选 2 单选 单选才有裁剪功能
//                ImageSelectorActivity.start(this, 1, 2, true, true, true);
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
            case R.id.txt_agreement:
                Intent intent = new Intent(this, AgreementActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_submit:
                if (path.equals("")) {
                    ToastUtils.showShortToast(this, "请先选择头像");
                    return;
                }
                if (etName.getText().toString().trim().equals("")) {
                    ToastUtils.showShortToast(this, "请输入姓名");
                    return;
                }
                if ("".equals(provinceId) || "".equals(cityId)) {
                    ToastUtils.showShortToast(SuccessActivity.this, "请先选择地区");
                    return;
                }
                if ("".equals(industryId)) {
                    ToastUtils.showShortToast(this, "请选择行业");
                    return;
                }
                if (file == null) {
                    completeInfo();
                } else {
                    uploadFile();
                }
                break;
        }
    }

    private void completeInfo() {
        MobclickAgent.onEvent(this, ConstantsVal.REGIST_COMINFO);
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("token", SPUtils.get(this, "token", "").toString());
        map.put("name", etName.getText().toString().trim());
        map.put("province_id", provinceId + "");
        map.put("city_id", cityId + "");
        map.put("industry_id", industryId);
        map.put("sales_id", modelId);
        map.put("sex",txtSex.getText().toString().equals("男") ? "1" : "0");
        map.put("message", "");
        map.put("position", "");
        map.put("headimg", path);
        map.put("upload_stat", 1);
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), map);
        HttpManager.getInstance().completeInfo(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        ToastUtils.showShortToast(SuccessActivity.this, "完善成功");
        SPUtils.put(SuccessActivity.this, ConstantsVal.AUTOLOGIN,"true");
        SPUtils.put(SuccessActivity.this,ConstantsVal.LOGINTYPE,"0");
        startActivity(new Intent(this,InterestActivity.class));
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
            String imgpath= PhotoUtil.saveMyBitmapWH(CommonUtils.getRealPathFromUri(this,mSelected.get(0)), 480,800);
            file = new File(imgpath);
            GlideImageLoder.getInstance().displayImage(this, file.getPath(), imgHead);
            LogUtils.d("huan"+imgpath);
            path = imgpath;
        }
    }

    private void uploadFile() {
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(upFileListener, this, true, false), file);
        HttpManager.getInstance().uploadFile(postEntity, new MyUploadListener(),
                SPUtils.get(this, "userId", "").toString(), SPUtils.get(this, "token", "").toString());
    }

    HttpOnNextListener upFileListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            LogUtils.d(o.toString());
            JsonObject object = JSONUtils.getAsJsonObject(o);
            path = object.get("path").getAsString();
            completeInfo();
        }
    };

    class MyUploadListener implements ProgressListener {
        @Override
        public void onProgress(long progress, long total, boolean done) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
