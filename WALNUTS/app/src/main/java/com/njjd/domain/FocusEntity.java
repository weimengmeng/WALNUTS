package com.njjd.domain;

/**
 * Created by mrwim on 17/8/15.
 */

public class FocusEntity {
    private String id;
    private String name;
    private String head;
    private String date;
    private String introduction;

    public FocusEntity(String id, String name, String head, String date,String introduction) {
        this.id = id;
        this.name = name;
        this.head = head;
        this.date = date;
        this.introduction=introduction;
    }
    public FocusEntity(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getData() {
        return date;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setData(String date) {
        this.date = date;
    }
}
