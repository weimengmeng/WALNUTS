package com.njjd.domain;

import java.io.Serializable;

/**
 * Created by mrwim on 17/9/14.
 */

public class ColumnEntity implements Serializable{
    private String id;//栏目ID
    private String head;
    private String name;
    private String title;
    private String content;
    private String pic;

    public ColumnEntity(String id, String head, String name, String title, String content, String pic) {
        this.id = id;
        this.head = head;
        this.name = name;
        this.title = title;
        this.content = content;
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
