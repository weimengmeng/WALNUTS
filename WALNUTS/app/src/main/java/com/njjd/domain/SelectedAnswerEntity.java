package com.njjd.domain;

import java.io.Serializable;

/**
 * Created by mrwim on 17/9/14.
 */

public class SelectedAnswerEntity implements Serializable {
    private String uid;//回答者id
    private String article_id;//文章ID
    private String answer_id;//回答ID
    private String head;
    private String name;
    private String message;
    private String title;
    private String isFocus="0";
    private String photo;
    private String replyContent;

    public SelectedAnswerEntity(String uid, String article_id, String answer_id, String head, String name, String message, String title, String isFocus, String photo, String replyContent) {
        this.uid = uid;
        this.article_id = article_id;
        this.answer_id = answer_id;
        this.head = head;
        this.name = name;
        this.message = message;
        this.title = title;
        this.isFocus = isFocus;
        this.photo = photo;
        this.replyContent = replyContent;
    }

    public SelectedAnswerEntity(){

    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(String isFocus) {
        this.isFocus = isFocus;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
}
