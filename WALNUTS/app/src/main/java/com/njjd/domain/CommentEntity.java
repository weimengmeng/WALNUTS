package com.njjd.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mrwim on 17/7/31.
 */

public class CommentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String commentId="";
    private String commentUId="";//评论人ID
    private String head="";
    private String name="";
    private String message="";
    private String content="";
    private String replyNum="0";
    private String time="";
    private List<ReplyEntity> replyEntities;
    public CommentEntity(JSONObject object){

        try {
            this.commentId=object.isNull("id")?"":object.getString("id");
            this.commentUId=object.isNull("uid")?"":object.getString("uid");
            this.head=object.isNull("headimgs")?"":object.getString("headimgs");
            this.name=object.isNull("uname")?"":object.getString("uname");
            this.message=object.isNull("introduction")?"":object.getString("introduction");
            this.content=object.isNull("content")?"":object.getString("content");
            this.time= object.isNull("change_time")?"":object.getString("change_time");
            this.replyNum =object.isNull("answer_num")?"":object.getString("answer_num");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public CommentEntity(){

    }
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public String getCommentUId() {
        return commentUId;
    }

    public void setCommentUId(String commentUId) {
        this.commentUId = commentUId;
    }

    public List<ReplyEntity> getReplyEntities() {
        return replyEntities;
    }

    public void setReplyEntities(List<ReplyEntity> replyEntities) {
        this.replyEntities = replyEntities;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
