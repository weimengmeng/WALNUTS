package com.njjd.domain;

import com.njjd.application.AppAplication;
import com.njjd.utils.SPUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mrwim on 17/7/26.
 */
@Entity
public class QuestionEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    private String questionId;
    private String title;
    private String content;
    private String photo;//内容图片
    private String pic;//前三位参与人头像
    private String uids;//前三位参与人uid
    private String focusNum;//关注人数
    private String answerNum;//评论人数
    private int isFocus;//是否关注
    private String dateTime;//发布时间
    private String tag;
    private String kind;
    private String tag_id;

    public QuestionEntity(JSONObject object,String kind){
        try {
            this.questionId=object.isNull("id")?"0":object.getString("id");
            this.title=object.isNull("title")?"":object.getString("title");
            this.content=object.isNull("contents")?"":object.getString("contents");
            this.answerNum=object.isNull("answer_num")?"0":object.getString("answer_num");
            this.focusNum=object.isNull("follow_num")?"0":object.getString("follow_num");
            this.dateTime=object.isNull("change_time")?"":object.getString("change_time");
            this.photo=object.isNull("imgs")?"":object.getString("imgs").replace("[","").replace("]","").replace("\\/","/").replace("\\\\","/");
            this.pic=object.isNull("headimgs")?object.getString("authheadimg"):object.getString("headimgs");
            this.uids=object.isNull("uids")? SPUtils.get(AppAplication.getContext(),"userId","").toString():object.getString("uids");
            this.tag="";
            this.isFocus=0;
            this.kind=kind;
            this.tag_id="";
        }catch (JSONException e){

        }
    }
    @Generated(hash = 226711368)
    public QuestionEntity(String questionId, String title, String content, String photo, String pic, String uids, String focusNum,
            String answerNum, int isFocus, String dateTime, String tag, String kind, String tag_id) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.pic = pic;
        this.uids = uids;
        this.focusNum = focusNum;
        this.answerNum = answerNum;
        this.isFocus = isFocus;
        this.dateTime = dateTime;
        this.tag = tag;
        this.kind = kind;
        this.tag_id = tag_id;
    }
    @Generated(hash = 98121125)
    public QuestionEntity() {
    }
    public String getQuestionId() {
        return this.questionId;
    }
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getPhoto() {
        return this.photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getPic() {
        return this.pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getFocusNum() {
        return this.focusNum;
    }
    public void setFocusNum(String focusNum) {
        this.focusNum = focusNum;
    }
    public String getAnswerNum() {
        return this.answerNum;
    }
    public void setAnswerNum(String answerNum) {
        this.answerNum = answerNum;
    }
    public int getIsFocus() {
        return this.isFocus;
    }
    public void setIsFocus(int isFocus) {
        this.isFocus = isFocus;
    }
    public String getDateTime() {
        return this.dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getKind() {
        return this.kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getTag_id() {
        return this.tag_id;
    }
    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }
    public String getUids() {
        return this.uids;
    }
    public void setUids(String uids) {
        this.uids = uids;
    }
}
