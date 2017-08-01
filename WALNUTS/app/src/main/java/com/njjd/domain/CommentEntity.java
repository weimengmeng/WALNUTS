package com.njjd.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mrwim on 17/7/31.
 */

public class CommentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String commentId;
    private String commentUId;//评论人ID
    private String head;
    private String name;
    private String message;
    private String content;
    private String replyNum;
    private String time;
    private List<ReplyEntity> replyEntities;

    public CommentEntity(String commentId, String commentUId, String head, String name,
                         String message, String content, String replyNum,String time) {
        this.commentId = commentId;
        this.commentUId = commentUId;
        this.head = head;
        this.name = name;
        this.message = message;
        this.content = content;
        this.replyNum = replyNum;
        this.time=time;
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
