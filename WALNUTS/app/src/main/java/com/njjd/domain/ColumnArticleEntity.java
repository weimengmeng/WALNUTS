package com.njjd.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by mrwim on 17/10/20.
 */

public class ColumnArticleEntity implements Serializable {
    public String article_id;//文章ID或者专栏ID
    public String uid;//用户ID
    public String head;//作者头像
    public String name;//作者名称
    public String title;//文章标题
    public String content;//文章内容
    public String pic;//文章配图
    public String desci;//文章描述
    public String commentNum;
    public String pointNum;
    public String time;
    public ColumnArticleEntity(){

    }
    public ColumnArticleEntity(JSONObject object){
        try {
            this.article_id=object.getString("id");
            this.head=object.isNull("headimg")?"":object.getString("headimg");
            this.name=object.isNull("uname")?"":object.getString("uname");
            this.title=object.isNull("title")?"":object.getString("title");
            this.desci=object.isNull("desci")?"":object.getString("desci");
            this.content=object.isNull("contents")?"":object.getString("contents");
            this.pic=object.isNull("imgs")?"":object.getString("imgs").replace("[","").replace("]","").replace("\\/","/").replace("\\\\","/");
            this.commentNum=object.isNull("answer_num")?"":object.getString("answer_num");
            this.pointNum=object.isNull("point_num")?"":object.getString("point_num");
            this.uid=object.isNull("uid")?"":object.getString("uid");
            this.time=object.isNull("add_time")?"":object.getString("add_time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getArticle_id() {
        return article_id;
    }

    public String getDesci() {
        return desci;
    }

    public void setDesci(String desci) {
        this.desci = desci;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getPointNum() {
        return pointNum;
    }

    public void setPointNum(String pointNum) {
        this.pointNum = pointNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
