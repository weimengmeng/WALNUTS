package com.njjd.domain;

import com.google.gson.JsonObject;

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
    public MyAnswerEntity(JsonObject object){
        this.article_id=object.get("article_id").getAsString();
        this.article_content=object.get("article_content").getAsString();
        this.article_imgs=object.get("article_imgs").getAsString();
        this.article_answer_num=object.get("article_answer_num").getAsString();
        this.article_follow_num=object.get("article_follow_num").getAsString();
        this.article_point_num=object.get("article_point_num").getAsString();
        this.title=object.get("title").getAsString();
        this.article_imgs=object.get("article_imgs").getAsString().replace("[","").replace("]","").replace("\\/","/").replace("\\\\","/");
        this.comment_content=object.get("comment_content").getAsString();
        this.comment_collect_num=object.get("comment_collect_num").getAsString();
        this.comment_point_num=object.get("comment_point_num").getAsString();
        this.comment_answer_num=object.get("comment_answer_num").getAsString();
        this.point_comment_stat=object.get("point_comment_stat").getAsString();
        this.comment_id=object.get("comment_id").getAsString();
        this.add_time=object.get("add_time").getAsString();
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
