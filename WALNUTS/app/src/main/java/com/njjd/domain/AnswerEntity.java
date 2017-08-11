package com.njjd.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mrwim on 17/7/31.
 */

public class AnswerEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String anwerId;
    private String answerUId;//回答人ID
    private String head;
    private String name;
    private String message;
    private String content;
    private String agree;
    private String open;
    private String time;
    private String isSave;
    private String isPrise;
    private List<CommentEntity> commentEntityList;

    public AnswerEntity(String anwerId, String answerUId, String head,
                        String name, String message, String content, String agree,
                        String open,String time,String isSave,String isPrise) {
        this.anwerId = anwerId;
        this.answerUId = answerUId;
        this.head = head;
        this.name = name;
        this.message = message;
        this.content = content;
        this.agree = agree;
        this.open = open;
        this.time=time;
        this.isPrise=isPrise;
        this.isSave=isSave;
    }
    public AnswerEntity(){

    }

    public String getIsSave() {
        return isSave;
    }

    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }

    public String getIsPrise() {
        return isPrise;
    }

    public void setIsPrise(String isPrise) {
        this.isPrise = isPrise;
    }

    public String getAnwerId() {
        return anwerId;
    }

    public void setAnwerId(String anwerId) {
        this.anwerId = anwerId;
    }

    public String getAnswerUId() {
        return answerUId;
    }

    public void setAnswerUId(String answerUId) {
        this.answerUId = answerUId;
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

    public String getAgree() {
        return agree;
    }

    public void setAgree(String agree) {
        this.agree = agree;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public List<CommentEntity> getCommentEntityList() {
        return commentEntityList;
    }

    public void setCommentEntityList(List<CommentEntity> commentEntityList) {
        this.commentEntityList = commentEntityList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
