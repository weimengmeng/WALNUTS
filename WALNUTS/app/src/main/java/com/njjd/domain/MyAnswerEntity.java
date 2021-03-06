package com.njjd.domain;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by mrwim on 17/8/16.
 */

public class MyAnswerEntity implements Serializable {
    private String article_id;
    private String article_content;
    private String title;
    private String article_imgs;
    private String article_answer_num;
    private String article_follow_num;
    private String article_point_num;
    private String comment_id;
    private String comment_content;
    private String comment_collect_num;
    private String comment_point_num;
    private String comment_answer_num;
    private String add_time;
    private String follow_article_stat;
    private String point_comment_stat;
    private String type;
    private String stat="";
    public MyAnswerEntity(JSONObject object){
        try {
            this.article_id=object.isNull("article_id")?"":object.getString("article_id");
            this.article_content=object.isNull("article_content")?"":object.getString("article_content");
            this.article_answer_num=object.isNull("article_answer_num")?"":object.getString("article_answer_num");
            this.article_follow_num=object.isNull("article_follow_num")?"":object.getString("article_follow_num");
            this.article_point_num=object.isNull("article_point_num")?"":object.getString("article_point_num");
            this.title=object.isNull("title")?"":object.getString("title");
            this.article_imgs=object.isNull("article_imgs")?"":object.getString("article_imgs").replace("[","").replace("]","").replace("\\/","/").replace("\\\\","/");
            this.comment_content=object.isNull("comment_content")?"":object.getString("comment_content");
            this.comment_collect_num=object.isNull("comment_collect_num")?"":object.getString("comment_collect_num");
            this.comment_point_num=object.isNull("comment_point_num")?"":object.getString("comment_point_num");
            this.comment_answer_num=object.isNull("comment_answer_num")?"":object.getString("comment_answer_num");
            this.point_comment_stat=object.isNull("point_comment_stat")?"":object.getString("point_comment_stat");
            this.comment_id=object.getString("comment_id");
            this.add_time=object.getString("add_time");
            this.type=object.getString("type");
            this.stat=object.isNull("stat")?"":object.getString("stat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public MyAnswerEntity() {
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getArticle_content() {
        return article_content;
    }

    public void setArticle_content(String article_content) {
        this.article_content = article_content;
    }

    public String getArticle_imgs() {
        return article_imgs;
    }

    public void setArticle_imgs(String article_imgs) {
        this.article_imgs = article_imgs;
    }

    public String getArticle_answer_num() {
        return article_answer_num;
    }

    public void setArticle_answer_num(String article_answer_num) {
        this.article_answer_num = article_answer_num;
    }

    public String getArticle_follow_num() {
        return article_follow_num;
    }

    public void setArticle_follow_num(String article_follow_num) {
        this.article_follow_num = article_follow_num;
    }

    public String getArticle_point_num() {
        return article_point_num;
    }

    public void setArticle_point_num(String article_point_num) {
        this.article_point_num = article_point_num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_collect_num() {
        return comment_collect_num;
    }

    public void setComment_collect_num(String comment_collect_num) {
        this.comment_collect_num = comment_collect_num;
    }

    public String getComment_point_num() {
        return comment_point_num;
    }

    public void setComment_point_num(String comment_point_num) {
        this.comment_point_num = comment_point_num;
    }

    public String getComment_answer_num() {
        return comment_answer_num;
    }

    public void setComment_answer_num(String comment_answer_num) {
        this.comment_answer_num = comment_answer_num;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getFollow_article_stat() {
        return follow_article_stat;
    }

    public void setFollow_article_stat(String follow_article_stat) {
        this.follow_article_stat = follow_article_stat;
    }

    public String getPoint_comment_stat() {
        return point_comment_stat;
    }

    public void setPoint_comment_stat(String point_comment_stat) {
        this.point_comment_stat = point_comment_stat;
    }
}
