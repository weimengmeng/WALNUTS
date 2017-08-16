package com.njjd.domain;

import com.youth.banner.Banner;

/**
 * Created by mrwim on 17/8/15.
 */

public class BannerEntity {
    private String title;
    private String url;
    private String id;
    private String img;

    public BannerEntity(String title, String url, String id, String img) {
        this.title = title;
        this.url = url;
        this.id = id;
        this.img = img;
    }
    public BannerEntity(){

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
