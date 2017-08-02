package com.njjd.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

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
    private String focusNum;//关注人数
    private String answerNum;//评论人数
    private int isFocus;//是否关注
    private String dateTime;//发布时间
    private String tag;
    @Generated(hash = 1659159985)
    public QuestionEntity(String questionId, String title, String content,
            String photo, String pic, String focusNum, String answerNum,
            int isFocus, String dateTime, String tag) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.pic = pic;
        this.focusNum = focusNum;
        this.answerNum = answerNum;
        this.isFocus = isFocus;
        this.dateTime = dateTime;
        this.tag = tag;
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
}
