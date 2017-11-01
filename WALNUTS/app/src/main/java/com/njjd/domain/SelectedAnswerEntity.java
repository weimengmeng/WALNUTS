package com.njjd.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by mrwim on 17/9/14.
 */

public class SelectedAnswerEntity implements Serializable {
    private String uid;//回答者id
    private String article_id;//文章ID
    private String answer_id;//回答ID
    private String head;//回答人头像
    private String name;//回答人name
    private String contents;//回答人name
    private String message;//回答人indrotuction
    private String title;//原问题标题
    private String isFocus="0";//是否关注了用户
    private String photo;//问题的图片
    private String replyContent;//回答的内容
    public SelectedAnswerEntity(JSONObject object) {
        try {
            name = object.isNull("uname") ? "" : object.getString("uname");
            uid = object.isNull("uid") ? "" : object.getString("uid");
            head = object.isNull("headimg") ? "" : object.getString("headimg");
            article_id = object.isNull("article_id") ? "" : object.getString("article_id");
            contents = object.isNull("contents") ? "" : object.getString("contents");
            message = object.isNull("introduction") ? "" : object.getString("introduction");
            answer_id = object.isNull("comment_id") ? "" : object.getString("comment_id");
            isFocus = object.isNull("is_focus") ? "0" : object.getString("is_focus");
            title = object.isNull("article_title") ? "" : object.getString("article_title");
            photo = object.isNull("imgs") ? "" : object.getString("imgs").replace("[","").replace("]","").replace("\\/","/").replace("\\\\","/");
            replyContent = object.isNull("comment_content") ? "" : object.getString("comment_content");

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
