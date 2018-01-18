package com.njjd.domain;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by mrwim on 18/1/18.
 */

public class LiveRoom implements Serializable{
    private String id;
    private String chatRoomId;
    private String uid;//主讲人uid
    private String host_uid;//主持人uid
    private String title;//标题
    private String desc;//描述
    private String startTime;//开播时间
    private String headimg;//主讲人头像
    private String name;//主讲人姓名
    private String coverImg;//封面
    private String bannerImg;//轮播图
    public LiveRoom(JsonObject object){
        this.id=object.get("id").getAsString().replace(".0","");
        this.chatRoomId=object.get("h_id").getAsString();
        this.uid=object.get("uid").getAsString();
        this.host_uid=object.get("host_uid").getAsString();
        this.title=object.get("title").getAsString();
        this.desc=object.get("desc").getAsString();
        this.startTime=object.get("start_time").getAsString();
        this.headimg=object.get("headimgs").getAsString();
        this.name=object.get("uname").getAsString();
        this.coverImg=object.get("headimgs").getAsString();//
//        this.bannerImg=object.get("bg_img").getAsString();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHost_uid() {
        return host_uid;
    }

    public void setHost_uid(String host_uid) {
        this.host_uid = host_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }
}
