package com.njjd.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by mrwim on 17/10/20.
 */

public class ColumnEntity implements Serializable {
    private String id;
    private String name;
    private String uid;// 作者
    private String uname;//用户名
    private String follow_num;//关注人数
    private String is_follow;//用户是否关注
    private String uhead;
    private String desc;//专栏描述
    private String pic;//专栏大图
    private String crousel_img;
    public ColumnEntity(JSONObject object){
        try {
            this.id=object.getString("id");
            this.uid=object.isNull("uid")?"":object.getString("uid");
            this.name=object.getString("column_name");
            this.uname=object.getString("uname");
            this.uhead=object.isNull("img")?"":object.getString("img").replace("[","").replace("]","").replace("\\/","/").replace("\\\\","/");
            this.pic=object.isNull("bg_img")?"":object.getString("bg_img").replace("[","").replace("]","").replace("\\/","/").replace("\\\\","/");
            this.desc=object.getString("desc");
            this.follow_num=object.getString("follow_numm");
            this.is_follow=object.getString("is_follow");
            this.crousel_img=object.isNull("crousel_img")?"":object.getString("crousel_img").replace("[","").replace("]","").replace("\\/","/").replace("\\\\","/");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCrousel_img() {
        return crousel_img;
    }

    public void setCrousel_img(String crousel_img) {
        this.crousel_img = crousel_img;
    }

    public String getId() {
        return id;
    }

    public String getUhead() {
        return uhead;
    }

    public void setUhead(String uhead) {
        this.uhead = uhead;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(String follow_num) {
        this.follow_num = follow_num;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
