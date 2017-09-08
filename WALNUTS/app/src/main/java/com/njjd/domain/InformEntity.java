package com.njjd.domain;

import com.njjd.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by mrwim on 17/8/22.
 */

public class InformEntity implements Serializable {
    private String uname;
    private String uid;
    private String headimg;
    private String article_id;
    private String comment_id;
    private JSONObject content;
    private String contents;
    private String stat;
    private String type;
    private String add_time;
    public InformEntity(JSONObject object){
        try {
            uname=object.isNull("uname")?"":object.getString("uname");
            uid=object.isNull("uid")?"":object.getString("uid");
            headimg=object.isNull("headimg")?"":object.getString("headimg");
            article_id=object.isNull("article_id")?"":object.getString("article_id");
            comment_id=object.isNull("comment_id")?"":object.getString("comment_id");
            stat=object.isNull("stat")?"":object.getString("stat");
            type=object.isNull("type")?"0":object.getString("type");
            add_time=object.isNull("add_time")?"":object.getString("add_time");
            if(!type.equals("1.0")||!type.equals("0.0")){
                if(type.equals("2.0")){
                    content=object.isNull("content")?null:object.getJSONArray("content").getJSONObject(0);
                }else{
                    content=object.isNull("content")?null:object.getJSONObject("content");
                }
                if(type.equals("3.0")){
                    comment_id=content.isNull("comment_id")?"":content.getString("comment_id");
                }
            }else{
                contents=object.isNull("content")?"":object.getString("content");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
