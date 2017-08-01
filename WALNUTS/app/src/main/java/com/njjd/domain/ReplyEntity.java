package com.njjd.domain;

import java.io.Serializable;

/**
 * Created by mrwim on 17/7/31.
 */

public class ReplyEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ReplyId;
    private String replyedUId;//被回复人id
    private String replyUId;//回复人ID
    private String head;
    private String name;
    private String message;
    private String content;
    private String replyNum;
    private String replyName;//被回复人昵称

    public ReplyEntity(String replyId, String replyedUId, String replyUId, String head, String name, String message, String content, String replyNum, String replyName) {
        ReplyId = replyId;
        this.replyedUId = replyedUId;
        this.replyUId = replyUId;
        this.head = head;
        this.name = name;
        this.message = message;
        this.content = content;
        this.replyNum = replyNum;
        this.replyName = replyName;
    }

    public String getReplyId() {
        return ReplyId;
    }

    public void setReplyId(String replyId) {
        ReplyId = replyId;
    }

    public String getReplyedUId() {
        return replyedUId;
    }

    public void setReplyedUId(String replyedUId) {
        this.replyedUId = replyedUId;
    }

    public String getReplyUId() {
        return replyUId;
    }

    public void setReplyUId(String replyUId) {
        this.replyUId = replyUId;
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

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }
}
