package com.njjd.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by mrwim on 17/8/16.
 */

public class SaveEntity implements Serializable{
    private String article_uid;
    private String article_uid_headimg;
    private String article_uid_name;
    private String article_uid_introduction;
    private String article_id;
    private String article_content;
    private String title;
    private String article_imgs;
    private String article_answer_num;
    private String article_follow_num;
    private String article_point_num;
    private String comment_content;
    private String comment_collect_num;
    private String comment_point_num;
    private String comment_answer_num;
    private String comment_uid;
    private String comment_uid_headimg;
    private String comment_uid_name;
    private String comment_uid_introduction;
    private String collect_time;
    private String follow_article_user_stat;
    private String point_comment_stat;
    private String comment_id;
    public SaveEntity(JSONObject object){
        try {
            this.article_uid=object.getString("article_uid");
            this.article_uid_headimg=object.getString("article_uid_headimg");
            this.article_uid_name=object.isNull("article_uid_name")?"未填写":object.getString("article_uid_name");
            this.article_uid_introduction=object.isNull("article_uid_introduction")?"此人好神秘":object.getString("article_uid_introduction");
            this.article_content=object.getString("article_content");
            this.article_id=object.getString("article_id");
            this.title=object.getString("title");
            this.article_imgs=object.isNull("article_imgs")?"":object.getString("article_imgs").replace("[","").replace("]","").replace("\\/","/").replace("\\\\","/");
            this.article_answer_num=object.getString("article_answer_num");
            this.article_follow_num=object.getString("article_follow_num");
            this.article_point_num=object.getString("article_point_num");
            this.comment_content=object.getString("comment_content");
            this.comment_collect_num=object.getString("comment_collect_num");
            this.comment_point_num=object.getString("comment_point_num");
            this.comment_answer_num=object.getString("comment_answer_num");
            this.comment_uid=object.getString("comment_uid");
            this.comment_uid_headimg=object.getString("comment_uid_headimg");
            this.comment_uid_name=object.getString("comment_uid_name");
            this.comment_uid_introduction=object.getString("comment_uid_introduction");
            this.collect_time=object.getString("collect_time");
            this.follow_article_user_stat=object.getString("follow_article_user_stat");
            this.point_comment_stat=object.getString("point_comment_stat");
            this.comment_id=object.getString("comment_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public SaveEntity(){

    }
    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getArticle_uid() {
        return article_uid;
    }

    public void setArticle_uid(String article_uid) {
        this.article_uid = article_uid;
    }

    public String getArticle_uid_headimg() {
        return article_uid_headimg;
    }

    public void setArticle_uid_headimg(String article_uid_headimg) {
        this.article_uid_headimg = article_uid_headimg;
    }
    public String getArticle_content() {
        return article_content;
    }
    public void setArticle_content(String article_content) {
        this.article_content = article_content;
    }

    public String getPoint_comment_stat() {
        return point_comment_stat;
    }

    public void setPoint_comment_stat(String point_comment_stat) {
        this.point_comment_stat = point_comment_stat;
    }

    public String getArticle_uid_name() {
        return article_uid_name;
    }

    public void setArticle_uid_name(String article_uid_name) {
        this.article_uid_name = article_uid_name;
    }

    public String getArticle_uid_introduction() {
        return article_uid_introduction;
    }

    public void setArticle_uid_introduction(String article_uid_introduction) {
        this.article_uid_introduction = article_uid_introduction;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getComment_uid() {
        return comment_uid;
    }

    public void setComment_uid(String comment_uid) {
        this.comment_uid = comment_uid;
    }

    public String getComment_uid_headimg() {
        return comment_uid_headimg;
    }

    public void setComment_uid_headimg(String comment_uid_headimg) {
        this.comment_uid_headimg = comment_uid_headimg;
    }

    public String getComment_uid_name() {
        return comment_uid_name;
    }

    public void setComment_uid_name(String comment_uid_name) {
        this.comment_uid_name = comment_uid_name;
    }

    public String getComment_uid_introduction() {
        return comment_uid_introduction;
    }

    public void setComment_uid_introduction(String comment_uid_introduction) {
        this.comment_uid_introduction = comment_uid_introduction;
    }

    public String getCollect_time() {
        return collect_time;
    }

    public void setCollect_time(String collect_time) {
        this.collect_time = collect_time;
    }

    public String getFollow_article_user_stat() {
        return follow_article_user_stat;
    }

    public void setFollow_article_user_stat(String follow_article_user_stat) {
        this.follow_article_user_stat = follow_article_user_stat;
    }
}
