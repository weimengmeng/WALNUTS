package com.njjd.domain;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by mrwim on 17/12/6.
 */

public class SearchArticleEntity implements Serializable{
    private String articleId;
    private String title;
    private String desc;
    private String img;
    private String uname;
    private String column_name;
    public SearchArticleEntity(JsonObject object) {
        this.articleId = object.get("id").getAsString();
        this.title = object.get("title").getAsString();
        this.desc = object.get("desci").getAsString();
        this.img = object.get("imgs").getAsString().replace("[","").replace("]","");
        this.uname = object.get("uname").getAsString();
        this.column_name = object.get("column_name").getAsString();
    }
    public SearchArticleEntity(){}
    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }
}
//   [{id=177.0, uid=23640953982, type=2.0, title=VS热播, desci=b'se'r,
//            contents=<p>别人发色部分的人<img src="http://192.168.0.111/hetao_api/public/" +
//            "/ueditor/php/upload/image/20171130/1512046762259875.jpg" title="15" +
//            "12046762259875.jpg" alt="暴风截图201791665061406.jpg"/></p>, imgs=[http:
//        //192.168.0.111/hetao_api/public/uploads/23640953982/cover/5a2000b321426.jpg
//        // ], answer_num=11.0, follow_num=0.0, point_num=0.0, collect_num=0.0, invisib
//        // le=0.0, anonymous=1.0, stat=��, sort_id=null, is_select=1.0, change_time=201
//        // 7-12-02 17:20:39, add_time=2017-11-30 20:59:31, headimg=http://192.168.0.11
//        // 1/hetao_api/public/static/headimgs/tu214.jpg, is_collect=0.0, uname=卓创2211,
//        // column_name=大咖学院}]
//
//    }
