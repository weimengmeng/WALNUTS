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
    private String coverImg="https://img.qlchat.com/qlLive/temp/MBJCY9YR-D8P2-PC4O-1516176855035-28N6DM5KF3AG.JPG?x-oss-process=image/resize,h_1600,w_1600";//封面
    private String bannerImg="https://img.qlchat.com/qlLive/temp/FDJIJ8D9-1FJX-8K4B-1516176855125-31UNHQGYKVQQ.JPG,https://img.qlchat.com/qlLive/temp/DESDDOWE-J2AE-RYFN-1516176855228-5HC6A7TI71JR.JPG,https://img.qlchat.com/qlLive/temp/8U68QJ3U-4ZUP-6TGA-1516176855304-5YJAXXFOTJNY.JPG,https://img.qlchat.com/qlLive/temp/7LVDUMOP-JX8W-VA2H-1516176855481-K7A8Q3GB8Q59.JPG";//轮播图
    private String shareUrl;//直播分享地址
    private String members;//历史人数进出次数
    private String currentNum;//当前在线人数
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
        this.members=object.get("num").getAsString().replace(".0","");
//        this.coverImg=object.get("cover_img").isJsonNull()?"":object.get("cover_img").getAsString();
//        this.bannerImg=object.get("bg_img").isJsonNull()?"":object.get("bg_img").getAsString();
//        this.shareUrl=object.get("url").isJsonNull()?"":object.get("url").getAsString();
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(String currentNum) {
        this.currentNum = currentNum;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
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
