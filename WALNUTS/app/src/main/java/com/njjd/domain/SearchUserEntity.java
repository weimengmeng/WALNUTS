package com.njjd.domain;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by mrwim on 17/12/6.
 */

public class SearchUserEntity implements Serializable{
    private String uid;
    private String uname;
    private String headimg;
    private String area;
    private String product;
    private String industry1;
    private String industry2;
    public SearchUserEntity(JsonObject object) {
        this.uid = object.get("uid").getAsString();
        this.uname = object.get("uname").getAsString();
        this.area = object.get("city_name").getAsString();
        this.headimg = object.get("headimg").getAsString();
        this.product = object.get("product").getAsString();
        this.industry1 = object.get("f_insdustry_name").getAsString();
        this.industry2 = object.get("industry_name").getAsString();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public SearchUserEntity() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getIndustry1() {
        return industry1;
    }

    public void setIndustry1(String industry1) {
        this.industry1 = industry1;
    }

    public String getIndustry2() {
        return industry2;
    }

    public void setIndustry2(String industry2) {
        this.industry2 = industry2;
    }
}
//    [{id=2689.0, uid=92447413178, uname=霸王, pwd=5c1d8c3a5796d330a651d1eb91a2a7e4,
//            phone=92447413178, city_id=72.0, province_id=9.0, industry_id=15.0,
//            sales_id=1.0, company=null, position=客户经理, sex=0.0,
//            headimg=http://192.168.0.111/hetao_api/public/static/headimgs/tu111.jpg,
//        // introduction=三分理论 七分实践~, product=, follow_num=2.0, be_follow_num=20.0,
//        // qq_uid=, wechat_uid=, sina_uid=, device_token=, token=, register_type=4.0,
//        // stat=1.0, login_time=null, user_type=0.0, change_time=2017-10-11 01:51:21,
//        // add_time=2017-10-11 01:51:21, province_name=上海市, city_name=上海市,
//        // industry_name=网络设备}
//}
