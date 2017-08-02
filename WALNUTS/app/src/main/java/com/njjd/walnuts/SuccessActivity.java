package com.njjd.walnuts;

import android.content.Intent;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.njjd.domain.ProvinceEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.GlideImageLoder;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.yongchun.library.view.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    @BindView(R.id.txt_city)
    TextView txtCity;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.txt_sex)
    TextView txtSex;
    private List<String> provinces = new ArrayList<>();
    private List<ProvinceEntity> provinceEntities = new ArrayList<>();
    private List<ProvinceEntity> cityEntities = new ArrayList<>();
    private List<String> citys = new ArrayList<>();
    private List<String> industrys1 = new ArrayList<>();
    private List<List<String>> industrys2 = new ArrayList<>();
    private OptionsPickerView<String> provincePickview, cityPickview, industryPickview, sexPickview;
    private String provinceId = "", cityId = "", industryId = "", provinceCode = "";
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
        ImmersedStatusbarUtils.initAfterSetContentView(this, imgBack);
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
        if (getIntent().getIntExtra("bind", 0) == 1) {
            //绑定账号情况，预先设置第三方的头像性别等
            GlideImageLoder.getInstance().displayImage(this, SPUtils.get(this, "thirdHead", "").toString(), imgHead);
            etName.setText(SPUtils.get(this, "thirdName", "").toString());
            txtSex.setText(SPUtils.get(this, "thirdSex", "").toString());
            path = SPUtils.get(this, "thirdHead", "").toString();
        }
        getProvinces();
        getIndustry();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getProvinces() {
        Map<String, String> map = new HashMap<>();
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener<Object>() {
            @Override
            public void onNext(Object o) {
                JsonArray array = JSONUtils.getAsJsonArray(o);
                JsonObject object = null;
                ProvinceEntity entity;
                for (int i = 0; i < array.size(); i++) {
                    object = array.get(i).getAsJsonObject();
                    entity = new ProvinceEntity(object.get("id").getAsString(), object.get("name").getAsString(), object.get("code").getAsString());
                    provinceEntities.add(entity);
                    provinces.add(object.get("name").getAsString());
                }
                provincePickview = new OptionsPickerView.Builder(SuccessActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        provinceId = provinceEntities.get(options1).getId();
                        provinceCode = provinceEntities.get(options1).getCode();
                        txtProvince.setText(provinceEntities.get(options1).getName());
                        citys.clear();
                        cityEntities.clear();
                        getcitys(provinceCode);
                    }
                }).build();
                provincePickview.setPicker(provinces);
            }
        }, this, false, false), map);
        HttpManager.getInstance().provinceList(postEntity);
    }

    private void getcitys(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener<Object>() {
            @Override
            public void onNext(Object o) {
                JsonArray array = JSONUtils.getAsJsonArray(o);
                JsonObject object = null;
                ProvinceEntity entity;
                for (int i = 0; i < array.size(); i++) {
                    object = array.get(i).getAsJsonObject();
                    entity = new ProvinceEntity(object.get("id").getAsString(), object.get("name").getAsString(), object.get("code").getAsString());
                    cityEntities.add(entity);
                    citys.add(object.get("name").getAsString());
                }
                cityPickview = new OptionsPickerView.Builder(SuccessActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        cityId = cityEntities.get(options1).getId();
                        txtCity.setText(cityEntities.get(options1).getName());
                    }
                }).build();
                cityPickview.setPicker(citys);
            }
        }, this, false, false), map);
        HttpManager.getInstance().cityList(postEntity);
    }

    private void getIndustry() {
        //选项1
        industrys1.add("广东");
        industrys1.add("湖南");
        industrys1.add("广西");
        //选项2
        ArrayList<String> options2Items_01 = new ArrayList<>();
        options2Items_01.add("广州");
        options2Items_01.add("佛山");
        options2Items_01.add("东莞");
        options2Items_01.add("珠海");
        ArrayList<String> options2Items_02 = new ArrayList<>();
        options2Items_02.add("长沙");
        options2Items_02.add("岳阳");
        options2Items_02.add("株洲");
        options2Items_02.add("衡阳");
        ArrayList<String> options2Items_03 = new ArrayList<>();
        options2Items_03.add("桂林");
        options2Items_03.add("玉林");
        industrys2.add(options2Items_01);
        industrys2.add(options2Items_02);
        industrys2.add(options2Items_03);
        industryPickview = new OptionsPickerView.Builder(SuccessActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                txtVocation.setText(industrys1.get(options1) + "" + industrys2.get(options1).get(options2));
                ToastUtils.showShortToast(SuccessActivity.this, industrys1.get(options1) + "  " + industrys2.get(options1).get(options2));
            }
        }).build();
        industryPickview.setPicker(industrys1, industrys2);
//        Map<String,String> map=new HashMap<>();
//        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener<Object>(){
//            @Override
//            public void onNext(Object o) {
//                industryPickview=new OptionsPickerView.Builder(SuccessActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
//                    @Override
//                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                        ToastUtils.showShortToast(SuccessActivity.this, industrys1.get(options1)+"  "+industrys2.get(options1).get(options2));
//                    }
//                }).build();
//                industryPickview.setPicker(industrys1,industrys2);
//            }
//        }, this,false, false), map);
//        HttpManager.getInstance().industryList(postEntity);
    }

    @OnClick({R.id.img_back, R.id.img_head, R.id.txt_province, R.id.txt_city, R.id.txt_vocation, R.id.txt_sex, R.id.txt_agreement, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_head:
                //1多选 2 单选 单选才有裁剪功能
                ImageSelectorActivity.start(this, 1, 2, true, true, true);
                break;
            case R.id.txt_province:
                if (provincePickview != null) {
                    provincePickview.show();
                }
                break;
            case R.id.txt_city:
                if ("".equals(provinceId) || "".equals(provinceCode)) {
                    ToastUtils.showShortToast(SuccessActivity.this, "请先选择有效省份");
                    return;
                }
                if (cityPickview != null) {
                    cityPickview.show();
                }
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
                if ("".equals(provinceId) || "".equals(provinceCode) || "".equals(cityId)) {
                    ToastUtils.showShortToast(SuccessActivity.this, "请先选择地区");
                    return;
                }
//                if("".equals(industryId)){
//                    ToastUtils.showShortToast(this,"请选择行业");
//                    return;
//                }
                if(file==null){
                    completeInfo();
                }else{
                    uploadFile();
                }
                break;
        }
    }

    private void completeInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", SPUtils.get(this, "userId", "").toString());
        map.put("name", etName.getText().toString().trim());
        map.put("province_id", provinceId + "");
        map.put("city_id", cityId + "");
        map.put("industry_id", "1");
        map.put("sex", txtSex.getText().toString().equals("男") ? "0" : "1");
        map.put("message", "");
        map.put("position", "");
        map.put("headimg", path);
        LogUtils.d(map.toString());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), map);
        HttpManager.getInstance().completeInfo(postEntity);
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        ToastUtils.showShortToast(SuccessActivity.this, "完善成功");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            file = new File(images.get(0));
            GlideImageLoder.getInstance().displayImage(this, images.get(0), imgHead);
            path=images.get(0);
        }
    }

    private void uploadFile() {
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(upFileListener, this, true, false), file);
        HttpManager.getInstance().uploadFile(postEntity, new MyUploadListener());
    }

    HttpOnNextListener upFileListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
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
