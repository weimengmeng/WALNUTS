package com.njjd.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by mrwim on 17/8/15.
 */
@Entity
public class BannerEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    private String title;
    private String url;
    @Id
    private String id;
    private String img;
    private String type;
//    public BannerEntity(String title, String url, String id, String img,String type) {
//        this.title = title;
//        this.url = url;
//        this.id = id;
//        this.img = img;
//        this.type=type;
//    }
//    public BannerEntity(){
//
//    }

    @Generated(hash = 300227573)
    public BannerEntity(String title, String url, String id, String img, String type) {
        this.title = title;
        this.url = url;
        this.id = id;
        this.img = img;
        this.type = type;
    }

    @Generated(hash = 1417309166)
    public BannerEntity() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
