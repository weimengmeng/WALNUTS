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
    private String sec_uid="";
    private String sec_com_id="";
    private String sec_content;
    private String sec_uname="";
    private String sec_introduction="";
    private String sec_headimgs="";
    public CommentEntity(JSONObject object){

        try {
            this.commentId=object.isNull("id")?"":object.getString("id");
            this.commentUId=object.isNull("uid")?"":object.getString("uid");
            this.head=object.isNull("headimgs")?"":object.getString("headimgs");
            this.name=object.isNull("uname")?"":object.getString("uname");
            this.message=object.isNull("introduction")?"":object.getString("introduction");
            this.content=object.isNull("content")?"":object.getString("content");
            this.time= object.isNull("add_time")?"":object.getString("add_time");
            this.replyNum =object.isNull("answer_num")?"0":object.getString("answer_num");

            this.sec_uid=object.isNull("sec_uid")?"":object.getString("sec_uid");
            this.sec_com_id=object.isNull("sec_com_id")?"":object.getString("sec_com_id");
            this.sec_content=object.isNull("sec_content")?"":object.getString("sec_content");
            this.sec_uname=object.isNull("sec_uname")?"":object.getString("sec_uname");
            this.sec_introduction= object.isNull("sec_introduction")?"":object.getString("sec_introduction");
            this.sec_headimgs =object.isNull("sec_headimgs")?"":object.getString("sec_headimgs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public CommentEntity(){

    }

    public String getSec_uid() {
        return sec_uid;
    }

    public void setSec_uid(String sec_uid) {
        this.sec_uid = sec_uid;
    }

    public String getSec_com_id() {
        return sec_com_id;
    }

    public void setSec_com_id(String sec_com_id) {
        this.sec_com_id = sec_com_id;
    }

    public String getSec_content() {
        return sec_content;
    }

    public void setSec_content(String sec_content) {
        this.sec_content = sec_content;
    }

    public String getSec_uname() {
        return sec_uname;
    }

    public void setSec_uname(String sec_uname) {
        this.sec_uname = sec_uname;
    }

    public String getSec_introduction() {
        return sec_introduction;
    }

    public void setSec_introduction(String sec_introduction) {
        this.sec_introduction = sec_introduction;
    }

    public String getSec_headimgs() {
        return sec_headimgs;
    }

    public void setSec_headimgs(String sec_headimgs) {
        this.sec_headimgs = sec_headimgs;
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


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
